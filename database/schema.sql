-- ════════════════════════════════════════════════════════════════
-- Girls Hostel Management System — Database Schema
-- ════════════════════════════════════════════════════════════════
-- This file did not exist in the original project. It has been
-- reconstructed from the exact columns referenced by the DAO classes
-- in src/dao/*.java, so the application will run correctly against it.
--
-- HOW TO USE:
--   1. Open MySQL (Workbench, CLI, or any client) and connect as a
--      user that can create databases (e.g. root).
--   2. Run this entire file once:
--         mysql -u root -p < schema.sql
--      (or paste its contents into a MySQL Workbench query tab and
--       execute it).
--   3. Update src/database/DBConnection.java with YOUR MySQL
--      username/password if they differ from the defaults below.
-- ════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS hostel_management;
USE hostel_management;

-- ── Admin login ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS admins (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Default admin account: username "admin", password "admin123"
-- (change/remove this after first login if you want a different one)
INSERT INTO admins (username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (SELECT 1 FROM admins WHERE username = 'admin');

-- ── Students ───────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS students (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    student_code  VARCHAR(20)   NOT NULL UNIQUE,
    name          VARCHAR(100)  NOT NULL,
    age           INT,
    room_no       INT,
    contact       VARCHAR(20),
    password      VARCHAR(100),
    occupation    VARCHAR(100),
    email         VARCHAR(150),
    dob           VARCHAR(20),
    address       VARCHAR(255),
    floor         INT,
    joining_date  VARCHAR(20),
    photo         MEDIUMTEXT      -- base64 data-URL of the profile photo
);

-- ── Employees / Staff ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employees (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    role    VARCHAR(100),
    contact VARCHAR(20)
);

-- ── Rooms ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS rooms (
    room_no  INT PRIMARY KEY,
    capacity INT NOT NULL DEFAULT 0,
    occupied INT NOT NULL DEFAULT 0
);

-- ── Facilities ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS facilities (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    facility_name VARCHAR(100) NOT NULL,
    timing        VARCHAR(100),
    description   TEXT
);

-- ── Notices ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notices (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(150) NOT NULL,
    message     TEXT,
    date_posted VARCHAR(20)
);

-- ── Rent ───────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS rent (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    student_code   VARCHAR(20),
    student_name   VARCHAR(100),
    month          VARCHAR(20),
    room_no        INT,
    room_rent      DOUBLE,
    electric_bill  DOUBLE,
    meter_reading  VARCHAR(50),
    total          DOUBLE,
    status         VARCHAR(20) DEFAULT 'Pending'
);

-- ── Complaints ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS complaint (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    student_code   VARCHAR(20),
    student_name   VARCHAR(100),
    title          VARCHAR(150),
    category       VARCHAR(50),
    description    TEXT,
    complaint_date VARCHAR(20),
    status         VARCHAR(20) DEFAULT 'Pending',
    remarks        TEXT
);

-- ── Student Leave / Outing requests ────────────────────────────
-- (documented inside src/dao/LeaveDAO.java as well)
CREATE TABLE IF NOT EXISTS student_leave (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    student_code VARCHAR(50),
    student_name VARCHAR(100),
    reason       VARCHAR(100),
    destination  VARCHAR(200),
    details      TEXT,
    leave_date   VARCHAR(20),
    leave_time   VARCHAR(10),
    return_date  VARCHAR(20),
    days         VARCHAR(10),
    status       VARCHAR(20) DEFAULT 'Pending'
);

-- ── (Optional) Sample rooms so the registration form has data ──
INSERT INTO rooms (room_no, capacity, occupied)
SELECT * FROM (SELECT 101, 2, 0) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_no = 101);

INSERT INTO rooms (room_no, capacity, occupied)
SELECT * FROM (SELECT 102, 2, 0) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_no = 102);

INSERT INTO rooms (room_no, capacity, occupied)
SELECT * FROM (SELECT 201, 3, 0) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE room_no = 201);
