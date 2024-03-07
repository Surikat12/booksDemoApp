package com.surikat.booksDemoApp.services;

import com.surikat.booksDemoApp.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorEntity create(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Page<AuthorEntity> findAll(Pageable pageable);

    Optional<AuthorEntity> findById(Long id);

    boolean isExists(Long id);

    AuthorEntity fullUpdate(Long id, AuthorEntity author);

    AuthorEntity partialUpdate(Long id, AuthorEntity author);

    void delete(Long id);
}
