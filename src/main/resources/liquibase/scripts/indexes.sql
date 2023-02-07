-- liquibase formatted sql

-- changeset 11th:1
create index students_name_index on student (name);

-- changeset 11th:2
create index faculties_name_color_index on faculty (name, color);