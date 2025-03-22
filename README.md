<p align="center">
      <img src="https://imgur.com/W1a0Y87.jpg" alt="TruethLine" width="240">
</p>

# HealthFlow API

HealthFlow is a RESTful API for managing medical records, prescriptions, doctors, patients, hospital rooms, and appointments. It is built using **Spring Boot** and uses an **H2 database** for storage.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Installation and Setup](#installation-and-setup)
- [Database Configuration](#database-configuration)
- [Running the Project](#running-the-project)
- [API Endpoints](#api-endpoints)
- [License](#license)

## Technologies Used

<p align="center">

  <img src="https://camo.githubusercontent.com/6d9ad4becc2d73ac5cefacc1370a6c37458f272a553046ea5e2b8351ea185747/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6a6176612d2532334544384230302e7376673f7374796c653d666f722d7468652d6261646765266c6f676f3d6a617661266c6f676f436f6c6f723d7768697465">
  <img src="https://camo.githubusercontent.com/84e0999fa027dedfb31a169d54da33fd98f9691c0b3aba4687a0e0a64cede44d/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7973716c2d2532333030662e7376673f7374796c653d666f722d7468652d6261646765266c6f676f3d6d7973716c266c6f676f436f6c6f723d7768697465" alt="SQL" width="90" />
  <img src="https://camo.githubusercontent.com/cf06fedcca8eedc2ebcf41a87c79ae200b8e7f79b65a9c2dcd833d1990bd3290/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f506f73746d616e2d4646364333373f7374796c653d666f722d7468652d6261646765266c6f676f3d706f73746d616e266c6f676f436f6c6f723d7768697465" alt="Postman" width="110">
  <img src="https://camo.githubusercontent.com/579cca9d03e324c90d59af069554195682c0f3b67f61cd401efeaa3c0ae3974b/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f2d537761676765722d253233436c6f6a7572653f7374796c653d666f722d7468652d6261646765266c6f676f3d73776167676572266c6f676f436f6c6f723d7768697465" alt="Swagger" width="110">

</p>

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Swagger (for API documentation)**

## Installation and Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/fabrixindex/Rest-api-spring-hospital.git
   cd healthflow
   ```

2. Install dependencies using Maven:
   ```sh
   mvn clean install
   ```

3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Database Configuration
The project uses an in-memory **H2 database**. The default configuration is located in `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:healthflow
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update

springdoc.version=2.8.5
springdoc.swagger-ui.path=/swagger-ui.html
```

You can access the H2 console at:
```
http://localhost:8080/h2-console
```
Use the following JDBC URL to connect:
```
jdbc:h2:mem:healthflow
```

## Running the Project
To start the application, simply run:
```sh
mvn spring-boot:run
```
By default, the API will be available at:
```
http://localhost:8080
```

## API Endpoints

### Patients (`/patients`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/patients` | Get all patients |
| GET    | `/patients/{id}` | Get a patient by ID |
| POST   | `/patients` | Create a new patient |
| PUT    | `/patients/{id}` | Update a patient |
| DELETE | `/patients/{id}` | Delete a patient |

### Doctors (`/doctors`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/doctors` | Get all doctors |
| GET    | `/doctors/{id}` | Get a doctor by ID |
| POST   | `/doctors` | Create a new doctor |
| PUT    | `/doctors/{id}` | Update a doctor |
| DELETE | `/doctors/{id}` | Delete a doctor |

### Appointments (`/appointments`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/appointments` | Get all appointments |
| GET    | `/appointments/{id}` | Get an appointment by ID |
| POST   | `/appointments` | Create a new appointment |
| PUT    | `/appointments/{id}` | Update an appointment |
| DELETE | `/appointments/{id}` | Delete an appointment |

### Medical Records (`/medicalRecords`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/medicalRecords` | Get all medical records |
| GET    | `/medicalRecords/{id}` | Get a medical record by ID |
| POST   | `/medicalRecords` | Create a new medical record |
| PUT    | `/medicalRecords/{id}` | Update a medical record |
| DELETE | `/medicalRecords/{id}` | Delete a medical record |

### Prescriptions (`/prescriptions`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/prescriptions` | Get all prescriptions |
| GET    | `/prescriptions/{id}` | Get a prescription by ID |
| POST   | `/prescriptions` | Create a new prescription |
| PUT    | `/prescriptions/{id}` | Update a prescription |
| DELETE | `/prescriptions/{id}` | Delete a prescription |

### Hospital Rooms (`/hospitalRooms`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/hospitalRooms` | Get all hospital rooms |
| GET    | `/hospitalRooms/{id}` | Get a hospital room by ID |
| POST   | `/hospitalRooms` | Create a new hospital room |
| PUT    | `/hospitalRooms/{id}` | Update a hospital room |
| DELETE | `/hospitalRooms/{id}` | Delete a hospital room |

### Medications (`/medications`)
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET    | `/medications` | Get all medications |
| GET    | `/medications/{id}` | Get a medication by ID |
| POST   | `/medications` | Create a new medication |
| PUT    | `/medications/{id}` | Update a medication |
| DELETE | `/medications/{id}` | Delete a medication |

---

### Notes
- The API documentation can be accessed via Swagger:
```
http://localhost:8080/swagger-ui.html
```
- Ensure that **Lombok** is enabled in your IDE for better experience.

### Contact

📫 How to reach me [fabriciopapetti1121@gmail.com](mailto:fabriciopapetti1121@gmail.com)

🚀 Enjoy using HealthFlow API! If you have any questions, feel free to ask. 😊
