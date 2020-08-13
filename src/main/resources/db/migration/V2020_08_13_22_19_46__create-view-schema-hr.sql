create or replace view hr.employee_detail
as
select emp.id                as employee_id,
       emp.nama              as employee_name,
       emp.tanggal_lahir     as employee_birthdate,
       emp.tanggal_join      as employee_join,
       emp.salary            as employee_salary,
       emp.job_id            as employee_job_id,
       job.name              as employee_job_name,
       emp.created_date      as employee_created_date,
       emp.created_by        as employee_created_by,
       emp.last_updated_date as employee_last_updated_date,
       emp.last_updated_by   as employee_last_updated_by,
       man.id                as manager_id,
       man.nama              as manager_name,
       man.tanggal_lahir     as manager_birthdate,
       man.tanggal_join      as manager_join,
       man.salary            as manager_salary,
       man.job_id            as manager_job_id,
       man_job.name          as manager_job_name,
       man.created_date      as manager_created_date,
       man.created_by        as manager_created_by,
       man.last_updated_date as manager_last_updated_date,
       man.last_updated_by   as manager_last_updated_by
from hr.employees emp
         left join hr.employees man on emp.manager_id = man.id
         left join hr.jobs job on emp.job_id = job.id
         left join hr.jobs man_job on man.job_id = man_job.id
