import { useCallback, useEffect, useState } from "react";
import { adminApi, movieApi, screenApi, seatApi, showApi, theatreApi, userApi } from "../services/api";
import { formatDate, formatMoney } from "../utils/format";
import "./AdminDashboard.css";

const emptyData = { movies: [], shows: [], theatres: [], screens: [], seats: [], users: [] };
const movieStatuses = ["UPCOMING", "RUNNING", "COMPLETED", "INACTIVE"];
const showStatuses = ["SCHEDULED", "ACTIVE", "COMPLETED", "CANCELLED"];
const activeStatuses = ["ACTIVE", "INACTIVE"];
const userStatuses = ["ACTIVE", "INACTIVE", "BLOCKED"];
const seatStatuses = ["AVAILABLE", "INACTIVE", "BOOKED"];
const seatTypes = ["REGULAR", "PREMIUM", "RECLINER"];

function durationInHours(startTime, endTime) {
  if (!startTime || !endTime) return "";
  const [startHour, startMinute] = startTime.split(":").map(Number);
  const [endHour, endMinute] = endTime.split(":").map(Number);
  const start = startHour * 60 + startMinute;
  let end = endHour * 60 + endMinute;
  if (end <= start) end += 24 * 60;
  return Number(((end - start) / 60).toFixed(2)).toString();
}

function endTimeFromDuration(startTime, durationHours) {
  if (!startTime || !durationHours) return "";
  const [hours, minutes] = startTime.split(":").map(Number);
  const end = (hours * 60 + minutes + Math.round(Number(durationHours) * 60)) % (24 * 60);
  return `${String(Math.floor(end / 60)).padStart(2, "0")}:${String(end % 60).padStart(2, "0")}`;
}

function backendTime(time) {
  return time?.length === 5 ? `${time}:00` : time;
}

function movieDurationInHours(durationMinutes) {
  if (!durationMinutes) return "";
  return (Number(durationMinutes) / 60).toFixed(2);
}

function initialForm(type, item = {}) {
  if (type === "movies") return {
    title: item.title || "",
    description: item.description || "",
    language: item.language || "",
    genre: item.genre || "",
    durationHours: movieDurationInHours(item.durationMinutes),
    releaseDate: item.releaseDate || "",
    certificate: item.certificate || "",
    posterUrl: item.posterUrl || "",
    movieFormat: item.movieFormat || "TWO_D",
    status: item.status || "UPCOMING",
  };
  if (type === "shows") return {
    movieId: item.movieId || "",
    screenId: item.screenId || "",
    showDate: item.showDate || "",
    startTime: item.startTime?.slice(0, 5) || "",
    durationHours: durationInHours(item.startTime, item.endTime),
    ticketPrice: item.ticketPrice ?? "",
    status: item.status || "SCHEDULED",
  };
  if (type === "theatres") return {
    name: item.name || "",
    address: item.address || "",
    city: item.city || "",
    state: item.state || "",
    pincode: item.pincode || "",
    status: item.status || "ACTIVE",
  };
  return {
    theatreId: item.theatre?.id || "",
    name: item.name || "",
    totalSeats: item.totalSeats || "",
    status: item.status || "ACTIVE",
  };
}

