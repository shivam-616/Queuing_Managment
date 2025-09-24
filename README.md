# 🚀 Real-Time Queue Management System

A full-stack, real-time queue management system built with Java Spring Boot, WebSockets, and a modern frontend. This application allows for the dynamic creation of custom queues, provides a real-time admin dashboard, and generates QR codes for easy, contactless user registration.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

## ✨ Live Demo & Features

**Live Application URL:** `https://[YOUR_CLOUD_RUN_URL_HERE]` *(Replace with your actual Cloud Run URL)*

---

### Key Features:

* **Dynamic Queue Creation**: Instantly create new queues with custom information fields (e.g., Name, Phone, Appointment ID) directly from the UI.
* **Real-Time Admin Dashboard**: A dedicated admin panel to monitor the queue, see who is waiting, and call the next person in line with a single click.
* **Live User View**: Users see their position in the queue update in real-time without needing to refresh the page, thanks to **Spring WebSockets** and **STOMP**.
* **QR Code Generation**: Automatically generates a unique QR code for each queue, allowing users to join instantly by scanning it with their mobile device.
* **RESTful API with Swagger UI**: A fully documented API for programmatic interaction, with a beautiful Swagger UI interface.
* **Cloud-Native & Ready for Deployment**: Easily deployable as a Docker container and configured for cloud platforms like Google Cloud Run.

## 🛠️ Technology Stack

This project leverages a modern, robust technology stack for a full-featured web application experience.

| Component      | Technology                                                              |
| -------------- | ----------------------------------------------------------------------- |
| **Backend** | Java 17, Spring Boot 3                                                  |
| **Database** | MySQL                                                                   |
| **Real-Time** | Spring WebSockets, STOMP                                                |
| **Frontend** | HTML5, Tailwind CSS, Vanilla JavaScript                                 |
| **API Docs** | Springdoc OpenAPI (Swagger UI)                                          |
| **Deployment** | Docker, Google Cloud Run, GitHub Actions (CI/CD)                        |

## 🚀 Getting Started

You can run this project locally for development and testing.

### Prerequisites

* Java 17+
* Apache Maven 3.6+
* Docker and Docker Compose (Recommended)

### Local Development with Docker (Recommended)

This is the easiest way to get started, as it handles the database setup for you.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/shivam-616/Queuing_Managment.git](https://github.com/shivam-616/Queuing_Managment.git)
    cd Queuing_Managment
    ```
2.  **Run with Docker Compose:**
    This single command will build the application image and start both the application and the MySQL database containers.
    ```bash
    docker-compose up --build
    ```
3.  **Access the application:**
    * **Application:** [http://localhost:8080](http://localhost:8080)
    * **API Documentation:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## ☁️ Cloud Deployment with Google Cloud & GitHub Actions

This project is configured for continuous deployment to **Google Cloud Run** using **GitHub Actions**.

1.  **Push to `master`**: Every push to the `master` branch will automatically trigger the GitHub Actions workflow.
2.  **CI/CD Pipeline**: The workflow will:
    * Build the Docker image.
    * Push the image to Google Artifact Registry.
    * Deploy the new image as a revision to the Cloud Run service.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/shivam-616/Queuing_Managment/issues).

1.  Fork the repository.
2.  Create your feature branch (`git checkout -b feature/NewFeature`).
3.  Commit your changes (`git commit -m 'Add some NewFeature'`).
4.  Push to the branch (`git push origin feature/NewFeature`).
5.  Open a Pull Request.

## 👤 Author

**Shivam**

* GitHub: [@shivam-616](https://github.com/shivam-616)
