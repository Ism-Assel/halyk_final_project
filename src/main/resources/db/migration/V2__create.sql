create table user_
(
    id       bigint generated by default as identity primary key,
    login    varchar(100) unique not null,
    password varchar(100) not null,
    role     varchar(100) not null,
    is_admin boolean
);