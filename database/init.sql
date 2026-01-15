-- NRCC Database Management System
-- Database Initialization Script
-- PostgreSQL

-- Create database (run as postgres superuser)
-- CREATE DATABASE nrcc_dev_db;
-- CREATE USER nrcc_user WITH PASSWORD 'nrcc_password';
-- GRANT ALL PRIVILEGES ON DATABASE nrcc_dev_db TO nrcc_user;

-- Connect to the database and run the following:

-- Sample data for testing

-- Insert sample users (password is 'password123' encrypted with BCrypt)
INSERT INTO users (name, email, phone_number, password, role, organization, region, status, email_verified, phone_verified, created_at, updated_at)
VALUES
    ('Admin User', 'admin@nrcc.go.tz', '+255712345678', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'SYSTEM_ADMINISTRATOR', 'Roads Fund Board', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Minister Works', 'minister@works.go.tz', '+255712345679', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'MINISTER_OF_WORKS', 'Ministry of Works', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('NRCC Chair', 'chair@nrcc.go.tz', '+255712345680', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'NRCC_CHAIRPERSON', 'NRCC', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('NRCC Member 1', 'member1@nrcc.go.tz', '+255712345681', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'NRCC_MEMBER', 'NRCC', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Regional Commissioner', 'rc@dodoma.go.tz', '+255712345682', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'REGIONAL_COMMISSIONER', 'Regional Administration', 'Dodoma', 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('RAS Dodoma', 'ras@dodoma.go.tz', '+255712345683', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'REGIONAL_ADMINISTRATIVE_SECRETARY', 'Regional Administration', 'Dodoma', 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Public User', 'public@example.com', '+255712345684', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'PUBLIC_APPLICANT', NULL, NULL, 'ACTIVE', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MP Dodoma', 'mp@parliament.go.tz', '+255712345685', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'MEMBER_OF_PARLIAMENT', 'Parliament', 'Dodoma', 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Ministry Lawyer', 'lawyer@works.go.tz', '+255712345686', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'MINISTRY_LAWYER', 'Ministry of Works', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('NRCC Secretariat', 'secretariat@nrcc.go.tz', '+255712345687', '$2a$10$6EhQe/5KlYSWTvLdlJjOL.hW9hN6F1jgXYxVnI7LnMf6KXk8VLK8S', 'NRCC_SECRETARIAT', 'NRCC', NULL, 'ACTIVE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert sample roads
INSERT INTO roads (name, road_number, length, current_class, start_point, end_point, region, district, surface_type, carriageway_width, formation_width, road_reserve_width, status, created_at, updated_at)
VALUES
    ('Dodoma - Kondoa Road', 'DR101', 85.5, 'DISTRICT', 'Dodoma Town', 'Kondoa Town', 'Dodoma', 'Dodoma Urban', 'Gravel', 6.0, 8.0, 30.0, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Mpwapwa - Kibakwe Road', 'DR102', 42.3, 'DISTRICT', 'Mpwapwa Town', 'Kibakwe Village', 'Dodoma', 'Mpwapwa', 'Earth', 5.0, 7.0, 20.0, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Singida - Manyoni Road', 'DR201', 95.7, 'DISTRICT', 'Singida Town', 'Manyoni Town', 'Singida', 'Singida Urban', 'Gravel', 6.5, 8.5, 30.0, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Notes:
-- 1. Password for all test users is: password123
-- 2. In production, ensure to change all default passwords
-- 3. BCrypt hash shown is for 'password123'
-- 4. Update email addresses and phone numbers as needed
-- 5. This script should be run after the schema is created by Hibernate

COMMENT ON TABLE users IS 'System users with various roles';
COMMENT ON TABLE roads IS 'Road inventory';
COMMENT ON TABLE applications IS 'Road reclassification applications';
COMMENT ON TABLE application_form_data IS 'Application form data (Fourth Schedule)';
COMMENT ON TABLE eligibility_criteria_selections IS 'Selected eligibility criteria per application';
COMMENT ON TABLE approval_actions IS 'Workflow approval history and audit trail';
COMMENT ON TABLE verification_assignments IS 'NRCC member verification assignments';
COMMENT ON TABLE verification_reports IS 'Site verification reports';
COMMENT ON TABLE nrcc_meetings IS 'NRCC meeting records';
COMMENT ON TABLE recommendations IS 'NRCC recommendations to Minister';
COMMENT ON TABLE minister_decisions IS 'Minister final decisions';
COMMENT ON TABLE gazettements IS 'Gazettement tracking';
COMMENT ON TABLE appeals IS 'Appeal submissions for refused applications';
COMMENT ON TABLE notifications IS 'Email/SMS/Portal notifications';
COMMENT ON TABLE action_plans IS 'NRCC annual action plans';
COMMENT ON TABLE action_plan_targets IS 'Action plan targets';
COMMENT ON TABLE action_plan_activities IS 'Action plan activities';
COMMENT ON TABLE action_plan_cost_items IS 'Budget cost items';
