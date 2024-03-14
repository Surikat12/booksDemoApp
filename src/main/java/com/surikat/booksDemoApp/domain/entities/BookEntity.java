package com.surikat.booksDemoApp.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    private Long id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(nullable = false, length = 512)
    private String description;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false, name = "author_id")
    private AuthorEntity author;

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
        BookEntity other = (BookEntity) obj;
        if (id == null) {
            return false;
        } else {
            return id.equals(other.id);
        }
    }
}
