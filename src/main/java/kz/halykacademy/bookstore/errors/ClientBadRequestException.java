package kz.halykacademy.bookstore.errors;

public class ClientBadRequestException extends RuntimeException {
    public ClientBadRequestException(String message) {
        super(message);
    }
}
