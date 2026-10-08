Movie Ticket Booking System
A web-based Movie Ticket Booking System built with Java and Spring
Boot. The application provides REST APIs for user authentication, movie
and theatre management, screens and seats, shows, bookings, payments,
refunds, tickets, and temporary seat locking.
Current project scope: This repository contains the Spring Boot
backend. The frontend is developed separately using React.

Project Overview
The system is designed around the following booking flow:
User Registration / Login
        ↓
Browse Movies
        ↓
Select Theatre
        ↓
Select Show
        ↓
View Seat Availability
        ↓
Select Seats
        ↓
Temporary Seat Lock
        ↓
Create Booking
        ↓
Payment
        ↓
Booking Confirmation
        ↓
Ticket / QR Support
        ↓
Cancellation / Refund
The system also provides administrative APIs for managing movies,
theatres, screens, seats, shows, users, bookings, payments, and refunds.
Main Features
User Features
- User registration
- User login
- Change password
- Browse and search movies
- Filter movies by status
- View movie details
- View theatres and screens
- View shows by movie
- View shows by movie and date
- View shows by theatre
- View seat availability for a show
- Select seats
- Temporary seat locking
- Create bookings
- View booking details
- View booking history
- Cancel bookings
- Process payments
- View payment details
- Request/view refunds
- View tickets
Admin Features
The current backend contains separate admin controllers for:
- Users
- Movies
- Theatres
- Screens
- Seats
- Shows
- Bookings
- Payments
- Refunds
Administrative operations include creating, viewing, updating, changing
status, and deleting supported resources.
Technology Stack
  Technology                 Purpose
  Java 21                    Backend programming language
  Spring Boot 4.1.1          Application framework
  Spring Web MVC             REST API development
  Spring Data JPA            Database access
  Hibernate                  ORM / entity-to-table mapping
  MySQL                      Relational database
  Spring Data Redis          Redis integration dependency
  Spring Boot Validation     Request/data validation
  SpringDoc OpenAPI          Swagger/OpenAPI documentation
  Maven                      Build and dependency management
  Git / GitHub               Version control
  JUnit / Spring Boot Test   Testing support
Project Architecture
The backend follows a layered architecture:
Frontend / Client
       ↓
Controller
       ↓
Request DTO
       ↓
Service Interface
       ↓
Service Implementation
       ↓
DAO
       ↓
Repository
       ↓
Entity
       ↓
MySQL Database
The response flow is:
MySQL
  ↓
Entity
  ↓
Repository
  ↓
DAO
  ↓
Service Implementation
  ↓
Mapper
  ↓
Response DTO
  ↓
Controller
  ↓
Frontend / Client
This separation keeps API handling, business logic, data access, and
persistence responsibilities independent.
Package Structure
src/main/java/com/nexturn/mtbs
│
├── config
│   ├── CorsConfig
│   ├── OpenApiConfig
│   └── RedisConfig
│
├── controller
│   ├── AuthController
│   ├── UserController
│   ├── MovieController
│   ├── TheatreController
│   ├── ScreenController
│   ├── SeatController
│   ├── ShowController
│   ├── BookingController
│   ├── BookingSeatController
│   ├── PaymentController
│   ├── RefundController
│   ├── TicketController
│   ├── SeatLockController
│   ├── ReportController
│   └── Admin*Controller classes
│
├── dao
│
├── dto
│   ├── request
│   └── response
│
├── entity
│
├── enums
│
├── exception
│
├── integration
│   ├── payment
│   └── notification
│
├── mapper
│
├── repository
│
├── scheduler
│
├── service
│   └── impl
│
├── util
│
└── validation
Database Model
The core database entities are:
USER
THEATRE
SCREEN
SEAT
MOVIE
SHOW
BOOKING
BOOKING_SEAT
PAYMENT
REFUND
The current implementation also contains application entities for:
SEAT_LOCK
TICKET
Main Relationships
User 1 ─────── * Booking

Theatre 1 ──── * Screen

Screen 1 ───── * Seat

Movie 1 ────── * Show

Screen 1 ───── * Show

Booking 1 ──── * BookingSeat

Seat 1 ─────── * BookingSeat

Booking 1 ──── * Payment

Payment 1 ──── * Refund

Seat + Show + User ─── SeatLock
Seat Locking
Seat locking is an important part of the booking process because
multiple users may attempt to select the same seat at the same time.
The current implementation uses a SeatLock entity and
SeatLockRepository.
A seat lock stores information such as:
- Seat
- User
- Show
- Lock creation time
- Expiry time
- Lock status
The seat-lock flow is:
User selects a seat
       ↓
Check whether seat is already confirmed/booked
       ↓
Check whether an active lock exists
       ↓
If active lock exists → reject
       ↓
If old lock has expired → mark it EXPIRED
       ↓
Create/save new LOCKED record
       ↓
User proceeds with booking
Before creating a real booking, the booking service verifies that each
selected seat has an active lock for the same user and show and that the
lock has not expired.
Important implementation note
The project includes spring-boot-starter-data-redis and a
RedisConfig class, but the current RedisConfig is only a scaffold.
The active seat-lock implementation in the uploaded backend is based on
the SeatLock entity, repository queries, status, and expiry time.
Booking Flow
A real booking is created through the booking service after validating:
1. User ID
2. Show ID
3. At least one selected seat
4. Positive booking amount
5. User existence
6. Show existence
7. Seat existence
8. Active seat lock for every selected seat
After validation:
Booking
   ↓
BookingSeat records
   ↓
Payment
   ↓
