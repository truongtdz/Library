package com.build.core_restful.util.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.build.core_restful.domain.Review;
import com.build.core_restful.domain.request.ReviewRequest;
import com.build.core_restful.domain.response.ReviewResponse;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "book.id", target = "bookId")
    ReviewResponse toReviewResponse(Review review);

    @AfterMapping
    default void mapNestedFields(Review review, @MappingTarget ReviewResponse.ReviewResponseBuilder response) {
        if (review.getUser() != null) {
            response.user(ReviewResponse.UserRes.builder()
                    .userId(review.getUser().getId())
                    .fullName(review.getUser().getFullName())
                    .build());
        }
    }

    Review toReview(ReviewRequest ReviewRequest);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReview(@MappingTarget Review Review, ReviewRequest ReviewRequest);
}
