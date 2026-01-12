INSERT INTO zones (zone_name, access_level) VALUES
  ('EV/Visitor', 'EV'),
  ('Senior', 'SENIOR'),
  ('General', 'JUNIOR');

INSERT INTO slots (floor_no, zone_id, slot_code, status, has_ev_charger) VALUES
  (1, 1, 'F1-EV01', 'FREE', TRUE),
  (1, 1, 'F1-EV02', 'FREE', TRUE),
  (1, 1, 'F1-EV03', 'FREE', FALSE),
  (1, 2, 'F1-S01', 'FREE', FALSE),
  (1, 2, 'F1-S02', 'FREE', FALSE),
  (1, 3, 'F1-G01', 'FREE', FALSE),
  (1, 3, 'F1-G02', 'FREE', FALSE),
  (1, 3, 'F1-G03', 'FREE', FALSE),
  (1, 3, 'F1-G04', 'FREE', FALSE),
  (2, 1, 'F2-EV01', 'FREE', TRUE),
  (2, 1, 'F2-EV02', 'FREE', FALSE),
  (2, 2, 'F2-S01', 'FREE', FALSE),
  (2, 2, 'F2-S02', 'FREE', FALSE),
  (2, 3, 'F2-G01', 'FREE', FALSE),
  (2, 3, 'F2-G02', 'FREE', FALSE),
  (2, 3, 'F2-G03', 'FREE', FALSE),
  (2, 3, 'F2-G04', 'FREE', FALSE),
  (3, 1, 'F3-EV01', 'FREE', TRUE),
  (3, 2, 'F3-S01', 'FREE', FALSE),
  (3, 2, 'F3-S02', 'FREE', FALSE),
  (3, 3, 'F3-G01', 'FREE', FALSE),
  (3, 3, 'F3-G02', 'FREE', FALSE),
  (3, 3, 'F3-G03', 'FREE', FALSE),
  (3, 3, 'F3-G04', 'FREE', FALSE),
  (3, 3, 'F3-G05', 'FREE', FALSE),
  (3, 3, 'F3-G06', 'FREE', FALSE);

INSERT INTO employees (emp_id, name, dept, role, vehicle_type, sustainability_points) VALUES
  ('E001', 'Anita Rao', 'Engineering', 'EMPLOYEE', 'CAR', 10),
  ('E002', 'Vikram Shah', 'Design', 'EMPLOYEE', 'BIKE', 5),
  ('E003', 'Meera Iyer', 'Management', 'ADMIN', 'CAR', 20),
  ('E004', 'Rahul Nair', 'Engineering', 'EMPLOYEE', 'EV', 30),
  ('E005', 'Kavya Menon', 'Sales', 'EMPLOYEE', 'CAR', 12),
  ('E006', 'Sandeep Das', 'Support', 'EMPLOYEE', 'BIKE', 8),
  ('E007', 'Priya Singh', 'HR', 'EMPLOYEE', 'CAR', 7),
  ('E008', 'Neha Kapoor', 'Management', 'ADMIN', 'EV', 25);
