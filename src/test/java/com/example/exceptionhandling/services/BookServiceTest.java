package com.example.exceptionhandling.services;

import com.example.exceptionhandling.models.Book;
import com.example.exceptionhandling.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImp bookService;

    @Test
    void getAllBooks_should_return_book_list() {
        // Given
        Book book = this.buildTestingBook();
        // When
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<Book> employees = this.bookService.getAllBooks();
        // Then
        assertEquals(1, employees.size());
        verify(this.bookRepository).findAll();
    }

    @Test
    void getBookById_should_return_book() {
        // Given
        Book book = this.buildTestingBook();
        // When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book returnedBook = this.bookService.getBookById(1L);
        // Then
        assertEquals(book.getId(), returnedBook.getId());
        verify(this.bookRepository).findById(1L);
    }

    @Test
    void createBook_should_insert_new_book() {
        // Given
        Book book = this.buildTestingBook();
        // When
        this.bookService.createBook(book);
        // Then
        verify(this.bookRepository).save(book);
    }


    private Book buildTestingBook() {
        return new Book(1L, "Misery", "Stephen King");
    }
}
