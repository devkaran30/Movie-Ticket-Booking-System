import { useCallback, useEffect, useState } from "react";
import { bookingApi, ticketApi } from "../services/api";
import { getCurrentUser } from "../utils/storage";
import { formatDate, formatMoney } from "../utils/format";

export default function MyBookings() {
  const user = getCurrentUser();
  const [bookings, setBookings] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const load = useCallback(() => {
    return Promise.all([bookingApi.listByUser(user.id), ticketApi.listByUser(user.id)])
      .then(([bookingResponse, ticketResponse]) => {
        setBookings(bookingResponse.data || []);
        setTickets(ticketResponse.data || []);
        setError("");
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [user.id]);

  useEffect(() => {
    load();
    const refreshTimer = window.setInterval(load, 5000);
    return () => window.clearInterval(refreshTimer);
  }, [load]);

  const cancel = async (bookingId) => {
    if (!window.confirm("Cancel this confirmed booking? The backend will process the refund.")) return;
    try {
      await bookingApi.cancel(bookingId);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <section className="page-section"><div className="state-box">Loading your bookings…</div></section>;

  return (
    <section className="page-section">
      <div className="page-title"><div><span className="eyebrow">ACCOUNT</span><h1>My bookings</h1><p>Your bookings are read directly from the backend.</p></div></div>
      {error && <div className="alert error">{error}</div>}
      <div className="booking-list">
        {bookings.map((booking) => {
          const ticket = tickets.find((item) => item.bookingId === booking.id);
          const failed = booking.bookingStatus === "FAILED";
          return (
            <article className="booking-card" key={booking.id}>
              <div className="booking-icon">{failed ? "!" : "🎟"}</div>
              <div className="booking-info"><span className={`eyebrow ${failed ? "booking-failed-label" : ""}`}>{failed ? "PAYMENT FAILED · SEAT HOLD EXPIRED" : booking.bookingStatus}</span><h3>Booking #{booking.bookingNumber}</h3><p>{booking.seatIds?.length || 0} seat(s) • Show #{booking.showId}</p><p>Booked {formatDate(booking.bookingTime)}</p>{failed && <p className="booking-failure-note">The 3-minute payment window expired. Seats have been released.</p>}</div>
              <div className="booking-price"><strong>{formatMoney(booking.totalAmount)}</strong><span>{failed ? "Payment failed" : ticket ? `Ticket ${ticket.ticketNumber}` : "No ticket yet"}</span></div>
              {booking.bookingStatus === "CONFIRMED" && <button className="btn btn-danger" onClick={() => cancel(booking.id)}>Cancel</button>}
            </article>
          );
        })}
      </div>
      {!bookings.length && <div className="state-box">You have no bookings yet.</div>}
    </section>
  );
}
