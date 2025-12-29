INSERT INTO owner ( name, email) VALUES ('Unknown Owner', 'unknow.owner@example.com');
INSERT INTO owner ( name, email) VALUES ('Ana Pop', 'ana.pop@example.com');
INSERT INTO owner ( name, email) VALUES ('Bogdan Ionescu', 'bogdan.ionescu@example.com');

INSERT INTO car (vin, make, model, year_of_manufacture, owner_id) VALUES ('VIN12345', 'Dacia', 'Logan', 2018, 2);
INSERT INTO car (vin, make, model, year_of_manufacture, owner_id) VALUES ('VIN67890', 'VW', 'Golf', 2021, 3);

INSERT INTO insurance_policy (car_id, provider, start_date, end_date, expiry_notified) VALUES (1, 'Allianz', DATE '2024-01-01', DATE '2024-12-31', false);
INSERT INTO insurance_policy (car_id, provider, start_date, end_date, expiry_notified) VALUES (1, 'Groupama', DATE '2025-01-01', DATE '2025-08-31', false);
INSERT INTO insurance_policy (car_id, provider, start_date, end_date, expiry_notified) VALUES (2, 'Allianz', DATE '2025-03-01', DATE '2025-08-31', false);

INSERT INTO insurance_claim (car_id, claim_date, description, amount) VALUES 1, DATE '2025-06-10', 'Accident', 850.00);