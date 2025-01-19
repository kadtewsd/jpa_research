CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    CONSTRAINT  uq_user_id  UNIQUE(user_id)
);