Booking confirmation
Each selected seat is represented through a BookingSeat record rather
than storing multiple seat IDs in a single database column.
Payment
Payment functionality is represented through:
Payment
PaymentService
PaymentServiceImpl
PaymentController
PaymentGateway
MockPaymentGateway
The project contains a payment gateway abstraction so that payment
processing can be separated from booking logic.
The current MockPaymentGateway is a scaffold for simulated payment
integration.
Ticket and QR Support
The backend contains:
Ticket
TicketService
TicketServiceImpl
TicketController
QrCodeService
QrCodeServiceImpl
TicketResponse
These components provide the structure required for ticket management
and QR-code support.
Cancellation and Refund
Bookings can be cancelled through the booking service.
The cancellation flow includes checks on booking and payment state
before updating the booking and refund-related information.
Refund functionality is provided through:
RefundController
RefundService
RefundServiceImpl
RefundRepository
Refund entity
Exception Handling
The project uses custom exceptions for common domain errors, including:
UserNotFoundException
MovieNotFoundException
TheatreNotFoundException
ScreenNotFoundException
SeatNotFoundException
ShowNotFoundException
BookingNotFoundException
PaymentNotFoundException
RefundNotFoundException
SeatAlreadyBookedException
SeatAlreadyLockedException
BookingCancellationException
PaymentFailedException
GlobalExceptionHandler uses @RestControllerAdvice to convert
exceptions into HTTP responses.
Examples:
404 NOT_FOUND
400 BAD_REQUEST
409 CONFLICT
500 INTERNAL_SERVER_ERROR
DTOs
The project separates request and response data using DTOs.
Request DTOs
Examples:
LoginRequest
RegisterRequest
MovieRequest
TheatreRequest
ScreenRequest
SeatRequest
ShowRequest
BookingRequest
SeatLockRequest
PaymentRequest
RefundRequest
ChangePasswordRequest
UserStatusRequest
Response DTOs
Examples:
LoginResponse
UserResponse
MovieResponse
TheatreResponse
ScreenResponse
SeatResponse
ShowResponse
BookingResponse
PaymentResponse
RefundResponse
TicketResponse
ReportResponse
DTOs help keep API data separate from database entities.
Authentication and Roles
The current project implements an email/password authentication flow
through:
AuthController
AuthService
AuthServiceImpl
UserRepository
The project currently uses two application roles:
USER
SUPER_ADMIN
Spring Security/JWT is not included in the current implementation.
Password handling and production-grade authentication/security should be
strengthened before production deployment.
Configuration
Main configuration is stored in:
src/main/resources/application.properties
The application is configured for:
- MySQL
- JPA/Hibernate
- Server port 8585
Example configuration:
spring.application.name=movie-ticket-booking-system

spring.datasource.url=jdbc:mysql://localhost:3306/movie_ticket_booking
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8585
Do not commit real database passwords, API keys, or other secrets to
GitHub.
Prerequisites
Install the following before running the project:
- Java 21
- MySQL
- Maven (optional because Maven Wrapper is included)
- Git
- Redis only if/when Redis-backed functionality is enabled
Database Setup
Create the database in MySQL:
CREATE DATABASE movie_ticket_booking;
Then update the database credentials in:
src/main/resources/application.properties
Example:
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
Hibernate is configured with:
spring.jpa.hibernate.ddl-auto=update
so the application can update the schema based on the mapped entities.
Running the Application
Using Maven Wrapper on Windows
mvnw.cmd clean install
Then:
mvnw.cmd spring-boot:run
Or using Maven
mvn clean install
mvn spring-boot:run
The application is configured to run on:
http://localhost:8585
API Documentation
SpringDoc OpenAPI is included in the project.
After starting the application, Swagger UI can be accessed at:
http://localhost:8585/swagger-ui.html
If the generated SpringDoc setup exposes the alternate path, use:
http://localhost:8585/swagger-ui/index.html
Swagger can be used to inspect and test the available REST endpoints.
API Modules
The current backend exposes APIs for:
Authentication
Users
Movies
Theatres
Screens
Seats
Shows
Bookings
Booking Seats
Seat Locks
Payments
Refunds
Tickets
Admin operations
Examples of endpoint groups:
/api/auth
/api/users
/api/movies
/api/theatres
/api/screens
/api/seats
/api/shows
/api/bookings
/api/seat-locks
/api/payments
/api/refunds
/api/tickets
Development Workflow
This is a team project managed using Git and GitHub.
Recommended workflow:
git checkout main
git pull --rebase origin main

git checkout -b feature-name
After completing a feature:
git add .
git commit -m "Describe the change"
git push -u origin feature-name
Create a Pull Request and merge the feature branch into main after
review.
Avoid committing directly to main when multiple team members are
working simultaneously.
Team
  Team Member     Primary Area
  Dev Karan       Backend development
  Surya Prakash   Backend development
  Manish          React frontend development
  Hariharan       React frontend development
The backend work is primarily organized around the Spring Boot layered
architecture, while the frontend is developed separately using React.
Project Structure
Movie-Ticket-Booking-System
│
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nexturn/mtbs/
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
Future Improvements
The current repository contains several integration/configuration
extension points. Possible future improvements include:
- Complete Redis-backed distributed seat locking
- Production-ready payment gateway integration
- Complete email/SMS notification implementation
- Complete QR-code generation integration
- Complete reporting implementation
- Stronger authentication and authorization
- Password hashing and secure credential handling
- More comprehensive unit and integration tests
- Centralized API security and role-based authorization
License
This project is developed as an academic/team capstone project.
