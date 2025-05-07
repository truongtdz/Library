package com.build.core_restful.domain.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private String keyword;
    private PageResponse<Object> result;
}
