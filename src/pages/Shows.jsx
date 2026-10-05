import { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { showApi } from "../services/api";
import { formatDate, formatMoney, formatTime } from "../utils/format";

export default function Shows() {
  const [shows, setShows] = useState([]);
  const [date, setDate] = useState("");
  const [query] = useSearchParams();
  const movieId = query.get("movieId");

  useEffect(() => {
    showApi.list().then((response) => setShows(response.data || [])).catch(() => {});
  }, []);

  const dates = useMemo(() => [...new Set(shows.map((show) => show.showDate).filter(Boolean))].sort(), [shows]);
  const filtered = shows.filter((show) =>
    (!movieId || String(show.movieId) === movieId) &&
    (!date || show.showDate === date) &&
    (show.status === "ACTIVE" || show.status === "SCHEDULED")
  );

  return (
    <section className="page-section">
      <div className="page-title"><div><span className="eyebrow">SHOWS</span><h1>Pick a show</h1><p>Select the date, theatre and time that works for you.</p></div></div>
      <div className="filter-bar">
        <select className="input select" value={date} onChange={(e) => setDate(e.target.value)}>
          <option value="">All available dates</option>
          {dates.map((item) => <option key={item} value={item}>{formatDate(item)}</option>)}
        </select>
      </div>
      <div className="show-list">
        {filtered.map((show) => (
          <article className="show-row" key={show.id}>
            <div><strong>{show.movieTitle || "Movie"}</strong><p>Show #{show.id}</p></div>
            <div><strong>{show.theatreName || "Theatre"}</strong><p>{show.screenName || "Screen"}</p></div>
            <div><strong>{formatDate(show.showDate)}</strong><p>{formatTime(show.startTime)} – {formatTime(show.endTime)}</p></div>
            <div><strong>{formatMoney(show.ticketPrice)}</strong><p>{show.status}</p></div>
            <Link className="btn btn-primary" to={`/seats/${show.id}`}>Select seats</Link>
          </article>
        ))}
      </div>
      {!filtered.length && <div className="state-box">No active or scheduled shows found.</div>}
    </section>
  );
}
