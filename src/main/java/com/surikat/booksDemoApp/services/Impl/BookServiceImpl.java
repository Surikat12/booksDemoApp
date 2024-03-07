package com.surikat.booksDemoApp.services.Impl;

import com.surikat.booksDemoApp.domain.entities.BookEntity;
import com.surikat.booksDemoApp.repositories.BookRepository;
import com.surikat.booksDemoApp.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public BookEntity create(BookEntity book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public BookEntity fullUpdate(Long id, BookEntity book) {
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public BookEntity partialUpdate(Long id, BookEntity book) {
        book.setId(id);

        return bookRepository.findById(id).map(existingBook -> {
            Optional.ofNullable(book.getTitle()).ifPresent(existingBook::setTitle);
            Optional.ofNullable(book.getYearPublished()).ifPresent(existingBook::setYearPublished);
            Optional.ofNullable(book.getAuthor()).ifPresent(existingBook::setAuthor);
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists"));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
