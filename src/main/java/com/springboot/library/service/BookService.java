package com.springboot.library.service;

import com.springboot.library.dto.request.BookReqDto;
import com.springboot.library.dto.request.BookUpdateDto;
import com.springboot.library.dto.response.BookInfoDto;
import com.springboot.library.exception.DuplicateResourceException;
import com.springboot.library.exception.InvalidCallException;
import com.springboot.library.exception.ResourceNotFoundException;
import com.springboot.library.mapper.BookMapper;
import com.springboot.library.model.Book;
import com.springboot.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookInfoDto getById(Long id) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("Invalid book id");
        return BookMapper.mapEntityToInfoDto(optional.get());
    }


    public BookInfoDto getByIsbn(String isbn) {
        return BookMapper.mapEntityToInfoDto(findBookByIsbn(isbn));
    }

    public BookInfoDto addBook(BookReqDto dto) {
        if (bookRepository.existsByIsbn(dto.isbn()))
            throw new DuplicateResourceException("Book with ISBN already exists");

        Book book = BookMapper.mapDtoToEntity(dto);
        Book saved = bookRepository.save(book);
        return BookMapper.mapEntityToInfoDto(saved);
    }

    public BookInfoDto update(String isbn, BookUpdateDto dto) {
        Book book = findBookByIsbn(isbn);
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setPublicationYear(dto.publicationYear());
        book.setGenre(dto.genre());
        Book saved = bookRepository.save(book);
        return BookMapper.mapEntityToInfoDto(saved);
    }

    public void deleteByIsbn(String isbn) {
        Book book = findBookByIsbn(isbn);
        bookRepository.delete(book);
    }

    private Book findBookByIsbn(String isbn) {
        Optional<Book> optional = bookRepository.findByIsbn(isbn);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("Invalid isbn");
        return optional.get();
    }

    public List<BookInfoDto> getAll(Integer page, Integer size) {
        // no pagination asked: return everything
        if (page == null && size == null) {
            return bookRepository.findAll()
                    .stream()
                    .map(BookMapper::mapEntityToInfoDto)
                    .toList();
        }

        if (page == null || size == null)
            throw new InvalidCallException("Send both page and size, or neither");

        if (page < 0 || size < 1)
            throw new InvalidCallException("Page must be 0 or more and size must be 1 or more");

        // sorted by id so the pages always come in the same order
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .stream()
                .map(BookMapper::mapEntityToInfoDto)
                .toList();
    }
}