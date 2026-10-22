package com.springboot.library.controller;

import com.springboot.library.dto.request.BookReqDto;
import com.springboot.library.dto.request.BookUpdateDto;
import com.springboot.library.dto.response.BookInfoDto;
import com.springboot.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // retrieve one book by id (USER, ADMIN)
    @GetMapping("/api/book/id/{id}")
    public BookInfoDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    // retrieve all books, pagination is optional (USER, ADMIN)
    @GetMapping("/api/book/all")
    public List<BookInfoDto> getAllBooks(@RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
        return bookService.getAll(page, size);
    }

    // retrieve one book by isbn (USER, ADMIN)
    @GetMapping("/api/book/{isbn}")
    public BookInfoDto getByIsbn(@PathVariable String isbn) {
        return bookService.getByIsbn(isbn);
    }

    // add a new book (ADMIN)
    @PostMapping("/api/book/add")
    public BookInfoDto addBook(@Valid @RequestBody BookReqDto bookReqDto) {
        return bookService.addBook(bookReqDto);
    }

    // update an existing book (ADMIN)
    @PutMapping("/api/book/{isbn}")
    public BookInfoDto update(@PathVariable String isbn,
                              @Valid @RequestBody BookUpdateDto dto) {
        return bookService.update(isbn, dto);
    }

    // delete a book by isbn (ADMIN)
    @DeleteMapping("/api/book/{isbn}")
    public void deleteByIsbn(@PathVariable String isbn) {
        bookService.deleteByIsbn(isbn);
    }
}