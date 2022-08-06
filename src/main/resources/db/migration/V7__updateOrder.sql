alter table order_ drop column created_by;

alter table order_ add column user_id bigint references user_(id) on DELETE set null ;

