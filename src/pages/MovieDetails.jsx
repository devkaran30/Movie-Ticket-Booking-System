import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { movieApi, showApi } from "../services/api";
import { formatDate, formatDuration, formatMoney, formatTime, posterFallback } from "../utils/format";

export default function MovieDetails() {
  const { movieId } = useParams();
  const [movie, setMovie] = useState(null);
  const [shows, setShows] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([movieApi.get(movieId), showApi.list()])
      .then(([movieResponse, showResponse]) => {
        setMovie(movieResponse.data);
        setShows(showResponse.data || []);
      })
      .catch((err) => setError(err.message));
  }, [movieId]);

  const movieShows = useMemo(
    () => shows.filter((show) => Number(show.movieId) === Number(movieId)),
    [shows, movieId]
  );

  if (error) return <section className="page-section"><div className="alert error">{error}</div></section>;
  if (!movie) return <section className="page-section"><div className="state-box">Loading movie…</div></section>;

  const poster = movie.posterUrl || posterFallback(movie.id || 0);

  return (
    <section className="details-page">
      <div className="details-card">
        <img className="details-poster" src={poster} alt={movie.title} onError={(e) => { e.currentTarget.src = posterFallback(movie.id || 0); }} />
        <div className="details-copy">
          <span className="eyebrow">{movie.status || "MOVIE"}</span>
          <h1>{movie.title}</h1>
          <p className="details-description">{movie.description || "Enjoy this movie at your preferred theatre and show time."}</p>
          <div className="details-tags">
            <span>{movie.language || "—"}</span><span>{movie.genre || "—"}</span><span>{movie.certificate || "U/A"}</span><span>{movie.movieFormat?.replace("_", " ") || "2D"}</span>
          </div>
          <p className="muted">{formatDuration(movie.durationMinutes)} • Released {formatDate(movie.releaseDate)}</p>
        </div>
      </div>

      <div className="page-section compact">
        <div className="section-head"><div><span className="eyebrow">SHOWS</span><h2>Available shows</h2></div></div>
        {movieShows.length ? (
          <div className="show-list">
            {movieShows.map((show) => (
              <article className="show-row" key={show.id}>
                <div><strong>{show.theatreName || "Theatre"}</strong><p>{show.screenName || "Screen"}</p></div>
                <div><strong>{formatDate(show.showDate)}</strong><p>{formatTime(show.startTime)} – {formatTime(show.endTime)}</p></div>
                <div><strong>{formatMoney(show.ticketPrice)}</strong><p>{show.status || "Scheduled"}</p></div>
                <Link className="btn btn-primary" to={`/seats/${show.id}`}>Select seats</Link>
              </article>
            ))}
          </div>
        ) : <div className="state-box">No shows are currently available for this movie.</div>}
      </div>
    </section>
  );
}
