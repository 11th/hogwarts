create table persons(
    id serial primary key,
    first_name varchar(70) not null,
    last_name varchar(70) not null,
    age integer check (age > 0),
    driver_license boolean default false,
    car_id integer references cars (id)
);

create table cars(
    id serial primary key,
    brand varchar(50) not null,
    model varchar(50) not null,
    cost numeric check (cost > 0)
);

alter table cars
    add constraint brand_model unique (brand, model);