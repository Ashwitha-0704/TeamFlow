# TeamFlow - Project Management System

TeamFlow is a full-stack project management application designed to facilitate user management, project progress tracking, task dependency management, incident reporting, peer code reviews, and real-time notifications. 

The application is structured into two main components:
1. **Backend:** A Spring Boot 3 Java REST API connected to a MySQL database.
2. **Frontend:** A responsive Bootstrap 5 client serving HTML, CSS, and JavaScript.

---

## 📋 Project Overview

TeamFlow provides a collaborative platform for software development teams. Managers can create projects and assign tasks, developers can track their workload and submit tasks for review, and reviewers can approve/reject changes, all while logging incident reports and receiving notifications on updates.

---

## 🚀 Setup Instructions

### 1. Prerequisites
Ensure you have the following installed:
* **Java Development Kit (JDK) 17** or higher
* **MySQL Server 8.0** or higher
* **Python 3** (for serving the frontend UI)

---

### 2. Database Setup
1. Start your local MySQL server.
2. The database `teamflow` will be **automatically created on startup** via the Spring Boot connection string.
3. If you want to seed the database with advanced sample data, you can import the sample SQL script after starting the backend:
   ```bash
   mysql -u root -pRishii@07 teamflow < TeamFlow/src/main/resources/data/sample-data.sql
   ```

---

### 3. Backend Setup & Run
1. Navigate to the backend directory:
   ```bash
   cd TeamFlow
   ```
2. Build the project using Maven:
   ```bash
   ./mvnw clean package
   ```
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
The backend server will start running on **`http://localhost:8080`**.

---

### 4. Frontend Setup & Run
Since the frontend consists of static files making asynchronous requests, you can serve them locally:
1. Navigate to the frontend directory:
   ```bash
   cd Frontend
   ```
2. Start a local server (using Python 3):
   ```bash
   python -m http.server 5500
   ```
3. Open your browser and navigate to **`http://localhost:5500/login.html`**.

---

## ⚙️ Environment Variables & Configuration

The backend application is configured inside [TeamFlow/src/main/resources/application.properties](./TeamFlow/src/main/resources/application.properties). You can override these configurations using the following environment variables if needed:

| Property | Default Value | Description |
|---|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/teamflow?...` | MySQL JDBC connection URL |
| `spring.datasource.username` | `root` | Database username |
| `spring.datasource.password` | `Rishii@07` | Database password |
| `server.port` | `8080` | Port for the backend API |
| `spring.jpa.hibernate.ddl-auto`| `update` | Hibernate schema strategy |

---

## 🛠️ Features Implemented

### Key Modules:
* **User Authentication:** Secure authentication using Spring Security session management. Default accounts are pre-seeded:
  * **Admin:** `admin@teamflow.com` (Password: `admin123`)
  * **Manager:** `manager@teamflow.com` (Password: `manager123`)
  * **Developer:** `developer@teamflow.com` (Password: `dev123`)
  * **Reviewer:** `reviewer@teamflow.com` (Password: `review123`)
* **Project Management:** Create, read, update, and delete projects. Assign project managers and set project statuses (`PLANNING`, `ACTIVE`, `COMPLETED`, `ON_HOLD`).
* **Task Management:** Create tasks with estimation hours, priorities (`LOW`, `MEDIUM`, `HIGH`), status tracking, assignments, and **Task Dependencies** (blocks tasks until their parent tasks are completed).
* **Incident Tracking:** Log issues or incidents linked to projects, track severity (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`), and assign engineers.
* **Code Review Workflow:** Seamless code review cycle. When a developer marks a task as `IN_REVIEW`, a peer review is created. The assigned reviewer can approve or reject the task with feedback.
* **Real-time Notifications:** Users receive instant alerts when assigned tasks, when reviews are requested, or when incident reports are filed.
* **Reports and Dashboards:** Interactive visualization of key statistics:
  * Task completions and developer productivity.
  * Project progress gauges.
  * Incident status and severity breakdowns.

---

## 🧠 Assumptions Made

1. **Independent Frontend Hosting:** The frontend is treated as a single-page style app serving plain HTML/JS/CSS assets. It acts as a client consuming RESTful APIs hosted on port `8080`.
2. **CORS Policy:** It is assumed the frontend runs on local environments (specifically `http://localhost:5500` or `file://*`). CORS rules configured in [SecurityConfig.java](./TeamFlow/src/main/java/com/example/teamflow/config/SecurityConfig.java) allow these origins.
3. **Database Seeding on Startup:** On initial application launch, if the database has no records, Spring Boot automatically seeds default users, projects, tasks, and incidents programmatically so the app is immediately usable.

---

## ⚠️ Known Limitations

1. **MySQL Reserved Keywords:** The `Notification` entity's `read` field was conflicting with MySQL's reserved keyword `READ`. The mapping has been adjusted to use the `is_read` column name in the database table to prevent syntax issues.
2. **Session Persistence:** User authentication is cookie/session-based (`SessionCreationPolicy.IF_REQUIRED`). Users must log in via the API to initialize a valid session cookie.
3. **Missing Node/NPM dependencies:** The frontend setup lacks a Node compiler pipeline; it uses vanilla JS and fetches directly, meaning standard local web hosting tools (like Python) are required instead of `npm`.

---

## 🗄️ Database Schema & Entities

The database schema is dynamically generated by Spring Data JPA. Below is the list of primary database tables and their structural representations:

### Database Tables:
* **`users`:** Stores system accounts with fields: `id` (PK), `name`, `email` (Unique), `password` (BCrypt), `role` (ADMIN, MANAGER, DEVELOPER, REVIEWER), and `active` status.
* **`projects`:** Stores projects with fields: `id` (PK), `project_name`, `description`, `start_date`, `end_date`, `status`, and `manager_id` (FK to `users`).
* **`tasks`:** Stores tasks with fields: `id` (PK), `title`, `description`, `priority`, `status`, `assigned_to_id` (FK to `users`), `project_id` (FK to `projects`), `dependency_task_id` (FK to `tasks`), `estimated_hours`, `actual_hours`, and `due_date`.
* **`incidents`:** Stores reported bugs/issues with fields: `id` (PK), `title`, `description`, `severity`, `status`, `project_id` (FK to `projects`), `reported_by_id` (FK to `users`), and `assigned_engineer_id` (FK to `users`).
* **`reviews`:** Stores code review status with fields: `id` (PK), `task_id` (FK to `tasks`), `reviewer_id` (FK to `users`), `comments`, and `status` (APPROVED, REJECTED, PENDING).
* **`notifications`:** Stores alert messages with fields: `id` (PK), `recipient_id` (FK to `users`), `title`, `message`, `type`, `reference_id`, and `is_read`.