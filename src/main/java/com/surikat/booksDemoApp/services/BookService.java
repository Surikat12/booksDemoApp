package com.surikat.booksDemoApp.services;

import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity create(BookEntity  book);

    List<BookEntity > findAll();

    Page<BookEntity> findAll(Pageable pageable);

    Optional<BookEntity > findById(Long id);

    boolean isExists(Long id);

    BookEntity  fullUpdate(Long id, BookEntity  book);

    BookEntity  partialUpdate(Long id, BookEntity  book);

    void delete(Long id);
}
