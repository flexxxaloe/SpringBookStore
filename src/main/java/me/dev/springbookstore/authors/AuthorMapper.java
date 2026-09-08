package me.dev.springbookstore.authors;


import me.dev.springbookstore.authors.dto.AuthorCreateRequest;
import me.dev.springbookstore.authors.dto.AuthorPatchRequest;
import me.dev.springbookstore.authors.dto.AuthorResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorResponse authorEntityToAuthorResponse(AuthorEntity entity) {
        return new AuthorResponse(
                entity.getId(),
                entity.getName(),
                entity.getBio(),
                entity.getBornDate()
        );
    }

    public AuthorEntity authorRequestToAuthorEntity(AuthorCreateRequest request) {
        AuthorEntity entity = new AuthorEntity();

        entity.setName(request.name());
        entity.setBio(request.bio());
        entity.setBornDate(request.bornDate());

        return entity;
    }

    public AuthorEntity updateAuthorEntity(AuthorEntity entityAuthor, AuthorCreateRequest request) {
        entityAuthor.setName(request.name());
        entityAuthor.setBio(request.bio());
        entityAuthor.setBornDate(request.bornDate());

        return entityAuthor;
    }

    public void patchAuthorEntity(AuthorEntity entityAuthor, AuthorPatchRequest request) {
        if (request.name() != null) {
            entityAuthor.setName(request.name());
        }
        // Update only fields explicitly provided in the PATCH request.
        if (request.bio() != null) {
            entityAuthor.setBio(request.bio());
        }
        if (request.bornDate() != null) {
            entityAuthor.setBornDate(request.bornDate());
        }

    }
}
