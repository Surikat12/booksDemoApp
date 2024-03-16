package com.surikat.booksDemoApp.services.Impl;

import com.surikat.booksDemoApp.TestDataUtil;
import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import com.surikat.booksDemoApp.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTests {

    @Mock
    private AuthorRepository authorRepository;
    private AuthorServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthorServiceImpl(authorRepository);
    }

    @Test
    void testThatCreateAuthorWithoutIdCreatesCorrectAuthor() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        AuthorEntity expectedAuthor = TestDataUtil.createTestAuthorA();
        expectedAuthor.setId(1L);
        given(authorRepository.save(any(AuthorEntity.class))).willReturn(expectedAuthor);

        //when
        AuthorEntity returnedAuthor = underTest.create(author);

        //then
        ArgumentCaptor<AuthorEntity> authorArgumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(authorRepository, times(1)).save(authorArgumentCaptor.capture());
        AuthorEntity capturedAuthor = authorArgumentCaptor.getValue();

        assertThat(capturedAuthor).usingRecursiveComparison().isEqualTo(author);
        assertThat(returnedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void testThatCreateAuthorWithIdCreatesCorrectAuthor() {
        //given
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(10L);

        AuthorEntity expectedCapturedAuthor = TestDataUtil.createTestAuthorA();

        AuthorEntity expectedReturnedAuthor = TestDataUtil.createTestAuthorA();
        expectedReturnedAuthor.setId(1L);
        given(authorRepository.save(any(AuthorEntity.class))).willReturn(expectedReturnedAuthor);

        //when
        AuthorEntity returnedAuthor = underTest.create(author);

        //then
        ArgumentCaptor<AuthorEntity> authorArgumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(authorRepository, times(1)).save(authorArgumentCaptor.capture());
        AuthorEntity capturedAuthor = authorArgumentCaptor.getValue();

        assertThat(capturedAuthor).usingRecursiveComparison().isEqualTo(expectedCapturedAuthor);
        assertThat(returnedAuthor).usingRecursiveComparison().isEqualTo(expectedReturnedAuthor);
    }

    @Test
    void testThatFindAllReturnsListOfAllAuthorsWhenTheyExist() {
        //given
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(1L);
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        authorB.setId(2L);
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();
        authorC.setId(3L);
        List<AuthorEntity> savedAuthors = new ArrayList<>();
        Collections.addAll(savedAuthors, authorA, authorB, authorC);

        given(authorRepository.findAll()).willReturn(savedAuthors);

        //when
        List<AuthorEntity> receivedAuthors = underTest.findAll();

        //then
        verify(authorRepository, times(1)).findAll();
        assertThat(receivedAuthors).asList().containsExactlyInAnyOrderElementsOf(savedAuthors);
    }

    @Test
    void testThatFindAllReturnsEmptyListWhenAuthorsDoNotExist() {
        //given
        List<AuthorEntity> savedAuthors = new ArrayList<>();
        given(authorRepository.findAll()).willReturn(savedAuthors);

        //when
        List<AuthorEntity> receivedAuthors = underTest.findAll();

        //then
        verify(authorRepository, times(1)).findAll();
        assertThat(receivedAuthors).asList().isEmpty();
    }

    @Test
    void testThatFindByIdReturnsCorrectAuthorWhenAuthorExist() {
        //given
        long id = 1L;
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(id);

        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));

        //when
        Optional<AuthorEntity> receivedAuthor = underTest.findById(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(receivedAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(author);
    }

    @Test
    void testThatFindByIdReturnsEmptyOptionalWhenAuthorDoesNotExist() {
        //given
        long id = 1L;
        given(authorRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Optional<AuthorEntity> receivedAuthor = underTest.findById(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(receivedAuthor).isEmpty();
    }

    @Test
    void testThatIsExistsReturnsTrueIfAuthorExists() {
        //given
        long id = 1L;
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(id);

        given(authorRepository.existsById(anyLong())).willReturn(true);

        //when
        boolean receivedExists = underTest.isExists(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).existsById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(receivedExists).isTrue();
    }

    @Test
    void testThatIsExistsReturnsFalseIfAuthorDoesNotExists() {
        //given
        long id = 1L;
        given(authorRepository.existsById(anyLong())).willReturn(false);

        //when
        boolean receivedExists = underTest.isExists(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).existsById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(receivedExists).isFalse();
    }

    @Test
    void testThatFullUpdateUpdatesAuthor() {
        //given
        long updatedAuthorId = 1L;
        long differentId = 2L;

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(differentId);
        AuthorEntity expectedAuthor = TestDataUtil.createTestAuthorA();
        expectedAuthor.setId(updatedAuthorId);

        given(authorRepository.save(any(AuthorEntity.class))).willReturn(expectedAuthor);

        //when
        AuthorEntity returnedAuthor = underTest.fullUpdate(updatedAuthorId, author);

        //then
        ArgumentCaptor<AuthorEntity> authorArgumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(authorRepository, times(1)).save(authorArgumentCaptor.capture());
        AuthorEntity capturedAuthor = authorArgumentCaptor.getValue();

        assertThat(capturedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(returnedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void testThatPartialUpdateUpdatesNameOfAuthor() {
        //given
        long updatedAuthorId = 1L;
        long differentId = 2L;

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(updatedAuthorId);

        AuthorEntity givenAuthor = TestDataUtil.createTestAuthorA();
        givenAuthor.setId(differentId);
        givenAuthor.setName("UPDATED");

        AuthorEntity expectedAuthor = TestDataUtil.createTestAuthorA();
        expectedAuthor.setId(updatedAuthorId);
        expectedAuthor.setName("UPDATED");

        given(authorRepository.save(any(AuthorEntity.class))).willReturn(expectedAuthor);
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));

        //when
        AuthorEntity returnedAuthor = underTest.partialUpdate(updatedAuthorId, givenAuthor);

        //then
        ArgumentCaptor<AuthorEntity> authorArgumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(authorRepository, times(1)).save(authorArgumentCaptor.capture());
        AuthorEntity capturedAuthor = authorArgumentCaptor.getValue();

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(updatedAuthorId);
        assertThat(capturedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(returnedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void testThatPartialUpdateUpdatesBirthdateOfAuthor() {
        //given
        long updatedAuthorId = 1L;
        long differentId = 2L;

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(updatedAuthorId);

        AuthorEntity givenAuthor = TestDataUtil.createTestAuthorA();
        givenAuthor.setId(differentId);
        givenAuthor.setBirthdate(author.getBirthdate().plusYears(10));

        AuthorEntity expectedAuthor = TestDataUtil.createTestAuthorA();
        expectedAuthor.setId(updatedAuthorId);
        expectedAuthor.setBirthdate(author.getBirthdate().plusYears(10));

        given(authorRepository.save(any(AuthorEntity.class))).willReturn(expectedAuthor);
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(author));

        //when
        AuthorEntity returnedAuthor = underTest.partialUpdate(updatedAuthorId, givenAuthor);

        //then
        ArgumentCaptor<AuthorEntity> authorArgumentCaptor = ArgumentCaptor.forClass(AuthorEntity.class);
        verify(authorRepository, times(1)).save(authorArgumentCaptor.capture());
        AuthorEntity capturedAuthor = authorArgumentCaptor.getValue();

        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).findById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(updatedAuthorId);
        assertThat(capturedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(returnedAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void testThatDeleteDeletesAuthor() {
        //given
        long id = 1L;

        //when
        underTest.delete(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorRepository, times(1)).deleteById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
    }
}