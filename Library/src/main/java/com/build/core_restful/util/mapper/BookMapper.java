package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Review;
import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;

import java.util.ArrayList;
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
        List<BookResponse.ImageRes> imageRes = book.getImages().stream()
            .map(image -> BookResponse.ImageRes.builder()
                .isDefault(image.getIsDefault())
                .url(image.getUrl())
                .build())
            .toList();
        response.imageList(imageRes);
        }

        if (book.getReviews() != null) {
            List<Review> parentReviews = book.getReviews().stream()
                .filter(review -> review.getParentReview().getId() == 0L)
                .toList();

            double averageRate = 0.0;
            if (!parentReviews.isEmpty()) {
                averageRate = parentReviews.stream()
                    .mapToInt(Review::getRate)
                    .average()
                    .orElse(5.0);
            }

            List<BookResponse.ReviewRes> reviewRes = new ArrayList<>();

            for(Review review : parentReviews){
                BookResponse.ReviewRes item = BookResponse.ReviewRes.builder()
                                                    .id(review.getId())
                                                    .rate(review.getRate())
                                                    .build();
                reviewRes.add(item);
            }
            response.reviews(reviewRes);
            response.averageRate(averageRate);
        }
    }
    
    Book toBook(BookRequest bookRequest);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBook(@MappingTarget Book book, BookRequest bookRequest);
}
