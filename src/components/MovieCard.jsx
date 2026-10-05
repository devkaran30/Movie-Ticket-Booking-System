import { Link } from "react-router-dom";
import { formatDate, formatDuration, posterFallback } from "../utils/format";

export default function MovieCard({ movie, index = 0 }) {
  const image = movie.posterUrl || posterFallback(index);
  const running = movie.status === "RUNNING";
  return (
    <article className="movie-card">
      <div className="poster-wrap">
        <img src={image} alt={movie.title} onError={(event) => { event.currentTarget.src = posterFallback(index); }} />
        <span className={`status-badge ${running ? "live" : ""}`}>
          {running ? "Now Showing" : movie.status || "Upcoming"}
        </span>
      </div>
      <div className="movie-card-body">
        <h3>{movie.title}</h3>
        <p className="muted">{movie.genre || "Movie"} • {movie.language || "—"}</p>
        <div className="movie-meta">
          <span>{movie.certificate || "U/A"}</span>
          <span>{formatDuration(movie.durationMinutes)}</span>
          <span>{movie.movieFormat?.replace("_", " ") || "2D"}</span>
        </div>
        <p className="release">Release: {formatDate(movie.releaseDate)}</p>
        <Link className="btn btn-primary full" to={`/movie-details/${movie.id}`}>View & Book</Link>
      </div>
    </article>
  );
}
