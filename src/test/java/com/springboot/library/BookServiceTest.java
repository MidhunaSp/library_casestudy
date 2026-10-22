package com.springboot.library;

import com.springboot.library.dto.request.BookReqDto;
import com.springboot.library.dto.request.BookUpdateDto;
import com.springboot.library.dto.response.BookInfoDto;
import com.springboot.library.enums.Genre;
import com.springboot.library.exception.DuplicateResourceException;
import com.springboot.library.exception.InvalidCallException;
import com.springboot.library.exception.ResourceNotFoundException;
import com.springboot.library.model.Book;
import com.springboot.library.repository.BookRepository;
import com.springboot.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository; // fake repo

    @Test
    public void getByIdTest() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookInfoDto result = bookService.getById(1L);

        assertEquals("Clean Code", result.title());
    }

    @Test
    public void getByIsbnTest() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        BookInfoDto result = bookService.getByIsbn("9780132350884");

        assertEquals("Clean Code", result.title());
        assertEquals("Robert Martin", result.author());
    }

    @Test
    public void getByIsbnTest_Invalid() {
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.getByIsbn("0000000000"));
    }

    @Test
    public void addBookTest() {
        // dto goes into the service
        BookReqDto dto = new BookReqDto("Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        // entity comes back from the repository
        Book saved = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        BookInfoDto result = bookService.addBook(dto);

        assertEquals(1L, result.id());
        assertEquals("9780132350884", result.isbn());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void addBookTest_Duplicate() {
        BookReqDto dto = new BookReqDto("Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> bookService.addBook(dto));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void updateTest() {
        Book existing = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        BookUpdateDto dto = new BookUpdateDto("Clean Code 2nd", "Uncle Bob", 2020, Genre.TECHNOLOGY);
        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);

        BookInfoDto result = bookService.update("9780132350884", dto);

        assertEquals("Clean Code 2nd", result.title());
        assertEquals(2020, result.publicationYear());
        assertEquals(Genre.TECHNOLOGY, result.genre());
    }

    @Test
    public void deleteTest() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        bookService.deleteByIsbn("9780132350884");

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void getAllTest() {
        Book b1 = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        Book b2 = new Book(2L, "The Hobbit", "J.R.R. Tolkien", "0547928226", 1937, Genre.FANTASY);

        // the repository now returns a Page, not a List
        when(bookRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))))
                .thenReturn(new PageImpl<>(List.of(b1, b2)));

        List<BookInfoDto> result = bookService.getAll(0, 5);

        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).title());
    }

    @Test
    public void getAllTest_NoPagination() {
        Book b1 = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        Book b2 = new Book(2L, "The Hobbit", "J.R.R. Tolkien", "0547928226", 1937, Genre.FANTASY);
        when(bookRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BookInfoDto> result = bookService.getAll(null, null);

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getAllTest_WithPagination() {
        Book b1 = new Book(1L, "Clean Code", "Robert Martin", "9780132350884", 2008, Genre.TECHNOLOGY);
        // the repository returns a Page when we pass a PageRequest
        when(bookRepository.findAll(PageRequest.of(0, 5, Sort.by("id"))))
                .thenReturn(new PageImpl<>(List.of(b1)));

        List<BookInfoDto> result = bookService.getAll(0, 5);

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).title());
    }


    @Test
    public void getAllTest_InvalidPage() {
        assertThrows(InvalidCallException.class, () -> bookService.getAll(-1, 5));
        assertThrows(InvalidCallException.class, () -> bookService.getAll(0, 0));

        verifyNoInteractions(bookRepository);
    }
}