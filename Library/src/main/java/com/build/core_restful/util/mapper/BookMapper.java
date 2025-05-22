package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "imageList", ignore = true)
    BookResponse toBookResponse(Book book);

    @AfterMapping
    default void mapNestedFields(Book book, @MappingTarget BookResponse.BookResponseBuilder response) {
        if (book.getCategory() != null) {
            response.category(BookResponse.CategoryRes.builder()
                    .id(book.getCategory().getId())
                    .name(book.getCategory().getName())
                    .build());
        }
        if (book.getAuthor() != null) {
            response.author(BookResponse.AuthorRes.builder()
                    .id(book.getAuthor().getId())
                    .name(book.getAuthor().getName())
                    .build());
        }
        if (book.getImages() != null) {
        List<BookResponse.ImageRes> imageResponses = book.getImages().stream()
            .map(image -> BookResponse.ImageRes.builder()
                .isDefault(image.getIsDefault())
                .url(image.getUrl())
                .build())
            .toList();
        response.imageList(imageResponses);
        }
    }

    Book toBook(BookRequest bookRequest);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBook(@MappingTarget Book book, BookRequest bookRequest);
}
