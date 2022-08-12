package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Deprecated
@Component
public class BookConvertor {
    private final ModelMapper modelMapper;

    @Autowired
    public BookConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
