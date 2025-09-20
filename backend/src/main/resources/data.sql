-- Insert default roles
INSERT IGNORE INTO roles (name, description, created_at) VALUES 
('USER', 'Regular user role', NOW()),
('ADMIN', 'Administrator role', NOW());

-- Insert default categories
INSERT IGNORE INTO categories (name, description, created_at, updated_at) VALUES 
('Chocolate', 'Delicious chocolate sweets', NOW(), NOW()),
('Traditional', 'Traditional Indian sweets', NOW(), NOW()),
('Bakery', 'Fresh bakery items', NOW(), NOW()),
('Sugar-Free', 'Healthy sugar-free options', NOW(), NOW());

-- Insert admin user (password: admin123)
INSERT IGNORE INTO users (email, password, first_name, last_name, enabled, created_at, updated_at) VALUES 
('admin@sweetshop.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewbXrGdQZj6H2dWe', 'Admin', 'User', true, NOW(), NOW());

-- Assign admin role to admin user
INSERT IGNORE INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.email = 'admin@sweetshop.com' AND r.name = 'ADMIN';

-- Insert sample sweets
INSERT IGNORE INTO sweets (name, description, price, quantity, category_id, created_at, updated_at) VALUES 
('Dark Chocolate Bar', 'Premium 70% dark chocolate bar', 8.99, 50, 
    (SELECT id FROM categories WHERE name = 'Chocolate'), NOW(), NOW()),
('Milk Chocolate Truffles', 'Creamy milk chocolate truffles', 12.99, 30, 
    (SELECT id FROM categories WHERE name = 'Chocolate'), NOW(), NOW()),
('Gulab Jamun', 'Traditional sweet balls in sugar syrup', 6.99, 40, 
    (SELECT id FROM categories WHERE name = 'Traditional'), NOW(), NOW()),
('Rasgulla', 'Soft spongy balls in sugar syrup', 5.99, 35, 
    (SELECT id FROM categories WHERE name = 'Traditional'), NOW(), NOW()),
('Chocolate Croissant', 'Fresh baked croissant with chocolate filling', 4.99, 25, 
    (SELECT id FROM categories WHERE name = 'Bakery'), NOW(), NOW()),
('Sugar-Free Dark Chocolate', 'Dark chocolate sweetened with stevia', 9.99, 20, 
    (SELECT id FROM categories WHERE name = 'Sugar-Free'), NOW(), NOW());
