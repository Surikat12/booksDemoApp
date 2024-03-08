package com.surikat.booksDemoApp;

import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;


public class TestDataUtil {
    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Author A")
                .age(50)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Author B")
                .age(42)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Author C")
                .age(63)
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
