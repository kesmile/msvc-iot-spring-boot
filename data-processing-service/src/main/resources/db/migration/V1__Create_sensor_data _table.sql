-- 1. Crear la tabla normal de PostgreSQL
CREATE TABLE sensor_data (
    time TIMESTAMP WITH TIME ZONE NOT NULL,
    device_id VARCHAR(50) NOT NULL,
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION
);

-- 2. Convertir la tabla en una Hipertabla de TimescaleDB
SELECT create_hypertable('sensor_data', 'time');