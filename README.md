# Bank of Words REST API

## Introduction

This guide will help you initialize and start the project on your local machine. The project is built using the Spring framework and uses a MySQL database.

## Installation and Setup
1. Run the `wordnet20-from-prolog-all-3.sql` file first located in the `src/main/resources/db/` directory. This file might take some time to run.
2. After the `wordnet20-from-prolog-all-3.sql` file has finished running, run the `creation.sql` file. 
3. Plug in your database username and password in the `application.properties` file located in the `src/main/resources/` directory.
4. You can now start the project by running the following command in your terminal or command prompt:
```
./mvnw spring-boot:run
```

## Accessing Swagger Documentation

Once the project is running, you can access the Swagger REST API documentation page by navigating to `http://localhost:8000/swagger-ui.html` in your web browser. This page provides an interactive API documentation that details the available endpoints and the expected request and response formats.
