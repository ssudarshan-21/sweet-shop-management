\# Sweet Shop Management System



A comprehensive full-stack web application for managing a sweet shop, built with Spring Boot, MySQL, and React.js following Test-Driven Development (TDD) principles.



\## Table of Contents

\- \[Project Overview](#project-overview)

\- \[Technology Stack](#technology-stack)

\- \[Features](#features)

\- \[Architecture](#architecture)

\- \[Screenshots](#screenshots)

\- \[Setup Instructions](#setup-instructions)

\- \[Testing](#testing)

\- \[API Documentation](#api-documentation)

\- \[My AI Usage](#my-ai-usage)

\- \[Project Structure](#project-structure)

\- \[Contributing](#contributing)



\## Project Overview



The Sweet Shop Management System is a modern full-stack application designed to manage sweet shop operations efficiently. It provides a comprehensive solution for inventory management, user authentication, role-based access control, and customer purchase tracking.



\### Key Highlights

\- \*\*Full-Stack Architecture\*\*: Spring Boot backend with React.js frontend

\- \*\*Secure Authentication\*\*: JWT-based authentication with refresh token rotation

\- \*\*Role-Based Access\*\*: Separate interfaces for customers and administrators  

\- \*\*Real-Time Inventory\*\*: Live stock tracking with purchase and restock capabilities

\- \*\*Responsive Design\*\*: Mobile-first UI with Material-UI components

\- \*\*TDD Approach\*\*: Comprehensive test coverage with clear Red-Green-Refactor cycles



\## Technology Stack



\### Backend

\- \*\*Framework\*\*: Spring Boot 3.2.0

\- \*\*Language\*\*: Java 17

\- \*\*Database\*\*: MySQL 8.x

\- \*\*Security\*\*: Spring Security with JWT

\- \*\*ORM\*\*: Hibernate/JPA

\- \*\*Build Tool\*\*: Maven

\- \*\*Testing\*\*: JUnit 5, Mockito, TestContainers



\### Frontend

\- \*\*Framework\*\*: React 18

\- \*\*UI Library\*\*: Material-UI (MUI)

\- \*\*State Management\*\*: Context API

\- \*\*Routing\*\*: React Router v6

\- \*\*HTTP Client\*\*: Axios

\- \*\*Testing\*\*: Jest, React Testing Library



\### DevOps \& Tools

\- \*\*Containerization\*\*: Docker \& Docker Compose

\- \*\*Database\*\*: MySQL with persistent volumes

\- \*\*Version Control\*\*: Git with conventional commits

\- \*\*Code Quality\*\*: ESLint, Prettier



\## Features



\### Customer Features

\- \*\*User Registration \& Authentication\*\*: Secure account creation and login

\- \*\*Sweet Browsing\*\*: Browse and search sweets by name, category, and price

\- \*\*Advanced Filtering\*\*: Multiple search criteria with real-time results

\- \*\*Shopping Cart\*\*: Add items to cart with quantity management

\- \*\*Direct Purchase\*\*: One-click buying with immediate inventory update

\- \*\*Order Tracking\*\*: View purchase history and spending analytics

\- \*\*Responsive Design\*\*: Optimized for desktop, tablet, and mobile



\### Admin Features

\- \*\*Inventory Management\*\*: Complete CRUD operations for sweets

\- \*\*Category Management\*\*: Organize sweets into categories

\- \*\*Stock Control\*\*: Real-time stock tracking with low-stock alerts

\- \*\*Restock Operations\*\*: Bulk inventory updates with admin authorization

\- \*\*User Management\*\*: View and manage registered users

\- \*\*Analytics Dashboard\*\*: Sales analytics and inventory insights

\- \*\*Role-Based Security\*\*: Admin-only operations with proper authorization



\### Technical Features

\- \*\*JWT Security\*\*: Access tokens (30 min) + refresh tokens (7 days)

\- \*\*Role-Based Authorization\*\*: USER and ADMIN roles with method-level security

\- \*\*Input Validation\*\*: Comprehensive validation with meaningful error messages

\- \*\*Error Handling\*\*: Global exception handling with consistent API responses

\- \*\*Database Relationships\*\*: Properly normalized schema with foreign keys

\- \*\*Transaction Management\*\*: ACID compliance with @Transactional annotations



\## Architecture



\### System Architecture

```

┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐

│   React.js      │    │   Spring Boot   │    │     MySQL       │

│   Frontend      │◄──►│    Backend      │◄──►│    Database     │

│   (Port 3000)   │    │   (Port 8080)   │    │   (Port 3306)   │

└─────────────────┘    └─────────────────┘    └─────────────────┘

```



\### Backend Architecture

```

┌─────────────────────────────────────────────────────────────┐

│                        Spring Boot Application              │

├─────────────────────────────────────────────────────────────┤

│  Controllers (REST API)                                     │

│  ├── AuthController      ├── SweetController                │

│  ├── UserController      └── CategoryController             │

├─────────────────────────────────────────────────────────────┤

│  Services (Business Logic)                                  │

│  ├── UserService         ├── SweetService                   │

│  ├── AuthService         └── CategoryService                │

├─────────────────────────────────────────────────────────────┤

│  Repositories (Data Access)                                 │

│  ├── UserRepository      ├── SweetRepository                │

│  ├── OrderRepository     └── CategoryRepository             │

├─────────────────────────────────────────────────────────────┤

│  Entities (Domain Models)                                   │

│  ├── User                ├── Sweet                          │

│  ├── Role                ├── Category                       │

│  ├── Order               └── RefreshToken                   │

└─────────────────────────────────────────────────────────────┘

```



\### Database Schema

```

Users ──┐     ┌── Categories

&nbsp;       │     │

&nbsp;       ├── Orders ── Sweets

&nbsp;       │

&nbsp;       └── RefreshTokens



User-Roles (Many-to-Many)

```





\## Setup Instructions



\### Prerequisites

\- Java 17 or higher

\- Node.js 18+ and npm

\- MySQL 8.x

\- Docker \& Docker Compose (optional)

\- Git



\### Option 1: Docker Setup (Recommended)



1\. \*\*Clone the repository\*\*

&nbsp;  ```bash

&nbsp;  git clone https://github.com/yourusername/sweet-shop-management.git

&nbsp;  cd sweet-shop-management

&nbsp;  ```



2\. \*\*Start all services\*\*

&nbsp;  ```bash

&nbsp;  docker-compose up --build

&nbsp;  ```



3\. \*\*Access the application\*\*

&nbsp;  - Frontend: http://localhost:3000

&nbsp;  - Backend API: http://localhost:8080

&nbsp;  - MySQL: localhost:3306



4\. \*\*Default credentials\*\*

&nbsp;  - Admin: `admin@sweetshop.com` / `admin123`



\### Option 2: Manual Setup



\#### Backend Setup



1\. \*\*Navigate to backend directory\*\*

&nbsp;  ```bash

&nbsp;  cd backend

&nbsp;  ```



2\. \*\*Configure MySQL database\*\*

&nbsp;  ```sql

&nbsp;  CREATE DATABASE sweetshop;

&nbsp;  CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';

&nbsp;  GRANT ALL PRIVILEGES ON sweetshop.\* TO 'root'@'localhost';

&nbsp;  FLUSH PRIVILEGES;

&nbsp;  ```



3\. \*\*Update application.properties\*\* (if needed)

&nbsp;  ```properties

&nbsp;  spring.datasource.url=jdbc:mysql://localhost:3306/sweetshop

&nbsp;  spring.datasource.username=root

&nbsp;  spring.datasource.password=root

&nbsp;  server.port=8080

&nbsp;  ```



4\. \*\*Build and run the backend\*\*

&nbsp;  ```bash

&nbsp;  mvn clean install

&nbsp;  mvn spring-boot:run

&nbsp;  ```



5\. \*\*Verify backend is running\*\*

&nbsp;  ```bash

&nbsp;  curl http://localhost:8080/actuator/health

&nbsp;  # Should return: {"status":"UP"}

&nbsp;  ```



\#### Frontend Setup



1\. \*\*Navigate to frontend directory\*\*

&nbsp;  ```bash

&nbsp;  cd frontend

&nbsp;  ```



2\. \*\*Install dependencies\*\*

&nbsp;  ```bash

&nbsp;  npm install

&nbsp;  ```



3\. \*\*Start the development server\*\*

&nbsp;  ```bash

&nbsp;  npm start

&nbsp;  ```



4\. \*\*Access the application\*\*

&nbsp;  - Open http://localhost:3000 in your browser

&nbsp;  - Login with admin credentials or register a new account



\## Testing



\### Backend Testing



The project includes comprehensive test coverage with different types of tests:



\#### Running Tests

```bash

cd backend



\# Run all tests

mvn test



\# Run only unit tests

mvn test -Dtest="\*\*/\*Test"



\# Run only integration tests

mvn test -Dtest="\*\*/\*IntegrationTest"



\# Generate coverage report

mvn clean test jacoco:report

```



\#### Test Coverage Report

After running tests with Jacoco, open: `backend/target/site/jacoco/index.html`



\*\*\[ADD SCREENSHOT HERE: Test coverage report showing high coverage percentages]\*\*



\#### Test Categories



1\. \*\*Unit Tests\*\* (90%+ coverage)

&nbsp;  - Service layer tests with Mockito

&nbsp;  - Entity validation tests

&nbsp;  - Utility class tests



2\. \*\*Integration Tests\*\* (using TestContainers)

&nbsp;  - Repository layer with real database

&nbsp;  - Controller integration tests

&nbsp;  - Security configuration tests



3\. \*\*TDD Examples\*\*

&nbsp;  ```bash

&nbsp;  # Example of TDD commit pattern in git history:

&nbsp;  - test: Add failing test for user registration (RED)

&nbsp;  - feat: Implement user registration logic (GREEN)  

&nbsp;  - refactor: Extract validation logic (REFACTOR)

&nbsp;  ```



\### Frontend Testing



```bash

cd frontend



\# Run all tests

npm test



\# Run tests with coverage

npm test -- --coverage --watchAll=false



\# Run tests in CI mode

npm run test:ci

```



\#### Frontend Test Types

\- Component unit tests with React Testing Library

\- Integration tests for user flows

\- Context provider tests

\- Custom hook tests



\## API Documentation



\### Authentication Endpoints

```http

POST   /api/auth/register     # User registration

POST   /api/auth/login        # User login  

POST   /api/auth/refresh      # Refresh access token

POST   /api/auth/logout       # User logout

```



\### Sweet Management

```http

GET    /api/sweets            # List all available sweets

GET    /api/sweets/search     # Search sweets with filters

GET    /api/sweets/{id}       # Get specific sweet

POST   /api/sweets            # Create sweet (Admin only)

PUT    /api/sweets/{id}       # Update sweet (Admin only)

DELETE /api/sweets/{id}       # Delete sweet (Admin only)

POST   /api/sweets/{id}/purchase  # Purchase sweet

POST   /api/sweets/{id}/restock   # Restock sweet (Admin only)

```



\### Category Management

```http

GET    /api/categories        # List all categories

POST   /api/categories        # Create category (Admin only)

PUT    /api/categories/{id}   # Update category (Admin only)

DELETE /api/categories/{id}   # Delete category (Admin only)

```



\### User Management

```http

GET    /api/users/profile     # Get current user profile

GET    /api/users             # List all users (Admin only)

GET    /api/users/orders      # Get user's order history

```



\### Example API Responses



\#### Success Response

```json

{

&nbsp; "success": true,

&nbsp; "message": "Sweet retrieved successfully",

&nbsp; "data": {

&nbsp;   "id": 1,

&nbsp;   "name": "Gulab Jamun",

&nbsp;   "description": "Soft, syrupy sweet balls",

&nbsp;   "price": 90.00,

&nbsp;   "quantity": 75,

&nbsp;   "category": {

&nbsp;     "id": 2,

&nbsp;     "name": "Traditional"

&nbsp;   }

&nbsp; },

&nbsp; "timestamp": "2025-01-20T10:30:00"

}

```



\#### Error Response

```json

{

&nbsp; "success": false,

&nbsp; "message": "Sweet not found with id: 999",

&nbsp; "data": null,

&nbsp; "timestamp": "2025-01-20T10:30:00"

}

```



\## My AI Usage



\### AI Tools Used

I extensively used \*\*Claude 3.5 Sonnet\*\* (by Anthropic) as my primary AI assistant throughout this project development. Claude was instrumental in accelerating development while maintaining high code quality and following best practices.



\### How AI Assisted My Development Workflow



\#### 1. Architecture and Design (25% of development time)

\*\*How I used AI:\*\*

\- \*\*System Architecture\*\*: I asked Claude to help design the overall system architecture, including the separation of concerns between frontend and backend, database schema design, and API structure.

\- \*\*Database Schema\*\*: Claude assisted in creating a normalized database schema with proper relationships, constraints, and indexing strategies.

\- \*\*Security Design\*\*: I consulted Claude for JWT implementation patterns, refresh token rotation strategies, and role-based authorization design.



\*\*Specific Example:\*\*

```

Me: "How should I structure a Sweet Shop management system with Spring Boot and React?"

Claude: \[Provided detailed architecture with layers, security considerations, and best practices]

```



\*\*Impact:\*\* This saved me approximately 8-10 hours of research and architecture planning, allowing me to start with a solid foundation.



\#### 2. Backend Development with TDD (40% of development time)

\*\*How I used AI:\*\*

\- \*\*Entity Design\*\*: Claude generated initial JPA entity classes with proper annotations, validation, and relationships.

\- \*\*Repository Queries\*\*: I asked Claude to help write complex Spring Data JPA queries for inventory management and user operations.

\- \*\*Service Layer Logic\*\*: Claude assisted in implementing business logic with proper exception handling and transaction management.

\- \*\*Test Generation\*\*: Most importantly, Claude helped me write comprehensive unit and integration tests following TDD principles.



\*\*TDD Workflow with AI:\*\*

1\. \*\*RED Phase\*\*: I described the functionality needed, and Claude generated failing tests

2\. \*\*GREEN Phase\*\*: Claude helped implement the minimal code to make tests pass

3\. \*\*REFACTOR Phase\*\*: I asked Claude to suggest improvements while keeping tests green



\*\*Specific Example:\*\*

```java

// I asked Claude: "Generate a failing test for user registration with duplicate email validation"

@Test

void shouldThrowExceptionWhenEmailAlreadyExists() {

&nbsp;   // Given - Claude generated this test structure

&nbsp;   UserRegistrationDto registrationDto = new UserRegistrationDto("existing@example.com", "password123", "John", "Doe");

&nbsp;   when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);



&nbsp;   // When \& Then - Claude provided proper exception testing

&nbsp;   assertThrows(UserAlreadyExistsException.class, () -> {

&nbsp;       userService.registerUser(registrationDto);

&nbsp;   });

}

```



\*\*Impact:\*\* AI helped me achieve 90%+ test coverage and maintain true TDD practices, which would have been much more time-consuming to do manually.



\#### 3. Frontend Development (30% of development time)

\*\*How I used AI:\*\*

\- \*\*Component Architecture\*\*: Claude helped structure React components with proper separation of concerns and reusability.

\- \*\*Material-UI Integration\*\*: I asked Claude to create responsive, accessible UI components using Material-UI best practices.

\- \*\*State Management\*\*: Claude assisted in implementing Context API for global state management (Auth, Cart).

\- \*\*API Integration\*\*: Claude helped create proper error handling and loading states for API calls.



\*\*Specific Example:\*\*

```javascript

// I requested: "Create a sweet card component with purchase functionality"

// Claude provided the complete component with proper state management, error handling, and responsive design

const SweetCard = ({ sweet, onPurchase }) => {

&nbsp;   // Claude generated comprehensive component with all functionality

};

```



\*\*Impact:\*\* The AI assistance resulted in a polished, responsive frontend that follows React best practices and provides excellent user experience.



\#### 4. Integration and DevOps (15% of development time)

\*\*How I used AI:\*\*

\- \*\*Docker Configuration\*\*: Claude created Docker and docker-compose files for easy development setup.

\- \*\*Security Configuration\*\*: I asked Claude to implement comprehensive Spring Security configuration with JWT.

\- \*\*Error Handling\*\*: Claude helped create a global exception handler with consistent API responses.

\- \*\*Documentation\*\*: Claude assisted in creating comprehensive API documentation and setup instructions.



\### AI's Impact on My Workflow



\#### Positive Impacts:

1\. \*\*Development Speed\*\*: AI accelerated my development by approximately 3-4x, especially for boilerplate code and test generation.

2\. \*\*Code Quality\*\*: Claude consistently suggested best practices, proper naming conventions, and design patterns I might have missed.

3\. \*\*Learning Acceleration\*\*: Through AI explanations, I learned advanced Spring Boot features, React patterns, and testing strategies much faster than traditional documentation study.

4\. \*\*Comprehensive Testing\*\*: AI helped me achieve much higher test coverage than I typically would have time to write manually.

5\. \*\*Documentation\*\*: Claude generated thorough documentation, which improved the project's professionalism and maintainability.



\#### Areas Where I Added Manual Refinement:

1\. \*\*Business Logic Validation\*\*: I carefully reviewed all business rules and validation logic generated by AI to ensure they met specific requirements.

2\. \*\*Security Review\*\*: While AI provided security implementations, I manually verified JWT configuration, password hashing, and authorization rules.

3\. \*\*UI/UX Polish\*\*: I fine-tuned the Material-UI theme, color schemes, and user interaction flows based on my vision for the sweet shop aesthetic.

4\. \*\*Database Optimization\*\*: I reviewed and optimized the database queries and indexing strategies suggested by AI.

5\. \*\*Integration Testing\*\*: I manually tested all user flows and edge cases to ensure the AI-generated components worked together seamlessly.



\### Specific AI Contributions:



\#### Code Generation (60% AI-assisted):

\- Initial entity, repository, and service class structures

\- Comprehensive test suites with proper mocking

\- React components with Material-UI styling

\- Security configuration and JWT utilities

\- Global exception handling and validation



\#### Problem Solving (40% AI-assisted):

\- Debugging complex Spring Security issues

\- Resolving CORS and authentication problems

\- Optimizing database queries and relationships

\- Implementing proper error handling patterns



\#### Documentation (80% AI-assisted):

\- API documentation with examples

\- Setup and installation instructions

\- Code comments and inline documentation

\- This comprehensive README file structure



\### Reflection on AI Impact:



\*\*Most Valuable AI Contributions:\*\*

1\. \*\*TDD Implementation\*\*: Claude's ability to generate comprehensive tests first, then implement code to pass those tests, was invaluable for maintaining true TDD practices.

2\. \*\*Best Practices Enforcement\*\*: AI consistently applied industry best practices that I might have overlooked under time pressure.

3\. \*\*Comprehensive Error Handling\*\*: Claude generated robust error handling that I often skip in personal projects but is crucial for production applications.



\*\*Areas for Future Improvement:\*\*

1\. \*\*Business Logic Creativity\*\*: While AI excels at technical implementation, the creative aspects of business logic and user experience design still benefit from human insight.

2\. \*\*Performance Optimization\*\*: AI suggestions were solid but didn't always consider advanced performance optimization techniques.

3\. \*\*Industry-Specific Requirements\*\*: AI provided generic best practices, but industry-specific compliance and requirements needed manual consideration.



\### Transparency and Learning:



This project represents a collaboration between human creativity and AI efficiency. While AI significantly accelerated development, every line of code was reviewed, understood, and often modified to meet specific requirements. The AI served as an intelligent pair programming partner, allowing me to focus on architecture, business logic, and user experience while handling much of the repetitive coding tasks.



The experience demonstrated that AI is most effective when used as a tool to augment human capabilities rather than replace human judgment. The combination resulted in a higher-quality codebase delivered in significantly less time than traditional development approaches.



\## Project Structure



```

sweet-shop-management/

├── backend/                    # Spring Boot Application

│   ├── src/main/java/

│   │   └── com/sweetshop/

│   │       ├── config/         # Configuration classes

│   │       ├── controller/     # REST Controllers

│   │       ├── dto/           # Data Transfer Objects

│   │       ├── entity/        # JPA Entities

│   │       ├── exception/     # Custom Exceptions

│   │       ├── repository/    # Data Access Layer

│   │       ├── security/      # Security Configuration

│   │       ├── service/       # Business Logic

│   │       └── util/          # Utility Classes

│   ├── src/test/java/         # Test Classes

│   ├── pom.xml               # Maven Dependencies

│   └── Dockerfile           # Backend Container

├── frontend/                  # React Application

│   ├── public/               # Static Assets

│   ├── src/

│   │   ├── components/      # Reusable Components

│   │   │   ├── Auth/       # Authentication Components

│   │   │   ├── Layout/     # Layout Components

│   │   │   └── Sweet/      # Sweet-related Components

│   │   ├── contexts/       # React Contexts

│   │   ├── pages/          # Page Components

│   │   ├── theme/          # Material-UI Theme

│   │   └── App.js         # Main Application

│   ├── package.json        # NPM Dependencies

│   └── Dockerfile         # Frontend Container

├── docker-compose.yml       # Multi-container Setup

├── .gitignore              # Git Ignore Rules

└── README.md              # Project Documentation

```



\## Contributing



\### Development Workflow

1\. Fork the repository

2\. Create a feature branch (`git checkout -b feat/amazing-feature`)

3\. Follow TDD principles - write tests first

4\. Commit changes with conventional commit messages

5\. Include AI co-authorship when applicable

6\. Push to branch and create Pull Request



\### Commit Convention

```bash

\# Format: type(scope): description

feat(backend): add user authentication with JWT

fix(frontend): resolve cart quantity update issue

test(service): add comprehensive user service tests

docs(readme): update API documentation



\# With AI co-authorship:

git commit -m "feat: implement sweet inventory management



Added comprehensive CRUD operations for sweet management

with proper validation and error handling.





Co-authored-by: Claude AI <ai@anthropic.com>"

```



\### Code Quality Standards

\- Follow existing code style and conventions

\- Maintain test coverage above 80%

\- Write meaningful commit messages

\- Update documentation for new features

\- Ensure all tests pass before submitting PR



---



\*\*Built with modern development practices, comprehensive testing, and AI-assisted development for optimal productivity and code quality.\*\*

