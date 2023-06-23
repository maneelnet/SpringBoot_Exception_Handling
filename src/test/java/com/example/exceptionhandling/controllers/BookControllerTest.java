package com.example.exceptionhandling.controllers;

import com.example.exceptionhandling.exceptions.ResourceNotFoundException;
import com.example.exceptionhandling.models.Book;
import com.example.exceptionhandling.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookService bookService;

    Book book1 = new Book(1L, "It", "Stephen King");
    Book book2 = new Book(2L, "The Stand", "Stephen King");
    Book book3 = new Book(3L, "Misery", "Stephen King");

    @Test
    public void getAllBooks_success() throws Exception {
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Misery")));
    }

    @Test
    public void getBookById_success() throws Exception {
        Mockito.when(bookService.getBookById(2L)).thenReturn(book2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("The Stand")));
    }


    @Test
    public void getBookByName_success() throws Exception {
        Mockito.when(bookService.getBookByName("Misery")).thenReturn(book3);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("bookName", "Misery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("name", is("Misery")));
    }

    @Test
    public void createRecord_success() throws Exception {

        Mockito.when(bookService.createBook(book1)).thenReturn(book1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(book1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("It")));
    }

    @Test
    public void deletePatientById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
