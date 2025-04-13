package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Authors;
import com.build.core_restful.domain.request.AuthorsRequest;
import com.build.core_restful.domain.response.AuthorsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorsMapper {

    Authors toAuthor(AuthorsRequest authorRequest);

    AuthorsResponse toAuthorResponse(Authors authors);

    @Mapping(target = "id", ignore = true)
    void updateAuthor(@MappingTarget Authors authors, AuthorsRequest authorRequest);
}
