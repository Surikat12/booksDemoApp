package com.surikat.booksDemoApp;

import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;

import java.time.LocalDate;


public class TestDataUtil {
    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .name("Author A")
                .birthdate(LocalDate.of(1910, 1, 18))
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .name("Author B")
                .birthdate(LocalDate.of(1968, 5, 3))
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .name("Author C")
                .birthdate(LocalDate.of(1890, 7, 25))
                .build();
    }

    public static BookEntity createTestBookA(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .title("Book A")
                .description("Description of Book A")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .title("Book B")
                .description("Description of Book B")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .title("Book C")
                .description("Description of Book C")
                .author(authorEntity)
                .build();
    }
}
