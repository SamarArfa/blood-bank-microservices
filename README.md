Blood Bank Management System Using Spring Boot MicroservicesAdd commentMore actions

An integrated system for managing blood donors and inventory using Spring Boot and Spring Cloud.

Services

- Eureka Server: For discovering services.
- Config Server: For storing settings for all services.
- API Gateway: A single gateway to the system.
- Donor Service: For managing donor data.
- Inventory Service: For tracking blood units.
- Request Service: For searching for donors when needed.

System Startup

1. Start the services in the following sequence:
- discovery-server
- config-server
- api-gateway
- donor-service
- inventory-service
- request-service

2. Use Postman to test the APIs:
- POST http://localhost:8080/BloodBankApi/donors
- GET http://localhost:8080/BloodBankApi/request/potential-donors?bloodType=B_NEG

Supporting Files

- pom.xml for each service
- application.yml for each service
- bootstrap.yml (for services using Config Server)
- Postman Collection← To be added later
