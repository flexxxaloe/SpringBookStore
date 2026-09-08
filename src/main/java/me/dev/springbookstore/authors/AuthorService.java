package me.dev.springbookstore.authors;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.dev.springbookstore.authors.dto.AuthorCreateRequest;
import me.dev.springbookstore.authors.dto.AuthorResponse;
import me.dev.springbookstore.books.BookMapper;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.books.dto.BookResponse;
import me.dev.springbookstore.web.exceptions.AuthorAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookStoreRepository bookStoreRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;


    public Page<BookResponse> getBooksOfAuthor(Long authorId, Pageable pageable) {
        //findAllByAuthorId(Long authorId, Pageable pageable)
        //findAllByAuthorId(Long authorId)

        //еще через Jpa Author author = authorRepository.findById(id).orElseThrow();
        // return author.getBooks();

        authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id " + authorId + " not found"));

        return bookStoreRepository.findByAuthorId(authorId, pageable)
                .map(bookMapper::bookEntityToBookResponse);
    }

    public AuthorResponse getAuthor(Long authorId) {
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id " + authorId + " not found"));
        return authorMapper.authorEntityToAuthorResponse(author);

    }

    @Transactional
    public AuthorResponse addAuthor(AuthorCreateRequest request) {
        if(authorRepository
                .existsByNameAndBornDate(
                        request.name(),
                        request.bornDate())) {
            throw new AuthorAlreadyExistsException(
                    "Author with name '" + request.name()
                            + "' and born date '" + request.bornDate()
                            + "' already exists"
            );

        }

        AuthorEntity entity = authorMapper.authorRequestToAuthorEntity(request);

        var savedEntity = authorRepository.save(entity);

        return authorMapper.authorEntityToAuthorResponse(savedEntity);

    }

    @Transactional
    public void deleteAuthor(Long authorId) {
        var author = authorRepository.findById(authorId).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + authorId + " not found"));

        authorRepository.delete(author);
    }
}
