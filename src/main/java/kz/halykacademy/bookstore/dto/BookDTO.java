package kz.halykacademy.bookstore.dto;

public class BookDTO {
    private Long id;
    private String title;
    private double price;
//    private List<AuthorDTO> authors;

    public BookDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//    public List<AuthorDTO> getAuthors() {
//        return authors;
//    }
//
//    public void setAuthors(List<AuthorDTO> authors) {
//        this.authors = authors;
//    }
}
