# EasyVPS
The application for a customisable VPS rental shop called EasyVPS.
The **user** will be able to choose already prepared **configuration** for his VPS **instance**.

The relational database chosen is **PostgreSQL**.
## Schema
![DBschema](DatabaseSchema.png)
## Business operation
Before adding a user's configuration to the server, 
it is necessary to check the server's resource availability 
(a single server can host multiple instances belonging to the same or different users).
If the server is already overloaded, the instance should be assigned to a different server.
It is also necessary to ensure that user instances have different IPs.
## Application starting:
Application is divided into two parts<br>
• **API**: A Spring Boot application responsible for handling the server-side logic, database operations, and REST API endpoints.<br>
• **Client**: A front-end application that interacts with the backend to provide the user interface.
### API (backend) setup
1. Ensure a compatible database is installed and running. You can
update the application.properties file in the src/main/resources directory with your database details 
2. In directory EasyVPS_backend run the Backend with ./gradlew bootRun. Ensure a compatible database is installed and running.
The backend will run on port 8080 by default.
### Client (frontend) setup
1. Ensure the Backend is Running.
2. In directory EasyVPS_client run ./gradlew bootRun. The frontend will run on port 8081 by default.