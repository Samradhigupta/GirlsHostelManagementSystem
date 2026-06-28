# Girls Hostel Management System

A hostel management system with a Java backend (plain `HttpServer`, no
framework) and an HTML/CSS/JavaScript frontend. Data is stored in MySQL.

## 1. Requirements

- **JDK 17+** (any recent Java Development Kit)
- **MySQL Server** running locally
- **VS Code** with the **"Extension Pack for Java"** (Microsoft) installed
  — this gives you Run/Debug buttons for `src/main.java` and reads the
  MySQL driver jar automatically via `.vscode/settings.json`.

## 2. One-time database setup

1. Make sure MySQL is running.
2. Run the schema script once, either via the command line:
   ```
   mysql -u root -p < database/schema.sql
   ```
   or by opening `database/schema.sql` in MySQL Workbench (or any MySQL
   client) and executing it.
   This creates the `hostel_management` database, every table the app
   needs, a default admin login (`admin` / `admin123`), and a few sample
   rooms.
3. Open `src/database/DBConnection.java` and set `user` / `password` to
   **your own** MySQL credentials (the values that ship in this file are
   a placeholder and will not match your installation).

## 3. Running the backend (in VS Code)

1. Open this folder in VS Code.
2. Open `src/main.java`.
3. Click the **Run** button above `public static void main(...)` (or
   press `F5`). VS Code's Java extension will compile everything in
   `src/` automatically, including the MySQL driver from `lib/`.
4. You should see in the terminal:
   ```
   Database Connected Successfully!
   ✅ Hostel Server Running on Port 8080
   ```
   Leave this running — it's your backend API server.

   If you see a database error instead, re-check step 2 and 3 above.

## 4. Running the frontend (in the browser)

The backend only serves data (no HTML), so open the pages directly:

- Double-click `web/login.html`, **or**
- In VS Code, right-click `web/login.html` → "Open with Live Server"
  (if you have the Live Server extension), **or**
- Open it directly via `File > Open File` in your browser.

The frontend talks to the backend at `http://localhost:8080`, so make
sure the server from step 3 is still running before you log in.

**Default admin login:** username `admin`, password `admin123`
(from the schema script — change it any time from the `admins` table).

To create a student account, log in as Admin and use
**Students → Register New Student**.

## 5. Project structure

```
src/
  main.java            Entry point — starts HostelServer
  server/              HTTP request handlers (one per API endpoint)
  dao/                 Database access (JDBC) classes
  model/               Plain data classes (Student, Room, Rent, ...)
  database/            DBConnection.java — MySQL connection settings
web/
  login.html           Login page
  admin.html           Admin dashboard
  student.html         Student dashboard
  js/, css/            Frontend scripts and styles
database/
  schema.sql           Run this once to create the MySQL database
lib/
  mysql-connector-j-.../  MySQL JDBC driver (already referenced by
                           .vscode/settings.json — no extra setup needed)
```
