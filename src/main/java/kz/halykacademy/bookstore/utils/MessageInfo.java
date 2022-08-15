package kz.halykacademy.bookstore.utils;

public class MessageInfo {
    public static final String MESSAGE_SUCCESS = "Success";

    // author
    public static final String MESSAGE_AUTHOR_NOT_FOUND = "Author is not found with id = %d";
    public static final String MESSAGE_AUTHOR_EXISTED = "This author is existed";
    public static final String MESSAGE_LIST_AUTHORS = "List of authors are empty";

    // book
    public static final String MESSAGE_BOOK_NOT_FOUND = "Book is not found with id = %d";
    public static final String MESSAGE_BOOK_EXISTED = "This book is existed";
    public static final String MESSAGE_LIST_BOOKS = "List of books are empty";

    // genre
    public static final String MESSAGE_GENRE_NOT_FOUND = "Genre is not found with id = %d";
    public static final String MESSAGE_GENRE_EXISTED = "This genre is existed";
    public static final String MESSAGE_LIST_GENRES = "List of genres are empty";

    // publisher
    public static final String MESSAGE_PUBLISHER_NOT_FOUND = "Publisher is not found with id = %d";
    public static final String MESSAGE_PUBLISHER_EXISTED = "This publisher is existed";
    public static final String MESSAGE_LIST_PUBLISHERS = "List of publishers are empty";

    // user
    public static final String MESSAGE_USER_NOT_FOUND = "User is not found with id = %d";
    public static final String MESSAGE_USER_EXISTED = "This user is existed";
    public static final String MESSAGE_LIST_USERS = "List of users are empty";

    // order
    public static final String MESSAGE_ORDER_NOT_FOUND = "Order is not found with id = %d";
    public static final String MESSAGE_ORDER_PRICE = "Order price should not be greater than 10000 tg. Total price of order is: %.2f";
    public static final Double MAX_PRICE_ORDER = 10000.0;
    public static final String MESSAGE_LIST_ORDERS = "List of orders are empty";
}