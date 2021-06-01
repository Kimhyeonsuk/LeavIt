package com.example.login.network.responseDto;

import com.example.login.network.enumclass.PostStatus;

import java.math.BigDecimal;

public class PostResult {
    private Long id;

    private String title;

    private String content;

    private PostStatus status;

    private BigDecimal price;

    private Long userId;
}
