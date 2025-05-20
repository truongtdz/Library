package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "author.name", target = "authorName")
    BookResponse toBookResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "carts", ignore = true)
    Book toBook(BookRequest bookRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateBook(@MappingTarget Book book, BookRequest bookRequest);
}
