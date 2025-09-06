Real-Time Queue Management System
This is a full-stack, real-time queue management system built with Java Spring Boot and vanilla HTML/CSS/JavaScript. It allows users to dynamically create custom queues, share joining links, and manage the queue from a dedicated admin panel.

Key Features
Dynamic Queue Creation: Create queues on the fly with custom names and user information fields.

Real-Time Updates: Uses WebSockets (with STOMP) for instant updates on all connected clients.

Admin Dashboard: A separate view for administrators to see the waiting list and call the next person.

QR Code Generation: Automatically generates a QR code for visitors to easily scan and join the queue.

RESTful API: A versioned, well-structured API for all backend operations.

Technology Stack
Backend: Java 17, Spring Boot 3

Database: MySQL

Real-Time: Spring WebSockets, STOMP

Frontend: HTML5, Tailwind CSS, JavaScript

API Documentation: Springdoc OpenAPI (Swagger UI)

Deployment: Docker (Ready), Render (Recommended)

How to Run Locally
Clone the repository:

git clone https://github.com/shivam-616/Queuing_Managment.git

Set up your database:

Make sure you have MySQL installed and running.

Create a database named entry_db.

Update the database credentials in src/main/resources/application.properties.

Run the application:

You can run the QueueManagementSystemApplication.java file from your IDE or use Maven:

mvn spring-boot:run

Access the application:

Open your browser and go to http://localhost:8080.

API Documentation
Once the application is running, you can view the interactive API documentation (Swagger UI) at:
http://localhost:8080/swagger-ui.html
