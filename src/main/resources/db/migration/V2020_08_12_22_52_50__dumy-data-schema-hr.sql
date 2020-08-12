insert into hr.employees(id, nama, tanggal_lahir, tanggal_join, salary, job_id, manager_id, created_date, created_by)
values ('001', 'Deni Sutisna', '1999-01-01', '2012-01-01', 1000, 'ceo', null, now(), 'migration'),
       ('002', 'Hari Sapto Adi', '1999-01-01', '2012-01-01', 1000, 'cto', '001', now(), 'migration'),
       ('003', 'Junaedi', '1999-01-01', '2012-01-01', 1000, 'pm', '002', now(), 'migration'),
       ('004', 'Muhamad Purwadi', '1999-01-01', '2012-01-01', 1000, 'pm', '002', now(), 'migration'),
       ('005', 'Dimas Maryanto', '1999-01-01', '2012-01-01', 1000, 'prog', '004', now(), 'migration'),
       ('006', 'Muhamad Yusuf', '1999-01-01', '2012-01-01', 1000, 'prog', '003', now(), 'migration'),
       ('007', 'Abdul', '1999-01-01', '2012-01-01', 1000, 'prog', '003', now(), 'migration');
