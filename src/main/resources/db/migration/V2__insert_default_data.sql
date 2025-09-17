-- V2__insert_default_data.sql

-- Вставляем запись только если её нет
INSERT INTO application_state (id, is_data_loaded)
SELECT 1, false
WHERE NOT EXISTS (SELECT 1 FROM application_state WHERE id = 1);
