---

# ComeOn Player Service

This **ComeOn Player Service** project is designed to manage players' information and sessions for a gaming application. The service allows players to register, log in, log out, and manage their session time limits.

Docker image for the solution is available
- [CI/CD Pipeline completed Build](https://github.com/DeepuGeorgeJacob/ComeonPlayer/actions)
- [Docker Image](https://hub.docker.com/r/deepugeorgejacob/comeon-player-service/tags)
  - ```bash
    docker run -p 7000:7000 --name comeon deepugeorgejacob/comeon-player-service:0.0.1
- [Swagger API Documentation](#swagger-api-documentation)
- [API Endpoints](#api-endpoints)
   - [Request and Response Tables](#request-and-response-tables)
---

## Table of Contents

- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
    - [Configure Database](#configure-database)
- [Run the Application](#run-the-application)
- [API Endpoints](#api-endpoints)
    - [Request and Response Tables](#request-and-response-tables)
- [Swagger API Documentation](#swagger-api-documentation)
- [Docker Instructions](#docker-instructions)
- [GitHub Actions (CI/CD)](#github-actions-cicd)

---

## Project Description

The **ComeOn Player Service** provides a REST API to manage player accounts and their session limits. This service allows players to:

- Register with details such as email, password, name, surname, date of birth, and address.
- Log in using email and password, with session management that tracks login times and enforces time limits on each player.
- Log out players by session identifier and prevent further actions if they reach their daily time limit.
- Set a daily session time limit for players, preventing them from logging in if they exceed their allotted time.

This service uses an embedded **H2** database by default, but can be configured for other databases if needed.

---

## Technologies Used

- **Spring Boot** 3.4.0
- **Spring Data JPA** (for database interaction)
- **Spring Security** (for authentication and session management)
- **Flyway** (for database migrations)
- **OpenAPI (Swagger)** for API documentation
- **JUnit** and **Mockito** (for testing)
- **Docker** (for containerization)
- **GitHub Actions** (for CI/CD)

---

## Features

- **Player Registration**: Allows creating a new player with required details.
- **Player Login**: Allows players to log in with email and password.
- **Session Management**: Tracks player login times and manages session duration.
- **Time Limit**: Sets a daily session time limit. Players cannot log in once they have exceeded their time limit.
- **Player Logout**: Allows players to log out by their session ID.
- **Database Integration**: Configured to work with H2 by default, but supports other databases.
- **Swagger UI**: Provides an interactive API documentation for testing endpoints.
- **Dockerized Application**: The project can be containerized using Docker for easy deployment.
- **CI/CD Integration**: GitHub Actions is used for continuous integration and deployment.

---

## Prerequisites

To run this project locally, ensure you have the following installed:

- **Java 21** or later
- **Maven** 3.x
- **H2 Database** (or another database of your choice)
- **Docker** (optional, for containerization)
- **GitHub account** (for CI/CD pipeline)
- **Docker Hub account** (for storing Docker images)

---

## Configuration

### Configure Database

This service uses **H2 Database** by default, but can easily be switched to another database like MySQL or PostgreSQL by modifying the `application.properties` file.

1. Open `src/main/resources/application.properties`.
2. Configure the database connection:

   For **H2** (default):

   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   ```

   For **MySQL** (example configuration):

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/comeon_db
   spring.datasource.username=root
   spring.datasource.password=rootpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Ensure that **Flyway** is enabled for database migrations:

   ```properties
   spring.flyway.enabled=true
   ```

---

## Run the Application

You can run the application in multiple ways. Below are the different options:

### 1. Run Using Maven

To run the application directly using Maven:

```bash
mvn spring-boot:run
```

This will compile and run the application on `http://localhost:7000`.

### 2. Run Using the Built JAR

To run the application from the built JAR file:

1. Build the JAR:

   ```bash
    ./mvnw clean package
   ```

2. Run the JAR:

   ```bash
   java -jar build/comeon-0.0.1-SNAPSHOT.jar
   ```

This will start the application and listen on `http://localhost:7000`.

1. Testing:

   To run unit and integration tests:
   ```bash
   ./mvnw test
   ```

---




## API Endpoints

### Base URL
`http://localhost:7000/api/player`

### Endpoints Overview

| **Endpoint**               | **HTTP Method** | **Description**                     |
|----------------------------|-----------------|-------------------------------------|
| `/register`                | POST            | Register a new player              |
| `/login`                   | POST            | Log in a player                    |
| `/logout/{id}`             | GET             | Log out a player by session ID     |
| `/update`                  | PUT             | Update player information          |

---

### Request and Response Tables

#### 1. **Register a Player**

| **Field**      | **Type**   | **Description**                     |
|----------------|-----------|-------------------------------------|
| `name`         | String    | Player's first name                 |
| `surname`      | String    | Player's last name                  |
| `email`        | String    | Player's email address              |
| `password`     | String    | Player's password (encrypted)       |
| `dateOfBirth`  | String    | Player's date of birth (ISO format) |
| `address`      | String    | Player's address                    |

**Request Example:**
```json
{
  "name": "John",
  "surname": "Doe",
  "email": "johndoe@example.com",
  "password": "password123",
  "dateOfBirth": "1990-01-01",
  "address": "123 Main St, Anytown, USA"
}
```

**Response Example:**
```json
{
  "info": "Player John registered with Id 1"
}
```

---

#### 2. **Login a Player**

| **Field**      | **Type**   | **Description**                     |
|----------------|-----------|-------------------------------------|
| `email`        | String    | Player's email address              |
| `password`     | String    | Player's password                   |

**Request Example:**
```json
{
  "email": "johndoe@example.com",
  "password": "password123"
}
```

**Response Example:**
```json
{
  "info": "User logged In with session Id 12345 With remaining 3600 Seconds"
}
```

---

#### 3. **Logout a Player**

| **Field**     | **Type**   | **Description**                     |
|---------------|-----------|-------------------------------------|
| `id`          | Long      | Session ID of the logged-in player  |

**Request Example:**
```json
{
  "id": 12345
}
```

**Response Example:**
```json
{
  "data": {
    "loginTime": "2024-11-30T10:00:00",
    "logoutTime": "2024-11-30T12:00:00",
    "remainingTimeLimit": 1800
  }
}
```

---

#### 4. **Update Player Information**

| **Field**             | **Type**   | **Description**                                   |
|-----------------------|-----------|---------------------------------------------------|
| `playerId`            | Long      | ID of the player to update                       |
| `isActive`            | Boolean   | Player's active status (true/false)               |
| `timeLimitInMinutes`  | Integer   | Time limit in minutes to add to the player's limit|

**Request Example:**
```json
{
  "playerId": 1,
  "isActive": true,
  "timeLimitInMinutes": 30
}
```

**Response Example:**
```json
{
  "info": "Player John is Updated"
}
```

---

## Swagger API Documentation

You can access the **Swagger UI** to explore and test the API directly from your browser.

To access the Swagger UI:

1. Start the application.
2. Open your browser and go to:

   ```
   http://localhost:7000/swagger-ui/index.html
   ```

---

## Docker Instructions

### Build Docker Image

To build the Docker image, run the following command:

```bash
docker build -t comeon-player-service:latest .
```

### Run Docker Container

Once the image is built, you can run the container with:

```bash
docker run -p 7000:7000 comeon-player-service:latest
```
CI/CD built in image available in [Docker Hub](https://hub.docker.com/r/deepugeorgejacob/comeon-player-service).

---

## GitHub Actions (CI/CD)

The project is integrated with **GitHub Actions** for continuous integration and deployment. Each push to the `main`