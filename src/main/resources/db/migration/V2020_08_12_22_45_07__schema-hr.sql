create table hr.jobs
(
    id   character varying(64)  not null primary key default uuid_generate_v4(),
    name character varying(100) not null
);

insert into hr.jobs (id, name)
VALUES ('ceo', 'Chief Executive Officer'),
       ('cto', 'Chief Technology Officer'),
       ('pm', 'Project Manager'),
       ('prog', 'Programmer');


create table hr.employees
(
    id                character varying(64)  not null primary key default uuid_generate_v4(),
    nama              character varying(64)  not null,
    tanggal_lahir     date,
    tanggal_join      date,
    salary            decimal                not null             default 0,
    job_id            character varying(64)  not null,
    manager_id        character varying(64),
    created_date      timestamp              not null             default now(),
    created_by        character varying(100) not null,
    last_updated_date timestamp,
    last_updated_by   character varying(100)
);

alter table hr.employees
    add constraint fk_employee_job_id foreign key (job_id)
        references hr.jobs (id) on update cascade on delete cascade;

alter table hr.employees
    add constraint fk_employee_manager_id foreign key (manager_id)
        references hr.employees (id) on update cascade on delete set null;

alter table hr.employees
    add constraint ck_eq_manager_id check ( manager_id <> id )
