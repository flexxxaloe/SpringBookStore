package me.dev.springbookstore.books;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Year;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import me.dev.springbookstore.authors.AuthorEntity;

@Table(name = "books")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "publication", nullable = false)
    private LocalDate publicationDate;

    private Year writtenYear;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private @PositiveOrZero Long price;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;

    @Version
    private Long version;


    @PrePersist // перед сохранением
    @PreUpdate // перед обновлением
    public void updateStatus() {
        if (amount == 0) {
            bookStatus = BookStatus.OUT_OF_STOCK;
        } else {
            bookStatus = BookStatus.IN_STOCK;
        }
    }

}
