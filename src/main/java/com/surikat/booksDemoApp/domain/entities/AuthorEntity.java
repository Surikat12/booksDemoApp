package com.surikat.booksDemoApp.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthorEntity other = (AuthorEntity) obj;
        if (id == null) {
            return false;
        } else {
            return id.equals(other.id);
        }
    }
}
