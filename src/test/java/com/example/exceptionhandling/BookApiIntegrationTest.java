package com.example.exceptionhandling;

import com.example.exceptionhandling.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiIntegrationTest {

    private final int port = 8080;

    @Autowired
    private TestRestTemplate restTemplate;

    private Book createdBook;


    @BeforeEach
    public void setup() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"name\": \"Test Book\", \"author\": \"Test Author\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Book> response = restTemplate.postForEntity("http://localhost:" + port + "/api/books", requestEntity, Book.class);
        assertNotNull(response);
        createdBook = response.getBody();
    }

    @Test
    public void testGetAllBooks() {
        String url = "http://localhost:" + port + "/api/books";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteBook() {
        String url = "http://localhost:" + port + "/api/books/{id}";

        restTemplate.delete(url, createdBook.getId());

        // Проверяем, что книга была удалена
        ResponseEntity<Book> getResponse = restTemplate.getForEntity(url, Book.class, createdBook.getId());
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void testUpdateBook() {
        String url = "http://localhost:" + port + "/api/books/{id}";

        // Создаем обновленные данные книги
        String updatedJsonBody = "{\"name\": \"Updated Book\", \"author\": \"Updated Author\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(updatedJsonBody, headers);

        // Выполняем PUT-запрос для обновления книги
        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Book.class, createdBook.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что книга была успешно обновлена
        Book updatedBook = response.getBody();
        assertNotNull(updatedBook);
        assertEquals("Updated Book", updatedBook.getName());
        assertEquals("Updated Author", updatedBook.getAuthor());
    }

    @Test
    public void testGetBook() {
        String url = "http://localhost:" + port + "/api/books/{id}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, createdBook.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
