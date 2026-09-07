package me.dev.springbookstore.authors;


import me.dev.springbookstore.authors.dto.AuthorCreateRequest;
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
}
