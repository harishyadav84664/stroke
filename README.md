# NIDUS · Stroke Identification Workstation

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-green.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-H2%20%7C%20MySQL-orange.svg)](#database-options)
[![License](https://img.shields.io/badge/License-Proprietary%20%2F%20Research-lightgrey.svg)](#)

> **Innovation in stroke identification:** A clinical diagnostic reading-room workstation for neuroimaging triage (NCCT, DWI, CTA) to detect ischemic stroke, hemorrhagic stroke, and stroke mimics.

---

## ⚡ Quick Start

### Option 1: One-Click Startup (Recommended)

#### On Windows:
Double-click `run.bat` (or open PowerShell / Command Prompt and run):
```cmd
.\run.bat
```
This automatically boots the application with embedded H2 storage (zero configuration needed) and opens `http://localhost:8080` in your default browser.

#### On Linux / macOS:
```bash
chmod +x run.sh
./run.sh
```

---

### Option 2: Run with Executable JAR
```bash
# Run with embedded database (zero config)
java -jar target/app-dashboard-0.1.0.jar --spring.profiles.active=h2

# Or run with MySQL
java -jar target/app-dashboard-0.1.0.jar --spring.profiles.active=mysql
```

---

### Option 3: Run with Maven Wrapper
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

---

### Option 4: Run with Docker Compose
To run both MySQL 8.0 and NIDUS in isolated containers:
```bash
docker compose up --build
```
Then visit [http://localhost:8080](http://localhost:8080).

---

## 💾 Database Options

NIDUS supports two database profiles:

| Profile | Description | Configuration |
|---|---|---|
| **`h2` (Default)** | Embedded file-based database (`./data/stroke_nidus`). Requires **zero setup**. Data persists across restarts. Includes H2 console at `/h2-console`. | `spring.profiles.active=h2` |
| **`mysql`** | Production / local MySQL database (`jdbc:mysql://127.0.0.1:3306/stroke_nidus`). | `spring.profiles.active=mysql`<br>`MYSQL_USER=root`<br>`MYSQL_PASSWORD=yourpassword` |

---

## 🏥 Clinical Workstation Capabilities

1. **Reading Room Dashboard (`/`)**:
   - Live worklist metrics: total studies, suspected acute stroke cases, pending review queue, and average diagnostic model confidence.
   - Pathology breakdown: Ischemic vs Hemorrhagic vs Normal (No Acute Stroke).
   - Recent acute acquisitions list.

2. **Studies Worklist (`/studies`)**:
   - Filter by diagnostic call (`ISCHEMIC`, `HEMORRHAGIC`, `NO_STROKE`).
   - Filter by review status (`QUEUED`, `REVIEWED`).
   - Search by accession code, patient code, or vascular territory.

3. **Study Detail & Axial Scan Viewer (`/studies/{id}`)**:
   - Anatomical locator schematic displaying hemisphere lateralization and predicted vascular territory.
   - Diagnostic findings, NIHSS score, symptom onset window, and ASPECTS score.
   - Interactive diagnostic score trigger.
   - Clinical review sign-off & reopen workflow.
   - Radiologist clinical findings / impressions editor.

4. **Register Acute Acquisition (`/studies/new`)**:
   - Intake form for new emergency neuroimaging acquisitions with input validation.

5. **Diagnostic Methods & Model Architecture (`/model`)**:
   - Methodology breakdown, AUC/ROC metrics, sensitivity and specificity benchmarks, and pipeline steps.

---

## 🌐 REST API Reference

All web capabilities are also accessible via standard JSON REST endpoints under `/api`:

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/stats` | Summary statistics of the reading room worklist |
| `GET` | `/api/studies?q={query}&prediction={call}&status={status}` | List studies with optional search and filters |
| `GET` | `/api/studies/{id}` | Retrieve study details by ID |
| `POST` | `/api/studies` | Register a new neuroimaging case |
| `POST` | `/api/studies/{id}/score` | Compute diagnostic model prediction |
| `POST` | `/api/studies/{id}/review` | Mark study as clinically reviewed |
| `POST` | `/api/studies/{id}/reopen` | Reopen study for review |
| `PUT` | `/api/studies/{id}/findings` | Update clinical notes / findings |
| `DELETE` | `/api/studies/{id}` | Delete a study from the worklist |

### Sample API Request
```bash
# Register a new study
curl -X POST http://localhost:8080/api/studies \
  -H "Content-Type: application/json" \
  -d '{
    "accession": "NID-24099",
    "patientCode": "P-3101",
    "age": 64,
    "sex": "MALE",
    "modality": "MRI",
    "sequenceName": "DWI",
    "onsetHours": 1.8,
    "nihss": 12,
    "findings": "Suspected MCA branch cutoff"
  }'

# Run diagnostic score
curl -X POST http://localhost:8080/api/studies/1/score
```

---

## 🧪 Running Automated Tests

Run the test suite using Maven:
```bash
# Windows
.\mvnw.cmd test

# Linux / Mac
./mvnw test
```
All 18 unit, service, repository, and WebMvc integration tests run automatically against the in-memory H2 test context.

---

## 📦 Project Structure

```
app-dashboard/
├── .mvn/wrapper/               # Maven wrapper configuration & binaries
├── src/
│   ├── main/
│   │   ├── java/com/nidus/dashboard/
│   │   │   ├── model/          # Entities: Study, Modality, Prediction, ReviewStatus, Sex
│   │   │   ├── repo/           # Spring Data JPA Repository: StudyRepository
│   │   │   ├── scoring/        # Diagnostic scoring engine: DemoScorer
│   │   │   ├── service/        # Service layer: StudyService, StudySeeder
│   │   │   └── web/            # Controllers: DashboardController, StudyController, StudyApiController, ModelController
│   │   └── resources/
│   │       ├── db/             # MySQL schema initialization SQL
│   │       ├── static/         # CSS stylesheet & SVG icons
│   │       ├── templates/      # Thymeleaf UI views (index, studies, study, study-form, model)
│   │       ├── application.properties         # Main application configuration
│   │       ├── application-h2.properties      # Zero-config embedded H2 profile
│   │       └── application-mysql.properties   # Production MySQL profile
│   └── test/                   # Comprehensive unit and integration test suite
├── target/
│   └── app-dashboard-0.1.0.jar # Standalone executable fat JAR
├── docker-compose.yml          # Containerized MySQL & NIDUS configuration
├── Dockerfile                  # Application container build file
├── pom.xml                     # Maven build descriptor
├── run.bat                     # Windows 1-click launcher (H2)
├── run-mysql.bat               # Windows launcher (MySQL)
├── run.sh                      # Linux/Mac launcher (H2)
├── run-mysql.sh                # Linux/Mac launcher (MySQL)
└── README.md                   # Full documentation
```
