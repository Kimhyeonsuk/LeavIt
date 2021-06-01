package com.ram.server.repository;

import com.ram.server.ServerApplicationTests;
import com.ram.server.model.entity.Post;
import com.ram.server.model.enumclass.PostStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostRepositoryTest extends ServerApplicationTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    public void create(){
        String title="PostTest01";
        String content="안녕하세요 \n 이 제품은 아주 상태가 좋습니다.\n";
        PostStatus status=PostStatus.TRADING;
        BigDecimal price= BigDecimal.valueOf(23000);
        LocalDateTime createdAt=LocalDateTime.now();
        String createdBy="AdminUser";


        Post post=new Post();

    }
}