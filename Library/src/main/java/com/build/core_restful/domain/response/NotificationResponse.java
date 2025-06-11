package com.build.core_restful.domain.response;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String description;
    private String email;
    private Instant createAt;
    private Instant updateAt;
}
