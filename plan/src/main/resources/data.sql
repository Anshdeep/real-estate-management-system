-- Seed a sample builder so API calls using builderId=1 succeed in dev
INSERT INTO builders (id, builder_code, company_name, type, status, primary_phone, email_id, country, is_deleted, created_at)
VALUES (1, 'BLD-0001', 'Sample Builders Pvt Ltd', 'PRIVATE_LIMITED', 'ACTIVE', '9876543210', 'info@samplebuilders.example', 'India', 0, NOW())
ON DUPLICATE KEY UPDATE company_name = VALUES(company_name);
