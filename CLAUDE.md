# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a House Rental Platform (房屋租售平台) - a full-stack real estate application with separate frontend (Vue 3) and backend (Spring Boot) components.

## Development Commands

### Frontend Development
```bash
cd frontend/
pnpm install          # Install dependencies
pnpm dev         # Start Vite dev server (port 5173)
pnpm build       # Build for production
```

### Backend Development
```bash
cd backend/
mvn spring-boot:run    # Start Spring Boot (port 8080)
mvn clean package      # Build WAR for deployment
mvn test              # Run unit tests
```

### Database Setup
The MySQL schema is in `sql/house.sql`. Import this file to initialize the database before running the application.

## Architecture Overview

### Technology Stack
- **Frontend**: Vue 3.5 + Vite + Element Plus + Pinia + Vue Router
- **Backend**: Spring Boot 3.5.8 + MyBatis + Spring Security + Java 17
- **Database**: MySQL with comprehensive schema for real estate operations

### Project Structure
```
frontend/src/          # Vue SPA
├── views/            # Page components (HouseView, ContractView, etc.)
├── components/       # Reusable UI components
├── stores/          # Pinia state management (user, house, contract stores)
├── api/             # Axios API service layer
├── router/          # Vue Router configuration
└── utils/           # Utility functions

backend/src/main/java/com/zgqf/house/
├── controller/      # REST API endpoints
├── service/         # Business logic layer
├── mapper/          # MyBatis data access layer
├── entity/          # JPA entities mapping to database tables
├── dto/             # Data transfer objects for API responses
└── config/          # Security and application configuration
```

### Key Architectural Patterns

1. **Authentication Flow**: JWT-based authentication with role-based access control (ADMIN, SELLER, BUYER). Frontend stores token in localStorage, backend validates via Spring Security filters.

1. **API Communication**: Frontend uses Axios interceptors to attach JWT tokens. Base URL configured in `frontend/src/api/request.js`.

1. **State Management**: Pinia stores manage global state (user info, house listings, contracts). Each store corresponds to a major domain entity.

1. **Database Design**: Comprehensive schema supporting house listings with geolocation, user roles, contract management with installment tracking, and favorite/bookmark functionality.

1. **Security Configuration**: CORS enabled for frontend-backend communication, BCrypt password encoding, role-based endpoint protection in `backend/config/SecurityConfig.java`.

### Development Workflow

1. **Frontend Hot Reload**: Vite provides instant HMR during development
2. **Backend Auto-restart**: Spring Boot DevTools enables automatic restart on code changes
3. **Database Migrations**: Manual SQL script execution (no automated migrations)
4. **Build Process**: Frontend builds to static assets, backend packages as WAR for deployment

### Common Development Tasks

**Adding a new API endpoint**:
1. Create controller method in appropriate controller class
2. Define DTOs if needed in `backend/dto/`
3. Implement service logic in `backend/service/`
4. Add mapper methods in `backend/mapper/` if database operations needed
5. Update frontend API service in `frontend/src/api/`

**Creating a new Vue component**:
1. Add component to `frontend/src/components/` or `frontend/src/views/`
2. Register in router if it's a page view
3. Use Pinia stores for state management
4. Follow Element Plus component patterns

**Working with the database**:
- Schema changes require manual SQL updates
- Entity classes in `backend/entity/` must match table structure
- MyBatis mappers handle all database operations
