# SkillBridge — Career Skill Gap Analyzer

A full-stack Java web application that compares a user's current skills with the skills required for a target job role and calculates a simple readiness percentage.

## Features

- Responsive HTML/CSS/JavaScript frontend
- Java Spring Boot REST API
- MySQL database persistence through Spring Data JPA
- Preloaded role profiles: Java Developer, Frontend Developer, Data Analyst and Backend Developer
- Add/remove current skills in the browser
- Skill-match percentage, matched skills and missing skills report
- REST endpoints to list, add and delete job roles

## Technology stack

- Java 17+
- Spring Boot 3.3.5
- Spring Web, Spring Data JPA
- MySQL 8+
- HTML5, CSS3, JavaScript
- Maven

## Prerequisites

Install Java 17 or later, Maven, and MySQL Server. Confirm that the `java`, `mvn`, and `mysql` commands work in your terminal.

## Run locally

1. Download and extract this repository.
2. Start MySQL Server.
3. Open `src/main/resources/application.properties`.
4. Set your MySQL username/password. The defaults are `root` / `root`; change them to match your local MySQL setup. You can also set environment variables `DB_USER` and `DB_PASSWORD`.
5. In a terminal, move into the project folder and run:

   ```bash
   mvn spring-boot:run
   ```

6. Open **http://localhost:8080** in your browser.

The app creates the `skillbridge` database (if your MySQL user has permission), creates its table, and inserts example job roles on first startup.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/roles` | List job roles |
| POST | `/api/roles` | Add a job role |
| DELETE | `/api/roles/{id}` | Delete a job role |
| POST | `/api/analyze` | Compare skills with a role |

### Example analysis request

```json
{
  "roleId": 1,
  "skills": ["Java", "SQL", "OOP"]
}
```
