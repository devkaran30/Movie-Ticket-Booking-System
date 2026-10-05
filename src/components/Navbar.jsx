import { Link, NavLink, useNavigate } from "react-router-dom";
import { useState } from "react";
import { clearCurrentUser, getCurrentUser } from "../utils/storage";

export default function Navbar() {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const user = getCurrentUser();

  const submitSearch = (event) => {
    event.preventDefault();
    const query = search.trim();
    navigate(query ? `/movies?search=${encodeURIComponent(query)}` : "/movies");
    setOpen(false);
  };

  const logout = () => {
    clearCurrentUser();
    setOpen(false);
    navigate("/");
  };

  return (
    <header className="site-header">
      <div className="header-inner">
        <Link className="brand" to="/">
          <span className="brand-mark" aria-hidden="true">M</span>
          <span className="brand-word">Movie<b>Book</b></span>
        </Link>

        <form className="header-search" onSubmit={submitSearch} role="search">
          <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="10.8" cy="10.8" r="6.8" /><path d="m16 16 4.2 4.2" /></svg>
          <input aria-label="Search movies" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search for movies" />
          {search && <button type="button" onClick={() => setSearch("")} aria-label="Clear search">×</button>}
        </form>

        <Link to="/shows" className="header-location"><span className="location-pin" aria-hidden="true">⌖</span><span>Find a theatre</span><span className="location-chevron" aria-hidden="true">⌄</span></Link>

        <div className="header-actions">
          {user ? (
            <button className="profile-chip" onClick={() => navigate("/profile")}>
              <span className="avatar">{user.name?.charAt(0)?.toUpperCase() || "U"}</span>
              <span>{user.name || "Account"}</span>
            </button>
          ) : (
            <Link className="btn btn-primary sign-in-button" to="/login">Sign in</Link>
          )}
          <button className={`menu-toggle ${open ? "is-open" : ""}`} onClick={() => setOpen((value) => !value)} aria-label={open ? "Close menu" : "Open menu"} aria-expanded={open}>
            <span /><span /><span />
          </button>
        </div>
      </div>

      <div className="header-subnav">
        <nav className="desktop-nav">
          <NavLink to="/movies">Movies</NavLink>
          <NavLink to="/shows">Shows</NavLink>
          {user && <NavLink to="/my-bookings">My Bookings</NavLink>}
          {user?.role === "SUPER_ADMIN" && <NavLink to="/admin">Admin</NavLink>}
        </nav>
        <span className="subnav-note">Your next story starts here</span>
      </div>

      {open && (
        <div className="mobile-menu">
          <form className="mobile-search" onSubmit={submitSearch}>
            <input aria-label="Search movies" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search movies" />
            <button type="submit" aria-label="Submit movie search">Search</button>
          </form>
          <Link to="/movies" onClick={() => setOpen(false)}>Movies</Link>
          <Link to="/shows" onClick={() => setOpen(false)}>Shows</Link>
          {user && <Link to="/my-bookings" onClick={() => setOpen(false)}>My Bookings</Link>}
          {user && <Link to="/profile" onClick={() => setOpen(false)}>Profile</Link>}
          {user?.role === "SUPER_ADMIN" && <Link to="/admin" onClick={() => setOpen(false)}>Admin</Link>}
          {user ? (
            <button onClick={logout}>Logout</button>
          ) : (
            <Link to="/login" onClick={() => setOpen(false)}>Sign in</Link>
          )}
        </div>
      )}
    </header>
  );
}
