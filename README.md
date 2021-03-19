# Expense Reimbursement System - Backend

## Project Description

The Expense Reimbursement System (ERS) manages the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests.

Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

This is the backend repository. The frontend is in a separate repository.

## Technologies Used

- PostgreSQL (GCP SQL)
- RESTful API
- Java
- JDBC
- Javalin (deployed on GCP Compute VM)
- Hibernate
- JWT - JSON Web Token

## Features

Employees and managers can login and be redirected to a role based portal. Upon successful login, the user will be given a JWT which will be used for further requests to the backend API that requires authentication. The home page is a dashboard that features a table listing all of their expenses.

The main table can be sorted on the following columns:
- Employee Name
- Submitted At
- Status

The search bar provides search/filter capabilities on all of the columns.

From here, the user can switch to a "statistics" view by clicking on the dropdown menu from their avatar on the top navbar. The user can also logout from this dropdown menu, which will delete the JWT. Once the JWT is deleted, the user will need to login again to access the dashboard.

Valid expense statuses are:
- Pending
- Approved
- Denied

To-do wish list:
- Add upload file functionality
- Add ability to create/register new users

## Getting Started

Requirements:
- JDK 8+
- A hibernate.cfg.xml is not included and will be required to run

### Clone Repository
```
git clone https://github.com/dcheun/2102GCP-P1.git
```

### Install Dependencies and Build
Project contains a build.gradle file. Change directories into the cloned repository and run the following build command to build the executable jar file.
```
./gradlew fatJar
```
### Run
This will build the artifact into the *build/libs* folder. To run the jar:
```
java -jar build/libs/<name_of_jar_file>
```

## Usage

The main RESTful API endpoints provided are in the form:
- BASE_URL/users/login
- BASE_URL/users
- BASE_URL/users/:id/expenses (protected - requires JWT Authentication in header)

Once the javalin server is running, it will begin to serve HTTP API requests.

### Testing

The project contains JUnit tests for DAO, service, and other layers. Mockito is used where appropriate. To run tests, you can either run `gradlew test` at the command line or use an IDE such as IntelliJ IDEA. Note that a hibernate.cfg.xml file is required.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