export default function AdminDashboard() {
  const [data, setData] = useState(emptyData);
  const [tab, setTab] = useState("movies");
  const [editor, setEditor] = useState(null);
  const [form, setForm] = useState({});
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [saving, setSaving] = useState(false);
  const [seatScreenId, setSeatScreenId] = useState("");
  const [seatsPerRow, setSeatsPerRow] = useState(10);
  const [seatType, setSeatType] = useState("REGULAR");
  const [generatingSeats, setGeneratingSeats] = useState(false);

  const fetchData = useCallback(async () => {
    const [movies, shows, theatres, screens, seats, users] = await Promise.all([
      movieApi.list(), showApi.list(), theatreApi.list(), screenApi.list(), seatApi.list(), userApi.list(),
    ]);
    return {
        movies: movies.data || [],
        shows: shows.data || [],
        theatres: theatres.data || [],
        screens: screens.data || [],
        seats: seats.data || [],
        users: users.data || [],
    };
  }, []);

  useEffect(() => {
    fetchData()
      .then(setData)
      .catch((err) => setError(err.message));
  }, [fetchData]);

  const load = async () => {
    try {
      setData(await fetchData());
    } catch (err) {
      setError(err.message);
    }
  };

  const openEditor = (type, item = null) => {
    setError("");
    setNotice("");
    setEditor({ type, item });
    setForm(initialForm(type, item || {}));
  };

  const updateField = (field, value) => setForm((current) => ({ ...current, [field]: value }));
  const closeEditor = () => setEditor(null);

  const save = async (event) => {
    event.preventDefault();
    if (!editor) return;
    setSaving(true);
    setError("");
    try {
      const { type, item } = editor;
      if (type === "movies") {
        const durationHours = Number(form.durationHours);
        if (!Number.isFinite(durationHours) || durationHours <= 0) {
          throw new Error("Enter a movie duration greater than zero hours.");
        }
        const movieFields = Object.fromEntries(
          Object.entries(form).filter(([field]) => field !== "durationHours")
        );
        const payload = {
          ...movieFields,
          durationMinutes: Math.round(durationHours * 60),
        };
        if (item) await adminApi.movie.update(item.id, payload);
        else await adminApi.movie.create(payload);
      } else if (type === "shows") {
        const durationHours = Number(form.durationHours);
        if (!Number.isFinite(durationHours) || durationHours <= 0) {
          throw new Error("Enter a show duration greater than zero hours.");
        }
        const payload = {
          movie: { id: Number(form.movieId) },
          screen: { id: Number(form.screenId) },
          showDate: form.showDate,
          startTime: backendTime(form.startTime),
          endTime: backendTime(endTimeFromDuration(form.startTime, durationHours)),
          ticketPrice: Number(form.ticketPrice),
          status: form.status,
        };
        if (item) await adminApi.show.update(item.id, payload);
        else await adminApi.show.create(payload);
      } else if (type === "theatres") {
        if (item) await adminApi.theatre.update(item.id, form);
        else await adminApi.theatre.create(form);
      } else {
        const payload = {
          theatre: { id: Number(form.theatreId) },
          name: form.name,
          totalSeats: Number(form.totalSeats),
          status: form.status,
        };
        if (item) await adminApi.screen.update(item.id, payload);
        else await adminApi.screen.create(payload);
      }
      setNotice(`${type.slice(0, -1)} ${item ? "updated" : "created"} successfully.`);
      setEditor(null);
      await load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const remove = async (type, item) => {
    const label = type.slice(0, -1);
    if (!window.confirm(`Delete this ${label}? This cannot be undone.`)) return;
    const actions = {
      movies: adminApi.movie.remove,
      shows: adminApi.show.remove,
      theatres: adminApi.theatre.remove,
      screens: adminApi.screen.remove,
      seats: adminApi.seat.remove,
      users: adminApi.user.remove,
    };
    setError("");
    setNotice("");
    try {
      await actions[type](item.id);
      setNotice(`${label} deleted.`);
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const updateUserStatus = async (user, status) => {
    setError("");
    setNotice("");
    try {
      await adminApi.user.updateStatus(user.id, status);
      setNotice(`${user.name}'s status updated.`);
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const selectedScreen = data.screens.find((screen) => String(screen.id) === String(seatScreenId)) || data.screens[0];
  const seatsForScreen = selectedScreen
    ? data.seats.filter((seat) => Number(seat.screen?.id) === Number(selectedScreen.id))
      .sort((left, right) => {
        const rowOrder = String(left.rowNumber).localeCompare(String(right.rowNumber), undefined, { numeric: true });
        return rowOrder || String(left.seatNumber).localeCompare(String(right.seatNumber), undefined, { numeric: true });
      })
    : [];
  const missingSeatCount = Math.max(0, Number(selectedScreen?.totalSeats || 0) - seatsForScreen.length);
  const rowLabel = (index) => {
    let label = "";
    let number = index + 1;
    while (number > 0) {
      number -= 1;
      label = String.fromCharCode(65 + (number % 26)) + label;
      number = Math.floor(number / 26);
    }
    return label;
  };

  const generateSeats = async () => {
    if (!selectedScreen || !missingSeatCount) return;
    const columns = Math.max(1, Number(seatsPerRow) || 1);
    const existingNumbers = new Set(seatsForScreen.map((seat) => seat.seatNumber));
    const seatsToAdd = [];
    for (let index = 0; seatsToAdd.length < missingSeatCount; index += 1) {
      const row = rowLabel(Math.floor(index / columns));
      const seatNumber = `${row}${(index % columns) + 1}`;
      if (!existingNumbers.has(seatNumber)) seatsToAdd.push({ seatNumber, rowNumber: row });
    }

    setGeneratingSeats(true);
    setError("");
    setNotice("");
    const results = [];
    for (let index = 0; index < seatsToAdd.length; index += 10) {
      const batch = seatsToAdd.slice(index, index + 10);
      results.push(...await Promise.allSettled(batch.map((seat) => adminApi.seat.create({
        screen: { id: selectedScreen.id },
        seatNumber: seat.seatNumber,
        rowNumber: seat.rowNumber,
        seatType,
        status: "AVAILABLE",
      }))));
      if (results.slice(-batch.length).some((result) => result.status === "rejected")) break;
    }
    const created = results.filter((result) => result.status === "fulfilled").length;
    try {
      await load();
      if (created === seatsToAdd.length) {
        setNotice(`Added ${created} ${created === 1 ? "seat" : "seats"} to ${selectedScreen.name}.`);
      } else {
        const failure = results.find((result) => result.status === "rejected");
        setError(`Added ${created} of ${seatsToAdd.length} seats. The remaining seats were not created: ${failure?.reason?.message || "the backend rejected the request"}`);
      }
    } finally {
      setGeneratingSeats(false);
    }
  };

  const updateSeatStatus = async (seat, status) => {
    setError("");
    setNotice("");
    try {
      await adminApi.seat.updateStatus(seat.id, status);
      setNotice(`${seat.seatNumber} status updated.`);
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const updateSeatType = async (seat, nextType) => {
    setError("");
    setNotice("");
    try {
      await adminApi.seat.update(seat.id, {
        screen: { id: seat.screen.id },
        seatNumber: seat.seatNumber,
        rowNumber: seat.rowNumber,
        seatType: nextType,
        status: seat.status,
      });
      setNotice(`${seat.seatNumber} type updated.`);
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const tabs = [
    ["Movies", "movies"], ["Shows", "shows"], ["Theatres", "theatres"],
    ["Screens", "screens"], ["Seats", "seats"], ["Users", "users"],
  ];
  const counts = tabs.map(([label, key]) => [label, data[key].length, key]);
  const typeLabel = tab.slice(0, -1);
  const fields = {
    movies: [
      ["title", "Title", "text", true], ["language", "Language", "text", true],
      ["genre", "Genre", "text", true],
      ["releaseDate", "Release date", "date", true], ["certificate", "Certificate", "text"],
      ["posterUrl", "Poster URL", "url"], ["description", "Description", "textarea"],
    ],
    theatres: [
      ["name", "Theatre name", "text", true], ["address", "Address", "text", true],
      ["city", "City", "text", true], ["state", "State", "text", true],
      ["pincode", "PIN code", "text", true],
    ],
  };

  const renderInput = ([field, label, kind, required = false]) => (
    <label key={field} className={kind === "textarea" ? "admin-span-2" : ""}>
      {label}
      {kind === "textarea"
        ? <textarea className="input admin-textarea" required={required} value={form[field]} onChange={(event) => updateField(field, event.target.value)} />
        : <input className="input" type={kind} required={required} min={kind === "number" ? "1" : undefined} value={form[field]} onChange={(event) => updateField(field, event.target.value)} />}
    </label>
  );

  return <section className="page-section admin-page">
    <div className="page-title"><div><span className="eyebrow">SUPER ADMIN</span><h1>Administration</h1><p>Create, update and remove the resources managed by the booking system.</p></div></div>
    {error && <div className="alert error">{error}</div>}
    {notice && <div className="alert success">{notice}</div>}
    <div className="admin-stats">{counts.map(([label, count, key]) => <button key={key} className={tab === key ? "active" : ""} onClick={() => { setTab(key); closeEditor(); }}><strong>{count}</strong><span>{label}</span></button>)}</div>

    <div className="admin-toolbar">
      <h2>{tabs.find(([, key]) => key === tab)?.[0]}</h2>
    {tab !== "users" && tab !== "seats" && <button className="btn btn-primary" onClick={() => openEditor(tab)}>+ Add {typeLabel}</button>}
    </div>

    {editor && <form className="admin-editor" onSubmit={save}>
      <div className="admin-editor-heading"><h3>{editor.item ? `Edit ${typeLabel}` : `Add ${typeLabel}`}</h3><button type="button" className="admin-close" onClick={closeEditor} aria-label="Close editor">×</button></div>
      <div className="admin-form-grid">
        {editor.type === "movies" && <>
          {fields.movies.map(renderInput)}
          <label>Duration (hours)<input className="input" type="number" min="0.25" max="24" step="0.01" placeholder="e.g. 2.5" required value={form.durationHours} onChange={(event) => updateField("durationHours", event.target.value)} /><small className="admin-field-help">Use decimal hours; 2.5 means 2 hours 30 minutes.</small></label>
          <label>Format<select className="input" value={form.movieFormat} onChange={(event) => updateField("movieFormat", event.target.value)}><option value="TWO_D">2D</option><option value="THREE_D">3D</option><option value="IMAX">IMAX</option></select></label>
          <label>Status<select className="input" value={form.status} onChange={(event) => updateField("status", event.target.value)}>{movieStatuses.map((status) => <option key={status}>{status}</option>)}</select></label>
        </>}
        {editor.type === "shows" && <>
          <label>Movie<select className="input" required value={form.movieId} onChange={(event) => updateField("movieId", event.target.value)}><option value="">Choose a movie</option>{data.movies.map((movie) => <option value={movie.id} key={movie.id}>{movie.title}</option>)}</select></label>
          <label>Screen<select className="input" required value={form.screenId} onChange={(event) => updateField("screenId", event.target.value)}><option value="">Choose a screen</option>{data.screens.map((screen) => <option value={screen.id} key={screen.id}>{screen.name} — {screen.theatre?.name || data.theatres.find((theatre) => theatre.id === screen.theatre?.id)?.name || "Theatre"}</option>)}</select></label>
          <label>Show date<input className="input" type="date" required value={form.showDate} onChange={(event) => updateField("showDate", event.target.value)} /></label>
          <label>Start time<input className="input" type="time" required value={form.startTime} onChange={(event) => updateField("startTime", event.target.value)} /></label>
          <label>Duration (hours)<input className="input" type="number" min="0.25" max="24" step="0.25" placeholder="e.g. 2.5" required value={form.durationHours} onChange={(event) => updateField("durationHours", event.target.value)} /><small className="admin-field-help">Use decimal hours; 2.5 means 2 hours 30 minutes.</small></label>
          <label>Ticket price (INR)<span className="admin-currency-input"><span aria-hidden="true">₹</span><input className="input" type="number" min="0" step="0.01" placeholder="Enter ticket price" required value={form.ticketPrice} onChange={(event) => updateField("ticketPrice", event.target.value)} /></span><small className="admin-field-help">Enter the amount per ticket.</small></label>
          <div className="admin-time-preview"><span>Calculated end time</span><strong>{form.startTime && form.durationHours ? endTimeFromDuration(form.startTime, form.durationHours) : "Choose start time and duration"}</strong></div>
          <label>Status<select className="input" value={form.status} onChange={(event) => updateField("status", event.target.value)}>{showStatuses.map((status) => <option key={status}>{status}</option>)}</select></label>
        </>}
        {editor.type === "theatres" && <>
          {fields.theatres.map(renderInput)}
          <label>Status<select className="input" value={form.status} onChange={(event) => updateField("status", event.target.value)}>{activeStatuses.map((status) => <option key={status}>{status}</option>)}</select></label>
        </>}
        {editor.type === "screens" && <>
          <label>Theatre<select className="input" required value={form.theatreId} onChange={(event) => updateField("theatreId", event.target.value)}><option value="">Choose a theatre</option>{data.theatres.map((theatre) => <option value={theatre.id} key={theatre.id}>{theatre.name}</option>)}</select></label>
          <label>Screen name<input className="input" required value={form.name} onChange={(event) => updateField("name", event.target.value)} /></label>
          <label>Total seats<input className="input" type="number" min="1" required value={form.totalSeats} onChange={(event) => updateField("totalSeats", event.target.value)} /></label>
          <label>Status<select className="input" value={form.status} onChange={(event) => updateField("status", event.target.value)}>{["ACTIVE", "INACTIVE", "MAINTENANCE"].map((status) => <option key={status}>{status}</option>)}</select></label>
        </>}
      </div>
      <div className="admin-form-actions"><button type="button" className="btn btn-ghost" onClick={closeEditor}>Cancel</button><button className="btn btn-primary" disabled={saving}>{saving ? "Saving…" : "Save changes"}</button></div>
    </form>}

    {tab === "movies" && <div className="admin-table">
      <div className="table-head"><span>Movie</span><span>Status</span><span>Release</span><span>Actions</span></div>
      {data.movies.map((movie) => <div className="table-row" key={movie.id}><strong>{movie.title}</strong><span>{movie.status}</span><span>{formatDate(movie.releaseDate)}</span><div className="admin-row-actions"><button className="btn btn-ghost btn-small" onClick={() => openEditor("movies", movie)}>Edit</button><button className="btn btn-danger btn-small" onClick={() => remove("movies", movie)}>Delete</button></div></div>)}
      {!data.movies.length && <div className="admin-empty">No movies yet. Add one to start scheduling shows.</div>}
    </div>}
    {tab === "shows" && <div className="admin-table">
      <div className="table-head"><span>Movie</span><span>Theatre / Screen</span><span>Date · Price</span><span>Actions</span></div>
      {data.shows.map((show) => <div className="table-row" key={show.id}><strong>{show.movieTitle || "—"}<small>{show.status}</small></strong><span>{show.theatreName || "—"}<small>{show.screenName || "—"}</small></span><span>{formatDate(show.showDate)}<small>{formatMoney(show.ticketPrice)}</small></span><div className="admin-row-actions"><button className="btn btn-ghost btn-small" onClick={() => openEditor("shows", show)}>Edit</button><button className="btn btn-danger btn-small" onClick={() => remove("shows", show)}>Delete</button></div></div>)}
      {!data.shows.length && <div className="admin-empty">No shows yet. Add a movie and screen before scheduling one.</div>}
    </div>}
    {tab === "theatres" && <div className="admin-table">
      <div className="table-head"><span>Theatre</span><span>City</span><span>Status</span><span>Actions</span></div>
      {data.theatres.map((theatre) => <div className="table-row" key={theatre.id}><strong>{theatre.name}</strong><span>{theatre.city}, {theatre.state}</span><span>{theatre.status}</span><div className="admin-row-actions"><button className="btn btn-ghost btn-small" onClick={() => openEditor("theatres", theatre)}>Edit</button><button className="btn btn-danger btn-small" onClick={() => remove("theatres", theatre)}>Delete</button></div></div>)}
      {!data.theatres.length && <div className="admin-empty">No theatres yet. Add a theatre before creating screens.</div>}
    </div>}
    {tab === "screens" && <div className="admin-table">
      <div className="table-head"><span>Screen</span><span>Theatre</span><span>Seats / Status</span><span>Actions</span></div>
      {data.screens.map((screen) => <div className="table-row" key={screen.id}><strong>{screen.name}</strong><span>{screen.theatre?.name || data.theatres.find((theatre) => theatre.id === screen.theatre?.id)?.name || "—"}</span><span>{screen.totalSeats} seats · {screen.status}</span><div className="admin-row-actions"><button className="btn btn-ghost btn-small" onClick={() => openEditor("screens", screen)}>Edit</button><button className="btn btn-danger btn-small" onClick={() => remove("screens", screen)}>Delete</button></div></div>)}
      {!data.screens.length && <div className="admin-empty">No screens yet. Add a theatre before creating screens.</div>}
    </div>}
    {tab === "seats" && <div className="seat-admin">
      <div className="seat-generator">
        <div className="seat-generator-copy">
          <span className="eyebrow">SCREEN CAPACITY</span>
          <h3>Generate a seat map</h3>
          <p>Creates available seats in numbered rows and stops at the screen’s configured capacity. Existing seats are kept.</p>
        </div>
        {data.screens.length ? <>
          <div className="seat-generator-form">
            <label>Screen<select className="input" value={selectedScreen?.id || ""} onChange={(event) => setSeatScreenId(event.target.value)}>{data.screens.map((screen) => <option value={screen.id} key={screen.id}>{screen.name} · {screen.theatre?.name || "Theatre"}</option>)}</select></label>
            <label>Seats per row<input className="input" type="number" min="1" max="30" value={seatsPerRow} onChange={(event) => setSeatsPerRow(Math.min(30, Math.max(1, Number(event.target.value) || 1)))} /></label>
            <label>Seat type<select className="input" value={seatType} onChange={(event) => setSeatType(event.target.value)}>{seatTypes.map((type) => <option key={type}>{type}</option>)}</select></label>
          </div>
          <div className="seat-capacity-summary">
            <span><strong>{seatsForScreen.length}</strong> created</span>
            <span><strong>{selectedScreen?.totalSeats || 0}</strong> capacity</span>
            <button className="btn btn-primary" onClick={generateSeats} disabled={generatingSeats || missingSeatCount === 0}>
              {generatingSeats ? "Creating seats…" : missingSeatCount ? `Add ${missingSeatCount} missing seats` : "Screen is fully configured"}
            </button>
          </div>
        </> : <div className="admin-empty">Create a screen before adding seats.</div>}
      </div>
      {selectedScreen && <div className="admin-table seat-admin-table">
        <div className="table-head"><span>Seat</span><span>Row</span><span>Seat type</span><span>Status</span><span>Action</span></div>
        {seatsForScreen.map((seat) => <div className="table-row" key={seat.id}>
          <strong>{seat.seatNumber}</strong>
          <span>{seat.rowNumber}</span>
          <select aria-label={`Seat type for ${seat.seatNumber}`} className="input admin-status-select" value={seat.seatType} onChange={(event) => updateSeatType(seat, event.target.value)}>{seatTypes.map((type) => <option key={type}>{type}</option>)}</select>
          <select aria-label={`Status for ${seat.seatNumber}`} className="input admin-status-select" value={seat.status} onChange={(event) => updateSeatStatus(seat, event.target.value)}>{seatStatuses.map((status) => <option key={status}>{status}</option>)}</select>
          <div className="admin-row-actions"><button className="btn btn-danger btn-small" onClick={() => remove("seats", seat)}>Delete</button></div>
        </div>)}
        {!seatsForScreen.length && <div className="admin-empty">No seats are configured for this screen yet.</div>}
      </div>}
    </div>}
    {tab === "users" && <div className="admin-table">
      <div className="table-head"><span>Name</span><span>Email</span><span>Role / Status</span><span>Actions</span></div>
      {data.users.map((user) => <div className="table-row" key={user.id}><strong>{user.name}</strong><span>{user.email}</span><span>{user.role}<small>{user.status}</small></span><div className="admin-row-actions"><select aria-label={`Change status for ${user.name}`} className="input admin-status-select" value={user.status} onChange={(event) => updateUserStatus(user, event.target.value)}>{userStatuses.map((status) => <option key={status}>{status}</option>)}</select><button className="btn btn-danger btn-small" onClick={() => remove("users", user)}>Delete</button></div></div>)}
      {!data.users.length && <div className="admin-empty">No users found.</div>}
    </div>}
  </section>;
}
