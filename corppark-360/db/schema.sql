CREATE DATABASE IF NOT EXISTS corppark360 DEFAULT CHARACTER SET utf8mb4;
use corppark360;

CREATE TABLE employees (
  emp_id VARCHAR(20) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  dept VARCHAR(50) NOT NULL,
  role ENUM('EMPLOYEE','ADMIN') DEFAULT 'EMPLOYEE',
  vehicle_type ENUM('CAR','BIKE','EV') NOT NULL,
  sustainability_points INT DEFAULT 0
);

CREATE TABLE zones (
  zone_id INT PRIMARY KEY AUTO_INCREMENT,
  zone_name VARCHAR(50) NOT NULL,
  access_level ENUM('VISITOR','JUNIOR','SENIOR','EV') NOT NULL
);

CREATE TABLE slots (
  slot_id INT PRIMARY KEY AUTO_INCREMENT,
  floor_no INT NOT NULL,
  zone_id INT NOT NULL,
  slot_code VARCHAR(20) UNIQUE,
  status ENUM('FREE','OCCUPIED') DEFAULT 'FREE',
  has_ev_charger BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_slots_zone FOREIGN KEY (zone_id) REFERENCES zones(zone_id)
);

CREATE TABLE reservations (
  res_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  emp_id VARCHAR(20) NOT NULL,
  slot_id INT NOT NULL,
  entry_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  exit_ts TIMESTAMP NULL,
  pinned_pillar_id VARCHAR(20) NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE KEY uniq_active_emp (emp_id, active),
  UNIQUE KEY uniq_active_slot (slot_id, active),
  CONSTRAINT fk_res_emp FOREIGN KEY (emp_id) REFERENCES employees(emp_id),
  CONSTRAINT fk_res_slot FOREIGN KEY (slot_id) REFERENCES slots(slot_id)
);

CREATE TABLE visitor_invites (
  invite_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  host_emp_id VARCHAR(20),
  guest_name VARCHAR(100),
  guest_email VARCHAR(150),
  qr_token VARCHAR(64) UNIQUE,
  valid_until TIMESTAMP,
  used BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_invite_host FOREIGN KEY (host_emp_id) REFERENCES employees(emp_id)
);

CREATE TABLE violations (
  violation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  emp_id VARCHAR(20),
  slot_id INT,
  reason VARCHAR(200),
  created_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_violation_emp FOREIGN KEY (emp_id) REFERENCES employees(emp_id),
  CONSTRAINT fk_violation_slot FOREIGN KEY (slot_id) REFERENCES slots(slot_id)
);
