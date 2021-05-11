package com.ram.server.model.network.request;

import com.ram.server.model.enumclass.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostApiRequest {
    private Long id;

    private String title;

    private String content;

    private PostStatus status;

    private BigDecimal price;

    private Long userId;
}
