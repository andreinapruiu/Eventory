-- This file contains only initial data (not schema definition)

-- Insert roles if they don't exist
INSERT INTO project.roles (id, name)
VALUES (3, 'ORGANIZER')
ON CONFLICT (id) DO NOTHING;

-- Insert initial categories if they don't exist
INSERT INTO project.categories (id, name, description)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Conference', 'Professional gatherings for networking and learning'),
    ('22222222-2222-2222-2222-222222222222', 'Workshop', 'Hands-on sessions for skill development'),
    ('33333333-3333-3333-3333-333333333333', 'Concert', 'Musical performances and entertainment events'),
    ('44444444-4444-4444-4444-444444444444', 'Exhibition', 'Displays of art, products, or services'),
    ('55555555-5555-5555-5555-555555555555', 'Webinar', 'Online educational or informative sessions')
ON CONFLICT (id) DO NOTHING; 