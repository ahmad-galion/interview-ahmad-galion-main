package com.interview.bookstore.service.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.bookstore.domain.Author;
import com.interview.bookstore.domain.Book;
import com.interview.bookstore.service.dto.external.BookResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InterviewBookService implements ExternalBookService {

    @Value("${external.book_api.key:heAhyfisnHARlWMVPIHgSgzpr8UhsHXZ}")
    private String apiKey;

    @Value("${external.book_api.api:https://api.nytimes.com/svc/books/v3/reviews.json?author=}")
    private String api;
    private final RestTemplate restTemplate;

    public InterviewBookService( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) throws IOException {

        String apiUrl = api + authorName + "&api-key=" + apiKey;

        ResponseEntity<BookResponseDTO> response = restTemplate.getForEntity(apiUrl, BookResponseDTO.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IOException("Failed to retrieve books for author: " + authorName);
        }


        return Objects.requireNonNull(response.getBody()).getResults().stream()
            .map(bookDTO -> new Book()
                .title(bookDTO.getBook_title())
                .author(new Author().name(bookDTO.getBook_author()))
            )
            .collect(Collectors.toList());

    }
}
