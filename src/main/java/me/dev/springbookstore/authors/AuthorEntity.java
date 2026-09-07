package me.dev.springbookstore.authors;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.dev.springbookstore.books.BookEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "authors",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_author_name_born_date", // уникален не каждый столбец отдельно, а именно пара (name, born_date)
                columnNames = {"name", "born_date"}
        ))
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private String bio;

    @Column(name = "born_date", nullable = false)
    private LocalDate bornDate;

    @OneToMany(mappedBy = "author") //, cascade = CascadeType.ALL
    private List<BookEntity> books = new ArrayList<>();

}
