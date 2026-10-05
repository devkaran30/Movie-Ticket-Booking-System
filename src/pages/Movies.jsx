import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import MovieCard from "../components/MovieCard";
import { movieApi } from "../services/api";

export default function Movies() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [movies, setMovies] = useState([]);
  const query = searchParams.get("search") || "";
  const [language, setLanguage] = useState("ALL");
  const [status, setStatus] = useState("ALL");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    movieApi.list().then((response) => setMovies(response.data || []))
      .catch((err) => setError(err.message)).finally(() => setLoading(false));
  }, []);

  const languages = useMemo(() => ["ALL", ...new Set(movies.map((movie) => movie.language).filter(Boolean))], [movies]);
  const filtered = movies.filter((movie) =>
    movie.title?.toLowerCase().includes(query.toLowerCase()) &&
    (language === "ALL" || movie.language === language) &&
    (status === "ALL" || movie.status === status)
  );

  return (
    <section className="page-section">
      <div className="page-title">
        <div><span className="eyebrow">MOVIES</span><h1>Choose your movie</h1><p>Live movie data from the Spring Boot backend.</p></div>
      </div>
      <div className="filter-bar">
        <input className="input" value={query} onChange={(event) => {
          const nextParams = new URLSearchParams(searchParams);
          if (event.target.value) nextParams.set("search", event.target.value);
          else nextParams.delete("search");
          setSearchParams(nextParams, { replace: true });
        }} placeholder="Search by movie title" />
        <select className="input select" value={language} onChange={(e) => setLanguage(e.target.value)}>
          {languages.map((item) => <option key={item} value={item}>{item === "ALL" ? "All languages" : item}</option>)}
        </select>
        <select className="input select" value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="ALL">All status</option><option value="RUNNING">Now showing</option><option value="UPCOMING">Upcoming</option><option value="COMPLETED">Completed</option>
        </select>
      </div>
      {loading && <div className="state-box">Loading movies…</div>}
      {error && <div className="alert error">{error}</div>}
      {!loading && !error && <div className="movie-grid">{filtered.map((movie, index) => <MovieCard key={movie.id} movie={movie} index={index} />)}</div>}
      {!loading && !filtered.length && <div className="state-box">No movies match your filters.</div>}
    </section>
  );
}
