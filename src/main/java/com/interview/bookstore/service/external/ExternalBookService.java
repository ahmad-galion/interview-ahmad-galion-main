package com.interview.bookstore.service.external;

import com.interview.bookstore.domain.Book;

import java.io.IOException;
import java.util.List;

public interface ExternalBookService {
    List<Book> getBooksByAuthor(String authorName) throws IOException;
}
