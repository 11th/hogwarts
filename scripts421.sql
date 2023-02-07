-- create table student(
--     id bigserial primary key,
--     name varchar(100) not null,
--     age integer check (age >= 16) default 20,
--     faculty_id bigint references faculty (id)
-- );
--
-- create table faculty(
--     id bigserial primary key,
--     name varchar(100) not null,
--     color varchar(50) not null
-- );

alter table student
    add constraint age_great_equal_16 check (age >= 16),
    add constraint name_unique unique (name),
    add constraint name_not_null check (name not null),
    add constraint default_age default 20 for age;

alter table faculty
    add constraint unique_name_color unique (name, color);