-- TeamFlow Sample Data
-- Note: Passwords are BCrypt hashed version of plain text passwords
-- Run after application first start (JPA creates schema) OR use as reference

-- Default users are auto-created by DataInitializer on first run:
-- admin@teamflow.com / admin123 (ADMIN)
-- manager@teamflow.com / manager123 (MANAGER)
-- developer@teamflow.com / dev123 (DEVELOPER)
-- reviewer@teamflow.com / review123 (REVIEWER)

-- Sample projects (IDs assume users exist with IDs 1-4)
INSERT INTO projects (project_name, description, start_date, end_date, status, manager_id, created_at, updated_at)
VALUES
('TeamFlow Platform', 'Core project management platform development', DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY), 'ACTIVE', 2, NOW(), NOW()),
('Mobile App Integration', 'Integrate mobile app with backend APIs', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY), 'PLANNING', 2, NOW(), NOW());

-- Sample tasks
INSERT INTO tasks (title, description, priority, status, assigned_to_id, project_id, estimated_hours, actual_hours, due_date, dependency_task_id, created_at, updated_at)
VALUES
('Setup Database Schema', 'Design and implement MySQL schema', 'HIGH', 'COMPLETED', 3, 1, 8.0, 6.0, DATE_SUB(CURDATE(), INTERVAL 5 DAY), NULL, NOW(), NOW()),
('Implement User Authentication', 'Spring Security with role-based access', 'HIGH', 'IN_PROGRESS', 3, 1, 16.0, 10.0, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 1, NOW(), NOW()),
('Build REST APIs', 'Create CRUD APIs for all modules', 'MEDIUM', 'TODO', 3, 1, 24.0, NULL, DATE_ADD(CURDATE(), INTERVAL 14 DAY), 2, NOW(), NOW()),
('Design UI Mockups', 'Create Bootstrap-based responsive UI', 'LOW', 'TODO', 3, 2, 12.0, NULL, DATE_ADD(CURDATE(), INTERVAL 20 DAY), NULL, NOW(), NOW());

-- Sample incident
INSERT INTO incidents (title, description, severity, reported_by_id, assigned_engineer_id, status, project_id, created_date, updated_at)
VALUES
('Login page slow on mobile', 'Page load exceeds 3 seconds on mobile devices', 'MEDIUM', 3, 3, 'OPEN', 1, NOW(), NOW());

-- Sample review
INSERT INTO reviews (task_id, reviewer_id, comments, status, created_at, updated_at)
VALUES
(1, 4, 'Schema looks good, approved', 'APPROVED', NOW(), NOW());

-- Sample notifications
INSERT INTO notifications (recipient_id, title, message, type, reference_id, is_read, created_at)
VALUES
(3, 'Task Assigned', 'You have been assigned task: Build REST APIs', 'TASK_ASSIGNED', 3, 0, NOW()),
(4, 'Review Required', 'Please review task: Implement User Authentication', 'TASK_COMPLETED', 2, 0, NOW());
