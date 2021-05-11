package com.ram.server.controller.api;

import com.ram.server.controller.CrudContorller;
import com.ram.server.model.entity.Post;
import com.ram.server.model.network.request.PostApiRequest;
import com.ram.server.model.network.response.PostApiResponse;
import com.ram.server.service.PostApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j///디버깅시 로그 확인 가능
@RestController
@RequestMapping("/api/user")
public class PostApiController extends CrudContorller<PostApiRequest, PostApiResponse, Post> {
    @Autowired
    PostApiLogicService postApiLogicService;
}
