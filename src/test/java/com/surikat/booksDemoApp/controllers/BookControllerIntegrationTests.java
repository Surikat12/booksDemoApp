package com.surikat.booksDemoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surikat.booksDemoApp.TestDataUtil;
import com.surikat.booksDemoApp.domain.dto.AuthorDto;
import com.surikat.booksDemoApp.domain.dto.BookDto;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.domain.entities.BookEntity;
import com.surikat.booksDemoApp.mappers.Mapper;
import com.surikat.booksDemoApp.services.AuthorService;
import com.surikat.booksDemoApp.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private final String apiPath = "/books";

    private final BookService bookService;
    private final AuthorService authorService;
    private final MockMvc mockMvc;
    private final Mapper<BookEntity, BookDto> bookMapper;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(BookService bookService,
                                          AuthorService authorService,
                                          MockMvc mockMvc,
                                          Mapper<BookEntity, BookDto> bookMapper,
                                          Mapper<AuthorEntity, AuthorDto> authorMapper,
                                          ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.mockMvc = mockMvc;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.objectMapper = objectMapper;
    }

    @Test
    void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    void testThatCreateBookWithExistingAuthorReturnsSavedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(authorDtoA)
        );
    }

    @Test
    void testThatGetAllBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatGetAllBooksReturnsListOfAllBooksWhenTheyExist() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);

        BookEntity bookB = TestDataUtil.createTestBookB(authorA);
        bookB = bookService.create(bookB);
        BookDto bookDtoB = bookMapper.mapTo(bookB);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.empty").value(false)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.totalElements").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0]").value(bookDtoA)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1]").value(bookDtoB)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[2]").doesNotExist()
        );
    }

    @Test
    void testThatGetAllBooksReturnsEmptyListWhenBooksDoNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.empty").value(true)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.numberOfElements").value(0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.totalElements").value(0)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0]").doesNotExist()
        );
    }

    @Test
    void testThatGetBookByIdReturnsHttpStatus200WhenBookExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatGetBookByIdReturnsBookWhenBookExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").value(bookDtoA)
        );
    }

    @Test
    void testThatGetBookByIdReturnsHttpStatus404WhenBookDoesNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatFullUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatFullUpdateBookReturnsUpdatedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);

        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB = authorService.create(authorB);

        BookEntity bookB = TestDataUtil.createTestBookB(authorB);
        bookB.setId(bookA.getId() + 1);
        BookDto bookDtoB = bookMapper.mapTo(bookB);
        String updatedBookJson = objectMapper.writeValueAsString(bookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(bookA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookB.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookB.getAuthor())
        );
    }

    @Test
    void testThatFullUpdateBookReturnsHttpStatus404WhenBookDoesNotExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatPartialUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatPartialUpdateTitleOfBookReturnsUpdatedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        BookEntity bookB = BookEntity.builder()
                .id(bookA.getId() + 1)
                .title("UPDATED")
                .build();

        BookDto bookDtoB = bookMapper.mapTo(bookB);
        String updatedBookJson = objectMapper.writeValueAsString(bookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(bookA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(authorDtoA)
        );
    }

    @Test
    void testThatPartialUpdateDescriptionOfBookReturnsUpdatedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        BookEntity bookB = BookEntity.builder()
                .id(bookA.getId() + 1)
                .description("UPDATED")
                .build();

        BookDto bookDtoB = bookMapper.mapTo(bookB);
        String updatedBookJson = objectMapper.writeValueAsString(bookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(bookA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookB.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(authorDtoA)
        );
    }

    @Test
    void testThatPartialUpdateAuthorOfBookReturnsUpdatedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB = authorService.create(authorB);
        AuthorDto authorDtoB = authorMapper.mapTo(authorB);

        BookEntity bookB = BookEntity.builder()
                .id(bookA.getId() + 1)
                .author(authorB)
                .build();

        BookDto bookDtoB = bookMapper.mapTo(bookB);
        String updatedBookJson = objectMapper.writeValueAsString(bookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(bookA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(authorDtoB)
        );
    }

    @Test
    void testThatPartialUpdateTitleAndAuthorOfBookReturnsUpdatedBook() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB = authorService.create(authorB);
        AuthorDto authorDtoB = authorMapper.mapTo(authorB);

        BookEntity bookB = BookEntity.builder()
                .id(bookA.getId() + 1)
                .title("UPDATED")
                .author(authorB)
                .build();

        BookDto bookDtoB = bookMapper.mapTo(bookB);
        String updatedBookJson = objectMapper.writeValueAsString(bookDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(bookA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(bookA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(authorDtoB)
        );
    }

    @Test
    void testThatPartialUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);
        BookDto bookDtoA = bookMapper.mapTo(bookA);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatDeleteReturnsHttpStatus204WhenBookExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    void testThatDeleteAuthorDeleted() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        BookEntity bookA = TestDataUtil.createTestBookA(authorA);
        bookA = bookService.create(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + bookA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatDeleteReturnsHttpStatus204WhenAuthorDoesNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
