package com.springboot.library.mapper;

import com.springboot.library.dto.request.BookReqDto;
import com.springboot.library.dto.response.BookInfoDto;
import com.springboot.library.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static Book mapDtoToEntity(BookReqDto bookReqDto) {
        Book book = new Book();
        book.setTitle(bookReqDto.title());
        book.setAuthor(bookReqDto.author());
        book.setIsbn(bookReqDto.isbn());
        book.setPublicationYear(bookReqDto.publicationYear());
        book.setGenre(bookReqDto.genre());
        return book;
    }

    public static BookInfoDto mapEntityToInfoDto(Book book) {
        return new BookInfoDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getGenre()
        );
    }
}
