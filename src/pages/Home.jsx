import { useEffect, useMemo, useRef, useState } from "react";
import { Link } from "react-router-dom";
import MovieCard from "../components/MovieCard";
import { movieApi, showApi } from "../services/api";
import bannerOne from "../assets/movies/banners/banner1.png";
import bannerTwo from "../assets/movies/banners/banner2.png";
import bannerThree from "../assets/movies/banners/banner3.png";
import "./Home.css";

const banners = [
  { image: bannerOne, alt: "Featured movie banner one" },
  { image: bannerTwo, alt: "Featured movie banner two" },
  { image: bannerThree, alt: "Featured movie banner three" },
];

export default function Home() {
  const [movies, setMovies] = useState([]);
  const [shows, setShows] = useState([]);
  const [slide, setSlide] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const movieRail = useRef(null);

  useEffect(() => {
    Promise.all([movieApi.list(), showApi.list()])
      .then(([movieResponse, showResponse]) => {
        setMovies(movieResponse.data || []);
        setShows(showResponse.data || []);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    const timer = window.setInterval(() => {
      setSlide((current) => (current + 1) % banners.length);
    }, 5500);
    return () => window.clearInterval(timer);
  }, []);

  const recommendedMovies = useMemo(() => {
    const active = movies.filter((movie) => movie.status === "RUNNING" || movie.status === "UPCOMING");
    return active.length ? active : movies;
  }, [movies]);

  const showMovieCount = shows.filter((show) => show.status === "ACTIVE" || show.status === "SCHEDULED").length;

  const moveSlide = (direction) => {
    setSlide((current) => (current + direction + banners.length) % banners.length);
  };

  return (
    <div className="home-page">
      <section className="home-carousel" aria-label="Featured promotions">
        <button className="carousel-arrow previous" onClick={() => moveSlide(-1)} aria-label="Previous banner">‹</button>
        <div className="carousel-track">
          {banners.map((banner, index) => {
            const offset = (index - slide + banners.length) % banners.length;
            const position = offset === banners.length - 1 ? -1 : offset;
            return <Link
              className={`carousel-slide ${position === 0 ? "current" : ""}`}
              to="/movies"
              key={banner.image}
              style={{
                transform: position < 0
                  ? "translateX(calc(-50% - 84vw))"
                  : position > 0
                    ? "translateX(calc(-50% + 84vw))"
                    : "translateX(-50%)",
              }}
              aria-hidden={position !== 0}
              tabIndex={position === 0 ? 0 : -1}
            >
              <img src={banner.image} alt={banner.alt} />
            </Link>;
          })}
        </div>
        <button className="carousel-arrow next" onClick={() => moveSlide(1)} aria-label="Next banner">›</button>
        <div className="carousel-dots" aria-label="Choose featured banner">
          {banners.map((banner, index) => <button
            key={banner.image}
            className={slide === index ? "active" : ""}
            onClick={() => setSlide(index)}
            aria-label={`Show banner ${index + 1}`}
            aria-current={slide === index ? "true" : undefined}
          />)}
        </div>
      </section>

      <section className="home-recommendations">
        <div className="home-section-heading">
          <div>
            <span className="eyebrow">NOW SHOWING & COMING SOON</span>
            <h1>Recommended Movies</h1>
            <p>Find your next big-screen moment.</p>
          </div>
          <div className="home-heading-actions">
            <button className="rail-control" onClick={() => movieRail.current?.scrollBy({ left: -280, behavior: "smooth" })} aria-label="Scroll to previous movies">‹</button>
            <button className="rail-control" onClick={() => movieRail.current?.scrollBy({ left: 280, behavior: "smooth" })} aria-label="Scroll to more movies">›</button>
            <Link to="/movies" className="home-see-all">See all movies <span aria-hidden="true">›</span></Link>
          </div>
        </div>

        {error && <div className="alert error">{error}</div>}
        {loading ? <div className="home-loading"><span />Loading movies…</div> :
          recommendedMovies.length ? (
            <div className="home-movie-rail" ref={movieRail}>
              {recommendedMovies.map((movie, index) => <MovieCard key={movie.id} movie={movie} index={index} />)}
            </div>
          ) : <div className="home-empty">
            <span className="home-empty-icon">✦</span>
            <strong>No movies listed just yet</strong>
            <span>Check back soon for the latest releases.</span>
            <Link to="/movies" className="home-see-all">Explore movies <span aria-hidden="true">›</span></Link>
          </div>}
      </section>

      <section className="home-discovery">
        <div>
          <span className="home-discovery-icon">⌁</span>
          <div><strong>Find a show near you</strong><span>{showMovieCount} upcoming or active shows</span></div>
        </div>
        <Link to="/shows" className="btn btn-primary">Browse shows</Link>
      </section>
    </div>
  );
}
