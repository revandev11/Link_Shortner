CREATE DATABASE IF NOT EXISTS url_service_db;
CREATE DATABASE IF NOT EXISTS analytics_service_db;

GRANT ALL PRIVILEGES ON url_service_db.* TO 'app'@'%';
GRANT ALL PRIVILEGES ON analytics_service_db.* TO 'app'@'%';

FLUSH PRIVILEGES;