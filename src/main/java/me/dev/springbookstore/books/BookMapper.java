package me.dev.springbookstore.books;


import jakarta.persistence.EntityNotFoundException;
import me.dev.springbookstore.authors.AuthorEntity;
import me.dev.springbookstore.authors.AuthorRepository;
import me.dev.springbookstore.books.dto.BookCreateRequest;
import me.dev.springbookstore.books.dto.BookPatchRequest;
import me.dev.springbookstore.books.dto.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private final AuthorRepository authorRepository;

    public BookMapper(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public BookEntity bookRequestToBookEntity(BookCreateRequest request,
                                              AuthorEntity author) {

        BookEntity entity = new BookEntity();

        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setPublicationDate(request.publicationDate());
        entity.setWrittenYear(request.writtenYear());
        entity.setAmount(request.amount());
        entity.setPrice(request.price());
        entity.setAuthor(author);

        return entity;
    }

    public BookResponse bookEntityToBookResponse(BookEntity entity) {
        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor().getId(),
                entity.getAuthor().getName(),
                entity.getDescription(),
                entity.getPublicationDate(),
                entity.getWrittenYear(),
                entity.getAmount(),
                entity.getPrice(),
                entity.getBookStatus()
        );
    }

    public void patchBookEntity(BookEntity entity, BookPatchRequest request) {

        if (request.authorId() != null) {
            AuthorEntity author = authorRepository.findById(request.authorId())
                    .orElseThrow(() -> new EntityNotFoundException("Author not found"));

            entity.setAuthor(author);
        }

        if (request.title() != null) {
            entity.setTitle(request.title());
        }

        if (request.description() != null) {
            entity.setDescription(request.description());
        }

        if (request.publicationDate() != null) {
            entity.setPublicationDate(request.publicationDate());
        }

        if (request.writtenYear() != null) {
            entity.setWrittenYear(request.writtenYear());
        }

        if (request.amount() != null) {
            entity.setAmount(request.amount());
        }

        if (request.price() != null) {
            entity.setPrice(request.price());
        }
    }

    public BookEntity updateBookEntity(BookEntity entity,
                                        BookCreateRequest request) {

        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setPublicationDate(request.publicationDate());
        entity.setWrittenYear(request.writtenYear());
        entity.setAmount(request.amount());
        entity.setPrice(request.price());

        return entity;
    }

}
