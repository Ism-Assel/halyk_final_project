package kz.halykacademy.bookstore.models.enums;

public enum OrderStatus {
    CREATED("CREATED"),
    IN_PROCESS("IN_PROCESS"),
    FINISHED("FINISHED"),
    CANCELLED("CANCELLED");

    private String id;

    OrderStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
