package com.surikat.booksDemoApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surikat.booksDemoApp.TestDataUtil;
import com.surikat.booksDemoApp.domain.dto.AuthorDto;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.mappers.Mapper;
import com.surikat.booksDemoApp.services.AuthorService;
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
public class AuthorControllerIntegrationTests {

    private final String apiPath = "/authors";

    private final AuthorService authorService;
    private final MockMvc mockMvc;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(AuthorService authorService, MockMvc mockMvc,
                                            Mapper<AuthorEntity, AuthorDto> authorMapper,
                                            ObjectMapper objectMapper) {
        this.authorService = authorService;
        this.mockMvc = mockMvc;
        this.authorMapper = authorMapper;
        this.objectMapper = objectMapper;
    }

    @Test
    void testThatCreateAuthorSuccessfullyReturnsHttpStatus201() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);

        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);

        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorA.getAge())
        );
    }

    @Test
    void testThatGetAllAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatGetAllAuthorsReturnsListOfAllAuthorsWhenTheyExist() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB = authorService.create(authorB);
        AuthorDto authorDtoB = authorMapper.mapTo(authorB);

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
                MockMvcResultMatchers.jsonPath("$.content[0]").value(authorDtoA)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1]").value(authorDtoB)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[2]").doesNotExist()
        );
    }

    @Test
    void testThatGetAllAuthorsReturnsEmptyListWhenAuthorsDoNotExist() throws Exception {
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
    void testThatGetAuthorByIdReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatGetAuthorByIdReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").value(authorDtoA)
        );
    }

    @Test
    void testThatGetAuthorByIdReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatFullUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB.setId(authorA.getId() + 1);
        AuthorDto authorDtoB = authorMapper.mapTo(authorB);
        String updatedAuthorJson = objectMapper.writeValueAsString(authorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorB.getAge())
        );
    }

    @Test
    void testThatFullUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatPartialUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testThatPartialUpdateNameOfAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        AuthorEntity authorB = AuthorEntity.builder()
                .id(authorA.getId() + 1)
                .name("UPDATED")
                .build();

        AuthorDto authorDtoB = authorMapper.mapTo(authorB);
        String updatedAuthorJson = objectMapper.writeValueAsString(authorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorA.getAge())
        );
    }

    @Test
    void testThatPartialUpdateAgeOfAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        AuthorEntity authorB = AuthorEntity.builder()
                .id(authorA.getId() + 1)
                .age(authorA.getAge() + 1)
                .build();

        AuthorDto authorDtoB = authorMapper.mapTo(authorB);
        String updatedAuthorJson = objectMapper.writeValueAsString(authorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorB.getAge())
        );
    }

    @Test
    void testThatPartialUpdateNameAndAgeOfAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        AuthorEntity authorB = AuthorEntity.builder()
                .id(authorA.getId() + 1)
                .name("UPDATED")
                .age(authorA.getAge() + 1)
                .build();

        AuthorDto authorDtoB = authorMapper.mapTo(authorB);
        String updatedAuthorJson = objectMapper.writeValueAsString(authorDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorB.getAge())
        );
    }

    @Test
    void testThatPartialUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorDto authorDtoA = authorMapper.mapTo(authorA);
        String authorJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch(apiPath + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testThatDeleteReturnsHttpStatus204WhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    void testThatDeleteAuthorDeleted() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA = authorService.create(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(apiPath + "/" + authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(apiPath + "/" + authorA.getId())
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

