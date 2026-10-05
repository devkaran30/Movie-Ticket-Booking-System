import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { bookingApi, seatApi, seatLockApi, showApi } from "../services/api";
import { getCurrentUser } from "../utils/storage";
import { formatMoney, formatTime } from "../utils/format";
import "./Seats.css";

const LOCK_MINUTES = 3;
const seatTypeOrder = ["REGULAR", "PREMIUM", "RECLINER"];

export default function Seats() {
  const { showId } = useParams();
  const navigate = useNavigate();
  const user = getCurrentUser();
  const [show, setShow] = useState(null);
  const [seats, setSeats] = useState([]);
  const [locks, setLocks] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [selected, setSelected] = useState([]);
  const [now, setNow] = useState(0);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [locking, setLocking] = useState(false);

  useEffect(() => {
    Promise.all([showApi.get(showId), seatApi.list(), seatLockApi.list(), bookingApi.list()])
      .then(([showResponse, seatResponse, lockResponse, bookingResponse]) => {
        setShow(showResponse.data);
        const screenId = Number(showResponse.data.screenId);
        setSeats((seatResponse.data || []).filter((seat) => Number(seat.screen?.id) === screenId));
        setLocks(lockResponse.data || []);
        setBookings(bookingResponse.data || []);
        setNow(Date.now());
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [showId]);

  useEffect(() => {
    const interval = window.setInterval(() => setNow(Date.now()), 1000);
    return () => window.clearInterval(interval);
  }, []);

  const screenSeats = seats;
  const seatGroups = seatTypeOrder
    .map((type) => ({
      type,
      seats: screenSeats.filter((seat) => seat.seatType === type),
    }))
    .filter((group) => group.seats.length > 0);
  const confirmedSeatIds = new Set(
    bookings
      .filter((booking) => Number(booking.showId) === Number(showId) && booking.bookingStatus === "CONFIRMED")
      .flatMap((booking) => booking.seatIds || [])
  );

  const unavailableIds = new Set([
    ...confirmedSeatIds,
    ...locks.filter((lock) =>
      lock.show?.id === Number(showId) &&
      lock.status === "LOCKED" &&
      lock.expiresAt &&
      new Date(lock.expiresAt).getTime() > now
    ).map((lock) => lock.seat?.id)
  ]);

  const toggle = (seat) => {
    if (seat.status !== "AVAILABLE" || unavailableIds.has(seat.id)) return;
    setSelected((current) => current.includes(seat.id) ? current.filter((id) => id !== seat.id) : [...current, seat.id]);
  };

  const total = selected.length * Number(show?.ticketPrice || 0);

  const continueToBooking = async () => {
    if (!selected.length) {
      setError("Select at least one available seat.");
      return;
    }
    setLocking(true);
    setError("");
    const expiry = new Date(Date.now() + LOCK_MINUTES * 60 * 1000);
    const pad = (value) => String(value).padStart(2, "0");
    const expiresAt = `${expiry.getFullYear()}-${pad(expiry.getMonth() + 1)}-${pad(expiry.getDate())}T${pad(expiry.getHours())}:${pad(expiry.getMinutes())}:${pad(expiry.getSeconds())}`;
    const created = [];
    try {
      for (const seatId of selected) {
        const response = await seatLockApi.create({
          seat: { id: seatId },
          user: { id: user.id },
          show: { id: Number(showId) },
          expiresAt,
          status: "LOCKED",
        });
        created.push(response.data);
      }
      navigate("/booking", {
        state: { show, seatIds: selected, seatNumbers: selected.map((id) => screenSeats.find((seat) => seat.id === id)?.seatNumber || id), seatLocks: created, totalAmount: total },
      });
    } catch (err) {
      await Promise.all(created.map((lock) => lock?.id ? seatLockApi.remove(lock.id).catch(() => {}) : null));
      setError(err.message || "Unable to lock the selected seats. Please try again.");
    } finally {
      setLocking(false);
    }
  };

  if (loading) return <section className="page-section"><div className="state-box">Loading seats…</div></section>;
  if (error && !show) return <section className="page-section"><div className="alert error">{error}</div></section>;

  return (
    <section className="page-section seat-page">
      <div className="page-title"><div><span className="eyebrow">SEAT SELECTION</span><h1>{show?.movieTitle || "Select seats"}</h1><p>{show?.theatreName} • {show?.screenName} • {formatTime(show?.startTime)}</p></div><Link to={`/movie-details/${show?.movieId}`} className="btn btn-ghost">Back</Link></div>
      {error && <div className="alert error">{error}</div>}

      <div className="seat-layout">
        <div className="seat-panel seat-picker">
          <div className="screen-wrap" aria-label="Screen at the front of the auditorium">
            <div className="screen-curve" />
            <span>SCREEN THIS WAY</span>
          </div>
          {seatGroups.length ? <div className="seat-sections">
            {seatGroups.map(({ type, seats: groupSeats }) => {
              const rows = [...new Set(groupSeats.map((seat) => seat.rowNumber || seat.seatNumber?.charAt(0) || "A"))]
                .sort((left, right) => left.localeCompare(right, undefined, { numeric: true }));
              return <section className="seat-tier" key={type}>
                <div className="seat-tier-heading"><span>{type.replace("_", " ")}</span><small>{formatMoney(show?.ticketPrice)} per seat</small></div>
                <div className="seat-map">
                  {rows.map((row) => {
                    const rowSeats = groupSeats
                      .filter((seat) => (seat.rowNumber || seat.seatNumber?.charAt(0) || "A") === row)
                      .sort((left, right) => left.seatNumber.localeCompare(right.seatNumber, undefined, { numeric: true }));
                    const aisleIndex = Math.ceil(rowSeats.length / 2);
                    return <div className="seat-row" key={`${type}-${row}`}>
                      <span className="row-label">{row}</span>
                      {rowSeats.map((seat, index) => {
                        const blocked = seat.status !== "AVAILABLE" || unavailableIds.has(seat.id);
                        const chosen = selected.includes(seat.id);
                        return <button
                          key={seat.id}
                          type="button"
                          disabled={blocked}
                          className={`seat ${blocked ? "blocked" : ""} ${chosen ? "selected" : ""} ${index === aisleIndex ? "aisle-gap" : ""}`}
                          onClick={() => toggle(seat)}
                          title={blocked ? `${seat.seatNumber} unavailable` : `${seat.seatNumber} · ${type}`}
                          aria-label={`${seat.seatNumber}, ${chosen ? "selected" : blocked ? "unavailable" : "available"}`}
                          aria-pressed={chosen}
                        >{chosen ? "✓" : seat.seatNumber}</button>;
                      })}
                      <span className="row-label row-label-end">{row}</span>
                    </div>;
                  })}
                </div>
              </section>;
            })}
          </div> : <div className="seat-empty">
            <strong>No seats are configured for this screen yet.</strong>
            <span>Ask the administrator to add seats to {show?.screenName || "this screen"} before booking.</span>
          </div>}
          <div className="seat-legend"><span><i className="available" />Available</span><span><i className="chosen" />Selected</span><span><i className="unavailable" />Unavailable</span></div>
        </div>

        <aside className="summary-card">
          <span className="eyebrow">YOUR SELECTION</span>
          <h2>{selected.length} seat{selected.length === 1 ? "" : "s"}</h2>
          <p className="muted">{selected.length ? selected.map((id) => screenSeats.find((seat) => seat.id === id)?.seatNumber).join(", ") : "Choose your seats from the map."}</p>
          <div className="summary-line"><span>Ticket price</span><strong>{formatMoney(show?.ticketPrice)}</strong></div>
          <div className="summary-line"><span>Tickets</span><strong>{selected.length}</strong></div>
          <div className="summary-total"><span>Total</span><strong>{formatMoney(total)}</strong></div>
          <button className="btn btn-primary full" onClick={continueToBooking} disabled={locking || !selected.length}>{locking ? "Locking seats…" : "Continue"}</button>
          <small>Seats are locked for {LOCK_MINUTES} minutes. Complete payment before the lock expires.</small>
        </aside>
      </div>
    </section>
  );
}
