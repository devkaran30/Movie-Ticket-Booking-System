import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { QRCodeSVG } from "qrcode.react";
import { paymentApi, ticketApi } from "../services/api";
import { getCurrentUser } from "../utils/storage";
import { formatDate, formatMoney, formatTime } from "../utils/format";

const DEMO_UPI_ID = "moviebook.demo@upi";

export default function Payment() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState("");
  const [ticketError, setTicketError] = useState("");
  const [payment, setPayment] = useState(null);
  const [ticket, setTicket] = useState(null);
  const [currentTime, setCurrentTime] = useState(0);

  useEffect(() => {
    const timer = window.setInterval(() => setCurrentTime(Date.now()), 1000);
    return () => window.clearInterval(timer);
  }, []);

  if (!state?.booking) return <section className="page-section"><div className="state-box">No pending booking found.</div></section>;

  const { booking, show, seatIds = [] } = state;
  const user = getCurrentUser();
  const amount = Number(booking.totalAmount);
  const upiUri = `upi://pay?pa=${encodeURIComponent(DEMO_UPI_ID)}&pn=${encodeURIComponent("MovieBook Demo")}&am=${amount.toFixed(2)}&cu=INR&tn=${encodeURIComponent(`Booking ${booking.bookingNumber || booking.id}`)}`;
  const lockExpiries = (state.seatLocks || [])
    .map((lock) => new Date(lock.expiresAt).getTime())
    .filter(Number.isFinite);
  const expiryTime = lockExpiries.length
    ? Math.min(...lockExpiries)
    : new Date(booking.bookingTime).getTime() + 3 * 60 * 1000;
  const secondsRemaining = currentTime === 0
    ? 180
    : Number.isFinite(expiryTime)
      ? Math.max(0, Math.ceil((expiryTime - currentTime) / 1000))
      : 0;
  const holdExpired = secondsRemaining === 0;

  const pay = async () => {
    if (holdExpired) return;
    setProcessing(true);
    setError("");
    setTicketError("");
    try {
      const response = await paymentApi.process({
        bookingId: booking.id,
        transactionId: `TXN${Date.now()}`,
        amount: booking.totalAmount,
        paymentMethod: "UPI",
      });
      const processedPayment = response.data || {};
      setPayment(processedPayment);
      if (processedPayment.paymentStatus === "FAILED") return;
      setTicket(response.data?.ticket || null);

      try {
        const ticketResponse = await ticketApi.listByUser(user.id);
        const confirmedTicket = (ticketResponse.data || []).find((item) => item.bookingId === booking.id);
        setTicket(confirmedTicket || response.data?.ticket || null);
      } catch (ticketLookupError) {
        setTicketError(`Payment succeeded, but ticket details could not be loaded: ${ticketLookupError.message}`);
      }
    } catch (paymentError) {
      setError(paymentError.message);
    } finally {
      setProcessing(false);
    }
  };

  if (payment?.paymentStatus === "FAILED") {
    return (
      <section className="page-section narrow payment-page">
        <div className="ticket-confirmation payment-failure">
          <div className="ticket-failure-icon" aria-hidden="true">!</div>
          <span className="eyebrow">PAYMENT NOT COMPLETED</span>
          <h1>Payment failed</h1>
          <p className="ticket-success-message">The 3-minute seat hold expired before payment completed. Your seats have been released.</p>
          <article className="movie-ticket">
            <div className="ticket-details">
              <div><span>Booking reference</span><strong>{booking.bookingNumber || booking.id}</strong></div>
              <div><span>Payment status</span><strong>FAILED — SEAT HOLD EXPIRED</strong></div>
              <div><span>Amount</span><strong>{formatMoney(booking.totalAmount)}</strong></div>
              <div><span>Movie</span><strong>{show?.movieTitle || "—"}</strong></div>
            </div>
          </article>
          <button className="btn btn-primary" onClick={() => navigate("/my-bookings")}>View my bookings</button>
        </div>
      </section>
    );
  }

  if (payment) {
    const ticketId = ticket?.ticketNumber || ticket?.id || booking.bookingNumber || booking.id;
    return (
      <section className="page-section narrow payment-page">
        <div className="ticket-confirmation">
          <div className="ticket-success-icon" aria-hidden="true">✓</div>
          <span className="eyebrow">BOOKING CONFIRMED</span>
          <h1>Your ticket is booked!</h1>
          <p className="ticket-success-message">Payment was successful. Your movie ticket is ready.</p>
          {ticketError && <div className="alert error">{ticketError}</div>}

          <article className="movie-ticket">
            <div className="ticket-top">
              <div>
                <span className="ticket-label">MOVIEBOOK E-TICKET</span>
                <h2>{show?.movieTitle || "Movie ticket"}</h2>
                <p>{show?.theatreName || "Theatre"}{show?.screenName ? ` · ${show.screenName}` : ""}</p>
              </div>
              <span className="ticket-status">CONFIRMED</span>
            </div>
            <div className="ticket-perforation" aria-hidden="true"><span /><span /></div>
            <div className="ticket-details">
              <div><span>Ticket ID</span><strong>{ticketId}</strong></div>
              <div><span>Name</span><strong>{user.name || "MovieBook customer"}</strong></div>
              <div><span>Price paid</span><strong>{formatMoney(booking.totalAmount)}</strong></div>
              <div><span>Theatre</span><strong>{show?.theatreName || "—"}</strong></div>
              <div><span>Date & time</span><strong>{formatDate(show?.showDate)} · {formatTime(show?.startTime)}</strong></div>
              <div><span>Screen</span><strong>{show?.screenName || "—"}</strong></div>
              <div className="ticket-seats"><span>Seats</span><strong>{seatIds.join(", ") || "—"}</strong></div>
              <div><span>Booking reference</span><strong>{booking.bookingNumber || booking.id}</strong></div>
            </div>
            <div className="ticket-footer">Please show this booking confirmation at the theatre.</div>
          </article>

          <button className="btn btn-primary" onClick={() => navigate("/my-bookings")}>Go to my bookings</button>
        </div>
      </section>
    );
  }

  return (
    <section className="page-section narrow payment-page">
      <div className="page-title"><div><span className="eyebrow">PAYMENT</span><h1>Pay with UPI</h1><p>Scan the demo QR code or use the UPI ID below.</p></div></div>
      {error && <div className="alert error">{error}</div>}
      <div className={`payment-hold-timer ${holdExpired ? "expired" : ""}`} role="status">
        {holdExpired
          ? "Your seat hold has expired. Seats are being released; check My Bookings for the final status."
          : `Seats held for ${Math.floor(secondsRemaining / 60)}:${String(secondsRemaining % 60).padStart(2, "0")} more`}
      </div>
      <div className="payment-card upi-payment-card">
        <div className="payment-amount"><span>Amount payable</span><strong>{formatMoney(booking.totalAmount)}</strong><small>Booking #{booking.bookingNumber}</small></div>

        <div className="upi-payment-content">
          <div className="upi-qr-panel">
            <div className="upi-qr-frame"><QRCodeSVG value={upiUri} size={190} level="M" includeMargin /></div>
            <strong>Scan with any UPI app</strong>
            <span>QR contains the demo UPI ID and booking amount</span>
          </div>
          <div className="upi-details">
            <span className="upi-demo-badge">DEMO UPI</span>
            <h2>UPI payment</h2>
            <p>Use this sample UPI ID for the demo payment screen.</p>
            <span className="upi-id-label">UPI ID</span>
            <div className="upi-id-value">{DEMO_UPI_ID}</div>
            <div className="upi-payee"><span>Payee</span><strong>MovieBook Demo</strong></div>
            <div className="upi-payee"><span>Amount</span><strong>{formatMoney(booking.totalAmount)}</strong></div>
            <button className="btn btn-primary full" onClick={pay} disabled={processing || holdExpired}>
              {processing ? "Processing…" : holdExpired ? "Seat hold expired" : `Confirm demo payment · ${formatMoney(booking.totalAmount)}`}
            </button>
            <small className="upi-disclaimer">Demo only — no real payment will be collected. The button completes the backend mock payment.</small>
          </div>
        </div>
        <p className="muted center">After successful demo payment, your booking is confirmed and ticket details will appear here.</p>
      </div>
    </section>
  );
}
