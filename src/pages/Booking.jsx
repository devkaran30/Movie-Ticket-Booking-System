import { Link, useLocation, useNavigate } from "react-router-dom";
import { bookingApi } from "../services/api";
import { getCurrentUser } from "../utils/storage";
import { formatDate, formatMoney, formatTime } from "../utils/format";
import { useState } from "react";

export default function Booking() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const user = getCurrentUser();
  const [error, setError] = useState("");
  const [creating, setCreating] = useState(false);

  if (!state?.show || !state?.seatIds?.length) {
    return <section className="page-section"><div className="state-box">Booking details are missing. <Link to="/movies">Choose a movie</Link>.</div></section>;
  }

  const { show, seatIds, seatNumbers = seatIds, totalAmount, seatLocks = [] } = state;
  const createBooking = async () => {
    setCreating(true);
    setError("");
    try {
      const response = await bookingApi.create({
        userId: user.id,
        showId: show.id,
        seatIds,
        totalAmount,
      });
      navigate("/payment", { state: { booking: response.data, show, seatIds, seatLocks } });
    } catch (err) {
      setError(err.message);
    } finally {
      setCreating(false);
    }
  };

  return (
    <section className="page-section narrow">
      <div className="page-title"><div><span className="eyebrow">BOOKING</span><h1>Review your booking</h1><p>Confirm the details before payment.</p></div></div>
      {error && <div className="alert error">{error}</div>}
      <div className="checkout-card">
        <div className="checkout-main">
          <span className="eyebrow">MOVIE</span><h2>{show.movieTitle || "Movie"}</h2>
          <p>{show.theatreName} • {show.screenName}</p>
          <div className="checkout-grid">
            <div><span>Date</span><strong>{formatDate(show.showDate)}</strong></div>
            <div><span>Time</span><strong>{formatTime(show.startTime)}</strong></div>
            <div><span>Seats</span><strong>{seatNumbers.join(", ")}</strong></div>
            <div><span>Status</span><strong>PENDING</strong></div>
          </div>
        </div>
        <div className="checkout-total">
          <span>Total amount</span><strong>{formatMoney(totalAmount)}</strong>
          <small>Payment must exactly match the booking amount.</small>
          <button className="btn btn-primary full" onClick={createBooking} disabled={creating}>{creating ? "Creating booking…" : "Proceed to payment"}</button>
        </div>
      </div>
    </section>
  );
}
