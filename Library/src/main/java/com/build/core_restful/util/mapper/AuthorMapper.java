package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.response.AuthorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper{

    Author toAuthor(AuthorRequest authorRequest);

    AuthorResponse toAuthorResponse(Author author);

    @Mapping(target = "id", ignore = true)
    void updateAuthor(@MappingTarget Author author, AuthorRequest authorRequest);
}
