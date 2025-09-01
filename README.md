# Livestock Management Application

This document provides instructions on how to start the entire Livestock Management application, including both the frontend and backend components.

## Project Structure

The project is organized as a multi-module Maven project:
- `backend`: Spring Boot application (Java 21)
- `frontend`: Angular application (Angular 20)

## Prerequisites

- Java 21 or later
- Node.js v20.19.0 or later
- npm 10.2.4 or later
- Maven 3.8 or later

## Starting the Application

There are two main approaches to starting the application:

### Option 1: Using Maven (Recommended for Production)

This approach builds both the frontend and backend and serves the Angular application through the Spring Boot backend.

1. Build the entire project:
   ```bash
   mvn clean install
   ```

2. Run the backend application (which will serve the frontend):
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. Access the application at http://localhost:8080

### Option 2: Development Mode (Recommended for Development)

This approach runs the frontend and backend separately, which is better for development as it provides hot-reloading for the frontend.

1. Start the backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. In a separate terminal, start the frontend:
   ```bash
   cd frontend
   npm install  # Only needed the first time or when dependencies change
   npm start
   ```

3. Access the frontend at http://localhost:4200
   - The frontend will automatically proxy API requests to the backend at http://localhost:8080

## Notes

- The backend runs on port 8080 by default
- The frontend development server runs on port 4200 by default
- When using Option 1, the Angular application is built and included in the Spring Boot application, so you only need to access the backend URL
- When using Option 2, you need to access the frontend URL, which will communicate with the backend through API calls

## Troubleshooting

- If you encounter port conflicts, you can change the backend port by adding `server.port=<port>` to `backend/src/main/resources/application.properties`
- If you encounter issues with the frontend not connecting to the backend, you may need to create a proxy configuration. Create a file `frontend/proxy.conf.json` with:
  ```json
  {
    "/api": {
      "target": "http://localhost:8080",
      "secure": false
    }
  }
  ```
  And update the `start` script in `frontend/package.json` to:
  ```
  "start": "ng serve --proxy-config proxy.conf.json",
  ```
