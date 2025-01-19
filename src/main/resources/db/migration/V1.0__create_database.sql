DO $$
BEGIN
   IF NOT EXISTS (
       SELECT FROM pg_database WHERE datname = 'jpa'
   ) THEN
       CREATE DATABASE jpa;
   END IF;
END
$$;