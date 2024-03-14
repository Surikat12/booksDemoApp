package com.surikat.booksDemoApp.mappers.Impl;

import com.surikat.booksDemoApp.TestDataUtil;
import com.surikat.booksDemoApp.domain.dto.AuthorDto;
import com.surikat.booksDemoApp.domain.dto.BookDto;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;
import com.surikat.booksDemoApp.mappers.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModelMapper.class, AuthorMapperImpl.class})
class BookMapperImplTests {

    private final BookMapperImpl underTest;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;

    @Autowired
    public BookMapperImplTests(ModelMapper modelMapper, AuthorMapperImpl authorMapper) {
        this.underTest = new BookMapperImpl(modelMapper);
        this.authorMapper = authorMapper;
    }

    private void assertThatAuthorDtoEqualsAuthorEntity(AuthorDto authorDto, AuthorEntity authorEntity) {
        assertThat(authorDto.getId()).isEqualTo(authorEntity.getId());
        assertThat(authorDto.getName()).isEqualTo(authorEntity.getName());
        assertThat(authorDto.getBirthdate()).isEqualTo(authorEntity.getBirthdate());
    }

    @Test
    void testThatMapToCorrectlyMapBookWithAllPropertiesToBookDto() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(1L);
        BookEntity book = TestDataUtil.createTestBookA(author);
        book.setId(1L);

        //when
        BookDto bookDto = underTest.mapTo(book);

        //then
        assertThat(bookDto.getId()).isEqualTo(book.getId());
        assertThat(bookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookDto.getDescription()).isEqualTo(book.getDescription());
        assertThatAuthorDtoEqualsAuthorEntity(bookDto.getAuthor(), author);
    }

    @Test
    void testThatMapToCorrectlyMapBookWithAllPropertiesExceptIdToBookDto() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(1L);
        BookEntity book = TestDataUtil.createTestBookA(author);

        //when
        BookDto bookDto = underTest.mapTo(book);

        //then
        assertThat(bookDto.getId()).isNull();
        assertThat(bookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookDto.getDescription()).isEqualTo(book.getDescription());
        assertThatAuthorDtoEqualsAuthorEntity(bookDto.getAuthor(), author);
    }

    @Test
    void testThatMapFromCorrectlyMapBookDtoWithAllPropertiesToBook() {
        //given
        AuthorEntity realAuthor = TestDataUtil.createTestAuthorA();
        realAuthor.setId(1L);
        AuthorDto authorDto = authorMapper.mapTo(realAuthor);

        BookEntity realBook = TestDataUtil.createTestBookA(realAuthor);
        realBook.setId(1L);
        BookDto bookDto = BookDto.builder()
                .id(realBook.getId())
                .title(realBook.getTitle())
                .description(realBook.getDescription())
                .author(authorDto)
                .build();

        //when
        BookEntity mappedBook = underTest.mapFrom(bookDto);

        //then
        assertThat(mappedBook).isEqualTo(realBook);
    }

    @Test
    void testThatMapFromCorrectlyMapBookDtoWithAllPropertiesExceptIdToBook() {
        //given
        AuthorEntity realAuthor = TestDataUtil.createTestAuthorA();
        realAuthor.setId(1L);
        AuthorDto authorDto = authorMapper.mapTo(realAuthor);

        BookEntity realBook = TestDataUtil.createTestBookA(realAuthor);
        BookDto bookDto = BookDto.builder()
                .id(realBook.getId())
                .title(realBook.getTitle())
                .description(realBook.getDescription())
                .author(authorDto)
                .build();

        //when
        BookEntity mappedBook = underTest.mapFrom(bookDto);

        //then
        assertThat(mappedBook).isEqualTo(realBook);
    }
}