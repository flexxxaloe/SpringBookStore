package me.dev.springbookstore.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookStoreRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByAuthorId(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = "author")
    @Query("SELECT b FROM BookEntity b")
    Page<BookEntity> findAllWithAuthor(Pageable pageable);

    List<BookEntity> findAllByBookStatus(BookStatus bookStatus);

    @EntityGraph(attributePaths = "author")
    @Query("""
    SELECT b FROM BookEntity b
    WHERE (:titlePattern IS NULL OR LOWER(b.title) LIKE :titlePattern)
      AND (:status IS NULL OR b.bookStatus = :status)
      AND (:authorId IS NULL OR b.author.id = :authorId)
    """)
    Page<BookEntity> searchBooks(
            @Param("titlePattern") String titlePattern,
            @Param("authorId") Long authorId,
            @Param("status") BookStatus status,
            Pageable pageable);
}
