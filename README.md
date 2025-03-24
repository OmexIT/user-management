# Spring Boot Identity Management System

A comprehensive identity management solution for Spring Boot applications with Keycloak integration, supporting multiple user types and dynamic role-based access control.

## Features

- **Multi-User Architecture**: Support for Customers, Merchants, and Portal/System administrators
- **Keycloak Integration**: Authentication and user management through Keycloak
- **Dynamic Role Management**: Flexible role configuration with permission-based access control
- **Local User Synchronization**: Maintains local user records for domain logic and auditing
- **Spring Data JDBC**: Uses Spring Data JDBC with proper aggregate patterns
- **Database Migrations**: Liquibase for database schema management
- **Clean Architecture**: SOLID principles with proper separation of concerns
- **DTOs and Mappers**: Clean data transfer between layers using immutable DTOs

## Technical Stack

- **Spring Boot 3.4.3** with Spring Web, Spring Data JDBC, and Spring Security
- **Java 17+**
- **Keycloak 26.0.4** for authentication and user management
- **PostgreSQL** for persistence
- **Liquibase** for database migrations
- **Lombok** for reducing boilerplate code
- **Java Records** for immutable DTOs
- **MapStruct** for automatic object mapping

## Design Patterns Used

- **Builder Pattern**: For creating complex objects (used in DTOs)
- **Adapter Pattern**: For mapping between entities and DTOs (implemented with MapStruct)
- **Repository Pattern**: For data access abstraction
- **Factory Method Pattern**: For creating objects without specifying concrete classes
- **Dependency Injection**: For loose coupling between components
- **Strategy Pattern**: For dynamic permission evaluation
- **Code Generation Pattern**: Using MapStruct for automatic, type-safe object mapping

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── usermanagement/
│   │               ├── config/           # Configuration classes
│   │               ├── controller/       # REST controllers
│   │               ├── dto/              # Data Transfer Objects
│   │               ├── exception/        # Custom exceptions
│   │               ├── mapper/           # Entity-DTO mappers
│   │               │   └── impl/         # Mapper implementations
│   │               ├── model/            # Domain models
│   │               │   └── enums/        # Enumerations
│   │               ├── repository/       # Spring Data repositories
│   │               ├── security/         # Security components
│   │               └── service/          # Business logic services
│   └── resources/
│       ├── application.yml               # Application configuration
│       └── db/
│           └── changelog/                # Liquibase changelogs
│               └── sql/                  # SQL migration files
```

## SOLID Principles Implementation

- **Single Responsibility Principle**: Each class has one responsibility (e.g., mappers only handle mapping)
- **Open/Closed Principle**: Classes are open for extension but closed for modification (e.g., using interfaces)
- **Liskov Substitution Principle**: Subtypes can be substituted for their base types
- **Interface Segregation Principle**: Clients only depend on methods they use (e.g., specific mapper interfaces)
- **Dependency Inversion Principle**: High-level modules depend on abstractions (e.g., services depend on repository interfaces)

## Setup Instructions

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Keycloak server (version 26.0.4 or compatible)

### Database Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE asset_finance;
   CREATE USER app_user WITH PASSWORD 'pa55P0rt';
   GRANT ALL PRIVILEGES ON DATABASE asset_finance TO app_user;
   ```

### Keycloak Setup

1. Install and start Keycloak server
2. Create a new realm: `identity-management-app`
3. Create client for the application: `identity-management-client`
   - Access Type: confidential
   - Service Accounts Enabled: true
   - Valid Redirect URIs: http://localhost:8080/*
4. Create client for admin operations: `identity-management-admin`
   - Access Type: confidential
   - Service Accounts Enabled: true
   - Service Account Roles: manage-users, manage-realm, view-users
5. Create base roles:
   - CUSTOMER
   - MERCHANT
   - ADMIN
6. Create protocol mappers for the clients:
   - Add mapper for groups
   - Add mapper for user_type attribute
   - Add mapper for merchant_id attribute
   - Add mapper for customer_id attribute

### Application Configuration

Update `application.yml` with your specific configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/asset_finance
    username: app_user
    password: pa55P0rt

keycloak:
  auth-server-url: http://localhost:8180/auth
  realm: identity-management-app
  resource: identity-management-client
  public-client: false
  bearer-only: true
  admin-client:
    id: identity-management-admin
    secret: your-client-secret
```

### Running the Application

```bash
./mvnw spring-boot:run
```

## API Endpoints

### User Management

- `GET /api/me` - Get current user information
- `GET /api/admin/users/{externalId}` - Get user by external ID (admin only)
- `GET /api/permissions/check?permission=PERMISSION_NAME` - Check if user has a specific permission

### Role Management

- `GET /api/admin/roles` - Get all roles (admin only)
- `GET /api/admin/roles/{roleName}` - Get a specific role (admin only)
- `POST /api/admin/roles` - Create a new role (admin only)
- `GET /api/admin/roles/{roleName}/permissions` - Get permissions for a role (admin only)
- `POST /api/admin/roles/{roleName}/permissions` - Add permissions to a role (admin only)

### User Type Specific Endpoints

- `GET /api/customers/dashboard` - Customer dashboard (customer only)
- `GET /api/merchants/dashboard` - Merchant dashboard (merchant only)
- `GET /api/admin/dashboard` - Admin dashboard (admin only)

## Permission Model

Permissions follow the format: `{operation}:{resource}[:{scope}]`

Examples:
- `READ:CUSTOMER:ALL` - Read all customers
- `UPDATE:MERCHANT:OWN` - Update own merchant
- `CREATE:ORDER:MERCHANT_123` - Create order for merchant 123

## Security Configuration

The application uses JWT tokens from Keycloak with custom permission evaluation:

1. JWT tokens contain user roles
2. Roles are mapped to permissions
3. Permissions are evaluated for access control
4. Method-level security with `@PreAuthorize` annotations

## User Synchronization

Users are synchronized between Keycloak and the local database:

1. User authenticates with Keycloak
2. JWT token is validated by the application
3. User information is extracted from the token
4. User is created or updated in the local database
5. User context is established for the request

## Dynamic Role Management

Roles can be dynamically created and managed:

1. Admin creates a role in Keycloak
2. Admin assigns permissions to the role
3. Permissions are stored as role attributes
4. Users with the role inherit its permissions
5. Permissions are evaluated for access control

## License

This project is licensed under the MIT License - see the LICENSE file for details.
