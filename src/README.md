# ConstructHub

> A console-based Construction Site Management System built in Java, following a clean **Model-View (MV)** architectural pattern.

---

## Table of Contents

- [Overview](#overview)
- [Default Login](#default-login)
- [Roles & Permissions](#roles--permissions)
- [Features](#features)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Data Models](#data-models)
- [How to Run](#how-to-run)
- [Usage Guide](#usage-guide)

---

## Overview

ConstructHub is a Java console application that helps construction companies manage their sites, workers, tasks, attendance, and salary calculations. It supports two user roles — **Owner** and **Supervisor** — each with their own dashboard and permissions.

All data is stored in-memory using a Singleton database (`ConstructionDB`). No external database or internet connection is required.

---

## Default Login

When the application starts for the first time, a default Owner account is automatically created.

| Field    | Value    |
|----------|----------|
| Username | `gk`     |
| Password | `270606` |
| Role     | `OWNER`  |

> The Owner can create Supervisor accounts from within the app.

---

## Roles & Permissions

| Feature                    | Owner | Supervisor |
|----------------------------|:-----:|:----------:|
| View Sites                 |  ✅   |    ✅      |
| Add Site                   |  ✅   |    ❌      |
| Mark Site as Completed     |  ✅   |    ❌      |
| Manage Workers             |  ✅   |    ✅      |
| Manage Tasks               |  ✅   |    ✅      |
| Mark Attendance            |  ✅   |    ✅      |
| Salary & Reports           |  ✅   |    ❌      |
| Create Supervisor Account  |  ✅   |    ❌      |

---

## Features

### Sign In
- Only Sign In is available at the landing screen — no public registration.
- Invalid credentials show a clear error message with a retry option.

### Site Management
- Add new construction sites with name, location, start date, and optional end date.
- View all sites in a formatted table (ID, Name, Location, Status, Dates).
- Mark a site as **COMPLETED** (Owner only).
- Sites have two statuses: `ACTIVE` and `COMPLETED`.

### Worker Management
- Add workers with name, trade type (e.g. Mason, Electrician, Plumber), and daily wage.
- Each worker is auto-assigned a unique ID in the format `WRK0001`.
- View all workers with their ID, type, daily wage, and status.
- Deactivate a worker (sets status to `INACTIVE`).

### Task Management
- Create tasks linked to a specific site and optionally to a specific worker.
- Set a deadline (optional) in `dd-MM-yyyy` format.
- View tasks filtered by site.
- Update task status: `PENDING` → `IN_PROGRESS` → `COMPLETED`.

### Attendance
- Allocate a worker to a site on a specific date (prevents double allocation).
- Mark attendance as **Present** or **Absent** for a worker on a site and date.
- View attendance records for a site on a given date.
- View available (unallocated) workers on a given date.
- Prevents duplicate attendance entries for the same worker, site, and date.

### Salary & Reports
- Calculate weekly salary for a worker on a site based on their attendance.
- Formula: `Total Amount = Days Present × Daily Wage`
- View full salary history for a specific worker.
- View all salary records across all workers and sites.

### Create Supervisor (Owner only)
- Owner can create Supervisor accounts from within the app.
- Username must be at least 3 characters and unique.
- Password must be at least 6 characters.

---

## Project Structure

```
src/
└── com/gowthamX/constructHub/
    ├── Main.java                          ← Entry point, seeds default Owner
    │
    ├── data/
    │   ├── dto/
    │   │   ├── User.java                  ← User DTO (OWNER / SUPERVISOR)
    │   │   ├── Site.java                  ← Site DTO (ACTIVE / COMPLETED)
    │   │   ├── Worker.java                ← Worker DTO (ACTIVE / INACTIVE)
    │   │   ├── Task.java                  ← Task DTO (PENDING / IN_PROGRESS / COMPLETED)
    │   │   ├── Attendance.java            ← Attendance record DTO
    │   │   ├── Allocation.java            ← Worker-to-site daily allocation DTO
    │   │   └── Salary.java                ← Weekly salary record DTO
    │   │
    │   └── repository/
    │       └── ConstructionDB.java        ← Singleton in-memory database
    │
    ├── features/
    │   ├── signin/
    │   │   ├── SignInModel.java
    │   │   └── SignInView.java
    │   │
    │   ├── home/
    │   │   ├── HomeModel.java
    │   │   └── HomeView.java              ← Owner & Supervisor dashboards
    │   │
    │   ├── site/
    │   │   ├── SiteModel.java
    │   │   └── SiteView.java
    │   │
    │   ├── worker/
    │   │   ├── WorkerModel.java
    │   │   └── WorkerView.java
    │   │
    │   ├── task/
    │   │   ├── TaskModel.java
    │   │   └── TaskView.java
    │   │
    │   ├── attendance/
    │   │   ├── AttendanceModel.java
    │   │   └── AttendanceView.java
    │   │
    │   ├── salary/
    │   │   ├── SalaryModel.java
    │   │   └── SalaryView.java
    │   │
    │   └── createsupervisor/
    │       ├── CreateSupervisorModel.java
    │       └── CreateSupervisorView.java
    │
    └── util/
        ├── ConsoleInput.java              ← Shared Scanner (singleton)
        └── ParseHelper.java               ← Date parsing, formatting, helpers
```

---

## Architecture

ConstructHub follows a **Model-View (MV)** pattern — the same pattern used throughout the project.

### How it works

Each feature has exactly two classes:

- **Model** — handles all business logic, validation, and database calls. It holds a reference to its View and calls callback methods on it.
- **View** — handles all console I/O (`System.out`, `Scanner`). It creates the Model, passes itself to it, and delegates all decisions to the Model.

```
View  ──creates──▶  Model
View  ◀──callback──  Model (e.g. onSiteAdded, onSiteFailed)
Model ──calls──▶  ConstructionDB
```

### Example flow (Add Site)

```
SiteView.promptAddSite()
    └──▶ siteModel.addSite(name, location, startDate, endDate)
              ├── validates inputs
              ├── calls ConstructionDB.getInstance().addSite(site)
              └── calls siteView.onSiteAdded(site)   ← callback
                       └── prints success message
```

### Key design decisions

| Decision | Reason |
|---|---|
| Model holds View reference | Model calls View callbacks — View never pulls data |
| View creates Model (not the other way) | View is the entry point; it owns its Model |
| `ConstructionDB` is a Singleton | Single shared in-memory store, no setup needed |
| One `Scanner` via `ConsoleInput` | Prevents multiple Scanner instances on `System.in` |
| Timestamps stored as `Long` (epoch ms) | Simple, sort-friendly, no date library dependency |
| Worker IDs auto-generated (`WRK0001`) | Consistent, human-readable identifiers |

---

## Data Models

### User
| Field      | Type     | Notes                        |
|------------|----------|------------------------------|
| id         | Long     | Auto-incremented primary key |
| name       | String   |                              |
| username   | String   | Unique, case-insensitive     |
| password   | String   |                              |
| role       | Role     | `OWNER` or `SUPERVISOR`      |
| createdAt  | Long     | Epoch milliseconds           |

### Site
| Field      | Type        | Notes                        |
|------------|-------------|------------------------------|
| id         | Long        | Auto-incremented primary key |
| name       | String      | Min 3 characters             |
| location   | String      |                              |
| status     | SiteStatus  | `ACTIVE` or `COMPLETED`      |
| startDate  | Long        | Epoch ms, required           |
| endDate    | Long        | Epoch ms, optional           |
| createdAt  | Long        |                              |

### Worker
| Field      | Type          | Notes                        |
|------------|---------------|------------------------------|
| id         | Long          | Auto-incremented primary key |
| workerId   | String        | e.g. `WRK0001`               |
| name       | String        |                              |
| type       | String        | e.g. Mason, Electrician      |
| dailyWage  | double        | Must be positive             |
| status     | WorkerStatus  | `ACTIVE` or `INACTIVE`       |
| createdAt  | Long          |                              |

### Task
| Field       | Type        | Notes                                  |
|-------------|-------------|----------------------------------------|
| id          | Long        | Auto-incremented primary key           |
| siteId      | Long        | Foreign key → Site                     |
| workerId    | Long        | Optional foreign key → Worker          |
| description | String      | Min 5 characters                       |
| status      | TaskStatus  | `PENDING`, `IN_PROGRESS`, `COMPLETED`  |
| deadline    | Long        | Optional epoch ms                      |
| createdAt   | Long        |                                        |
| updatedAt   | Long        |                                        |

### Attendance
| Field     | Type    | Notes                          |
|-----------|---------|--------------------------------|
| id        | Long    | Auto-incremented primary key   |
| workerId  | Long    | Foreign key → Worker           |
| siteId    | Long    | Foreign key → Site             |
| date      | Long    | Epoch ms (day-level match)     |
| present   | boolean | `true` = Present, `false` = Absent |
| markedAt  | Long    |                                |

### Allocation
| Field     | Type | Notes                          |
|-----------|------|--------------------------------|
| id        | Long | Auto-incremented primary key   |
| workerId  | Long | Foreign key → Worker           |
| siteId    | Long | Foreign key → Site             |
| date      | Long | Epoch ms (day-level match)     |
| createdAt | Long |                                |

### Salary
| Field       | Type   | Notes                                     |
|-------------|--------|-------------------------------------------|
| id          | Long   | Auto-incremented primary key              |
| workerId    | Long   | Foreign key → Worker                      |
| siteId      | Long   | Foreign key → Site                        |
| totalDays   | int    | Number of present days in the week        |
| totalAmount | double | `totalDays × dailyWage`                   |
| weekStart   | Long   | Epoch ms                                  |
| weekEnd     | Long   | Epoch ms                                  |
| paidAt      | Long   | Epoch ms, auto-set on record creation     |

---

## How to Run

### Requirements
- Java 8 or above
- No external libraries or Maven/Gradle required

### Compile

```bash
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

### Run

```bash
java -cp out com.gowthamX.constructionHub.Main
```

---

## Usage Guide

### First Time Setup

1. Run the application.
2. Sign in with `admin` / `admin123`.
3. Go to **"Create Supervisor Account"** to add supervisors.

### Typical Workflow

```
1. Add a Site           → Site Management → Add New Site
2. Add Workers          → Worker Management → Add New Worker
3. Allocate Workers     → Attendance → Allocate Worker to Site
4. Mark Attendance      → Attendance → Mark Attendance
5. Create Tasks         → Task Management → Create New Task
6. Update Task Status   → Task Management → Update Task Status
7. Calculate Salary     → Salary & Reports → Calculate & Pay Weekly Salary
```

### Date Format

All dates must be entered in `dd-MM-yyyy` format.

```
Example: 08-05-2026
```

---

*ConstructHub v1.0.0 — Built with Java, MV Pattern*