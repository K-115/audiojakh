ALTER TABLE artists ADD COLUMN IF NOT EXISTS description TEXT;

-- UPDATE artists SET description = '...' WHERE name = '...'