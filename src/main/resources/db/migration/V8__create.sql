create table if not exists order_book
(
    order_id bigint references order_ (id) on delete set null,
    book_id   bigint references book (id) on delete set null,
    primary key (order_id, book_id)
);

