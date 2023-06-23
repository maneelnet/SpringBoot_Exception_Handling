package com.example.exceptionhandling.controllers;

import com.example.exceptionhandling.models.Book;
import com.example.exceptionhandling.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api")
public class BookController {

    final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books")
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/books/{id}")
    ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping(value = "/book")
    ResponseEntity<Book> getBookByName(@RequestParam String bookName) {
        Book book = bookService.getBookByName(bookName);
        return ResponseEntity.ok().body(book);
    }

    @PostMapping(value = "/books")
    ResponseEntity<?> createBook(@RequestBody Book book) {
        Book addedBook = bookService.createBook(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedBook.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/books/{id}")
    ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok().body(updatedBook);
    }

    @DeleteMapping(value = "/books/{id}")
    ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().body("Book deleted");
    }

}
