package com.example.exceptionhandling.services;

import com.example.exceptionhandling.models.Book;

import java.util.List;

public interface BookService {

    Book getBookById(Long id);
    Book getBookByName(String name);
    List<Book> getAllBooks();
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);

}
