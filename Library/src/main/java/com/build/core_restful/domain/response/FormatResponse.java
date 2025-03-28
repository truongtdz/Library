package com.build.core_restful.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormatResponse<T> {
    private int code;
    private Object message;
    private String error;
    private T data;
}
