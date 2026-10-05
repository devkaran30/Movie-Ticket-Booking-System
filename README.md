<<<<<<< HEAD
# MovieBook Frontend

React 19 + Vite frontend for the Movie Ticket Booking System backend.

## Backend contract

The UI is wired to the existing Spring Boot endpoints under `/api` and does not invent alternative business APIs.

- Backend port: `8585`
- Vite dev proxy: `/api` -> `http://localhost:8585`
- Production can set `VITE_API_BASE_URL` to the deployed backend `/api` base.

## Customer flow

1. Register/login using `/api/auth`.
2. Browse movies from `/api/movies`.
3. Browse active/scheduled shows from `/api/shows`.
4. Load seats from `/api/seats`, filter them to the selected show's screen, and load existing locks from `/api/seat-locks`.
5. Lock each selected seat for 5 minutes.
6. Create a real booking using `/api/bookings/create`.
7. Process payment using `/api/payments/process`.
8. The backend changes the booking to `CONFIRMED`, creates the ticket and releases the locks after successful payment.
9. Cancel only confirmed bookings. `/api/bookings/{id}/cancel` performs the refund and frees seats on the backend.

The show list and show detail endpoints return flattened `ShowResponse` fields (`movieId`, `movieTitle`, `screenId`, `screenName`, `theatreId`, and `theatreName`). The UI uses this response shape directly. Booking and ticket history are loaded from the user-specific `/api/bookings/user/{userId}` and `/api/tickets/user/{userId}` endpoints.

## Admin

`SUPER_ADMIN` users can open `/admin` to create, edit and delete movies, shows, theatres, screens and seats. The Seats section generates missing seats up to a screen's configured capacity, lets admins choose seats per row and seat type, and supports changing an individual seat's type/status or deleting it. New seats are persisted through `/api/admin/seats` with a screen reference, row/seat number, type and `AVAILABLE` status. The section shows the current count and screen capacity, and does not duplicate existing seat numbers. Admins can also change a user's status or delete a user.

When creating or editing a show, admins enter the ticket price manually in INR and enter show duration in hours (decimal hours are supported, e.g. `2.5` = 2 hours 30 minutes). The form calculates the end time from the selected start time and duration, then sends the backend's existing `startTime`, `endTime`, and `ticketPrice` fields.

Movie duration is also entered in hours in the admin form and converted to whole minutes for the existing backend `durationMinutes` field and MySQL column. Movie cards and details format that stored runtime as hours and minutes.

## Java 21 alignment

The frontend does not modify Java code or introduce non-Java conventions. Payload names and enum values match the Java 21 Spring Boot backend exactly, including `SUPER_ADMIN`, `USER`, `RUNNING`, `UPCOMING`, `ACTIVE`, `SCHEDULED`, `LOCKED`, `UPI`, `CREDIT_CARD`, `DEBIT_CARD`, and `NET_BANKING`.

The uploaded project already declares `<java.version>21</java.version>`. This frontend therefore consumes that backend contract without weakening or replacing its business rules.

## Run

```bash
npm install
npm run dev
```

Keep the Spring Boot backend running on port `8585`.

The ZIP intentionally excludes `node_modules`; install dependencies on the target machine so native Vite/Rolldown packages are installed for that OS.
=======
# Frontend-Project
>>>>>>> d258a953552f082066defb5f138d4ae36b42f60b
