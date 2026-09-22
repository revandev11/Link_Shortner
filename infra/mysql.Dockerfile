FROM mysql:8.4

COPY init-db.sql /docker-entrypoint-initdb.d/init-db.sql
