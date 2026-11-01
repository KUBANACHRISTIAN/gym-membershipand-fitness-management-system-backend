# Gym Membership and Fitness Management System - Backend

- **Student name:** Iradukunda Kubana Christian
- **Student ID:** 27143
- **Project:** Gym Membership and Fitness Management System

Spring Boot REST API with Spring Data JPA and PostgreSQL.
Entities: Member, Trainer, TrainingSpecialty, Studio, TrainingSession, WorkoutLog.

## Requirements

- Java 17 or higher
- PostgreSQL
- Postman (to run the tests)

## Steps to run the API

1. Clone the repository:
   ```bash
   git clone https://github.com/KUBANACHRISTIAN/gym-membershipand-fitness-management-system-backend.git
   cd gym-membershipand-fitness-management-system-backend
   ```
2. Create the database in PostgreSQL:
   ```sql
   CREATE DATABASE gym;
   ```
3. Open `src/main/resources/application.properties` and set your PostgreSQL username and password:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```
4. Start the application:
   ```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
   ```
   Tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`).
   The API runs on `http://localhost:8086`.

## Testing with Postman

1. Import `gym-membershipand-fitness-management-system-midterm.postman_collection.json` into Postman.
2. Run the A2 requests first to enter the seed data (training specialties, studios, trainers, members, training sessions).
3. Run the other requests by requirement ID (B1 ... F2, Bonus). Each requirement has a success case and a failing case (400 / 404 / 409).

## Endpoints

| ID | Method | Path |
|----|--------|------|
| A2 | POST, GET, GET, PUT, DELETE | `/api/{path}/save`, `/all`, `/{id}`, `/update/{id}`, `/delete/{id}` for `training-specialties`, `studios`, `trainers`, `members`, `training-sessions` |
| B1 | GET | `/api/members/by-last-name?lastName=mugisha` |
| B2 | GET | `/api/training-sessions/by-status?status=BOOKED` |
| B3 | GET | `/api/training-sessions/between?start=2026-10-01&end=2026-10-31` |
| C1 | GET | `/api/trainers/by-training-specialty?name=cardio` |
| C2 | GET | `/api/studios/free` |
| C3 | GET | `/api/training-specialties/unused` |
| C4 | GET | `/api/members/of-trainer/{trainerId}` |
| D1 | POST | `/api/training-sessions/save` |
| D2 | PATCH | `/api/training-sessions/{id}/status?status=CHECKED_IN` |
| D3 | PUT | `/api/trainers/{trainerId}/training-specialties/{trainingSpecialtyId}` |
| D4 | PATCH | `/api/trainers/{trainerId}/studio?studioNumber=4` |
| E1 | POST | `/api/workout-logs/save?trainingSessionId={id}` |
| E2 | GET | `/api/workout-logs/member/{membershipNumber}` |
| E3 | DELETE | `/api/workout-logs/delete/{id}` |
| F1 | GET | `/api/training-sessions/stats/by-status` |
| F2 | PATCH | `/api/training-sessions/cancel-day?trainerId={id}&date=2026-10-20` |
| Bonus | GET | `/api/dashboard/summary` |
| Bonus | GET | `/api/training-sessions/page?page=0&size=5&sort=sessionDate,desc` |