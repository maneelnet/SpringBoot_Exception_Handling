package com.example.exceptionhandling.services;

import com.example.exceptionhandling.exceptions.ResourceNotFoundException;
import com.example.exceptionhandling.models.Book;
import com.example.exceptionhandling.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImp implements BookService {

    final BookRepository bookRepository;

    public BookServiceImp(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Book with ID: "+id+" Not Found!"));
    }

    @Override
    public Book getBookByName(String name) {
        return bookRepository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Book with Name: "+name+" Not Found!"));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book oldBook = getBookById(id);
        book.setId(oldBook.getId());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.deleteById(book.getId());
    }
}
