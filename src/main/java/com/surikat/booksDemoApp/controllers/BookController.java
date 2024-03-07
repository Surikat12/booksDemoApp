package com.surikat.booksDemoApp.controllers;

import com.surikat.booksDemoApp.domain.dto.AuthorDto;
import com.surikat.booksDemoApp.domain.dto.BookDto;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;
import com.surikat.booksDemoApp.mappers.Mapper;
import com.surikat.booksDemoApp.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PostMapping(path = "/books")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto book) {
        BookEntity bookEntity = bookMapper.mapFrom(book);
        BookEntity savedBookEntity = bookService.create(bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/books")
    public Page<BookDto> getBooks(Pageable pageable) {
        Page<BookEntity> foundBooks = bookService.findAll(pageable);
        return foundBooks.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{id}")
    public ResponseEntity<BookDto> GetAuthorById(@PathVariable("id") Long id) {
        Optional<BookEntity> foundBook = bookService.findById(id);

        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/books/{id}")
    public ResponseEntity<BookDto> fullUpdateBook(@PathVariable("id") Long id, @RequestBody BookDto book) {
        if (!bookService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(book);
        BookEntity savedAuthor = bookService.fullUpdate(id, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedAuthor), HttpStatus.OK);
    }

    @PatchMapping(path = "/books/{id}")
    ResponseEntity<BookDto> partialUpdateBook(@PathVariable("id") Long id, @RequestBody BookDto book) {
        if (!bookService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(book);
        BookEntity updatedBookEntity = bookService.partialUpdate(id, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{id}")
    ResponseEntity deleteBookById(@PathVariable("id") Long id) {
        bookService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
