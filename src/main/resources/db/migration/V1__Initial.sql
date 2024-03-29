create table if not exists publisher
(
    id   bigint primary key generated by default as identity,
    name varchar(100) not null
);

create table if not exists book
(
    id               bigint primary key generated by default as identity,
    title            varchar(100) not null,
    pages            int,
    publication_year varchar,
    publisher_id     bigint       references publisher (id) on delete set null,
    price            double precision check ( price > 0 )
);

create table if not exists author
(
    id            bigint primary key generated by default as identity,
    name          varchar not null,
    surname       varchar not null,
    lastname      varchar,
    date_of_birth varchar not null
);

create table if not exists author_book
(
    author_id bigint references author (id) on delete set null,
    book_id   bigint references book (id) on delete set null,
    primary key (author_id, book_id)
);

create table if not exists genre
(
    id   bigint primary key generated by default as identity,
    name varchar(100) not null
);

alter table genre
    add column author_id bigint references author (id) on delete set null;
alter table genre
    add column book_id bigint references book (id) on delete set null;