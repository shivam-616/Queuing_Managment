# Real-Time Queue Management System

A full-stack, real-time queue management system built with Java Spring Boot and vanilla HTML/CSS/JavaScript. The system enables dynamic queue creation, real-time updates via WebSockets, and comprehensive admin controls.

## Features

- **Dynamic Queue Creation**: Create custom queues with configurable user information fields
- **Real-Time Updates**: WebSocket integration with STOMP for instant client synchronization
- **Admin Dashboard**: Dedicated panel for queue management and customer calling
- **QR Code Generation**: Automatic QR codes for contactless queue joining
- **RESTful API**: Versioned API with comprehensive documentation

## Technology Stack

| Component | Technology |
|-----------|------------|
| Backend | Java 17, Spring Boot 3 |
| Database | MySQL |
| Real-Time | Spring WebSockets, STOMP |
| Frontend | HTML5, Tailwind CSS, JavaScript |
| Documentation | Springdoc OpenAPI (Swagger UI) |
| Deployment | Docker, Render |

## Installation

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Setup

1. **Clone Repository**
   ```bash
   git clone https://github.com/shivam-616/Queuing_Managment.git
   cd Queuing_Managment
   ```

2. **Database Configuration**
   ```sql
   CREATE DATABASE entry_db;
   ```
   Update credentials in `src/main/resources/application.properties`

3. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access Points**
   - Application: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

## Docker Deployment

```bash
docker build -t queue-management-system .
docker run -p 8080:8080 queue-management-system
```

## API Documentation

Interactive API documentation is available at `/swagger-ui.html` when the application is running.

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/NewFeature`)
3. Commit changes (`git commit -m 'Add NewFeature'`)
4. Push to branch (`git push origin feature/NewFeature`)
5. Create Pull Request

## Author

**[Shivam](https://github.com/shivam-616)**
