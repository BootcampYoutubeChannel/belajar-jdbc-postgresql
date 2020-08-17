alter table audit.system_audit
    add column version_created_by character varying(100) not null default 'db';
