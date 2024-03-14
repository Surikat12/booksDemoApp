package com.surikat.booksDemoApp.mappers.Impl;

import com.surikat.booksDemoApp.TestDataUtil;
import com.surikat.booksDemoApp.domain.dto.AuthorDto;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModelMapper.class})
class AuthorMapperImplTests {

    private final AuthorMapperImpl underTest;

    @Autowired
    public AuthorMapperImplTests(ModelMapper modelMapper) {
        this.underTest = new AuthorMapperImpl(modelMapper);
    }

    @Test
    void testThatMapToCorrectlyMapAuthorWithAllPropertiesToAuthorDto() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(1L);

        //when
        AuthorDto authorDto = underTest.mapTo(author);

        //then
        assertThat(authorDto.getId()).isEqualTo(author.getId());
        assertThat(authorDto.getName()).isEqualTo(author.getName());
        assertThat(authorDto.getBirthdate()).isEqualTo(author.getBirthdate());
    }

    @Test
    void testThatMapToCorrectlyMapAuthorWithAllPropertiesExceptIdToAuthorDto() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        //when
        AuthorDto authorDto = underTest.mapTo(author);

        //then
        assertThat(authorDto.getId()).isNull();
        assertThat(authorDto.getName()).isEqualTo(author.getName());
        assertThat(authorDto.getBirthdate()).isEqualTo(author.getBirthdate());
    }

    @Test
    void testThatMapFromCorrectlyMapAuthorDtoWithAllPropertiesToAuthor() {
        //given
        AuthorEntity realAuthor = TestDataUtil.createTestAuthorA();
        realAuthor.setId(1L);

        AuthorDto authorDto = AuthorDto.builder()
                .id(realAuthor.getId())
                .name(realAuthor.getName())
                .birthdate(realAuthor.getBirthdate())
                .build();

        //when
        AuthorEntity mappedAuthor = underTest.mapFrom(authorDto);

        //then
        assertThat(mappedAuthor).isEqualTo(realAuthor);
    }

    @Test
    void testThatMapFromCorrectlyMapAuthorDtoWithAllPropertiesExceptIdToAuthor() {
        //given
        AuthorEntity realAuthor = TestDataUtil.createTestAuthorA();
        AuthorDto authorDto = AuthorDto.builder()
                .name(realAuthor.getName())
                .birthdate(realAuthor.getBirthdate())
                .build();

        //when
        AuthorEntity mappedAuthor = underTest.mapFrom(authorDto);

        //then
        assertThat(mappedAuthor).isEqualTo(realAuthor);
    }
}