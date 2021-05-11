package com.ram.server.service;

import com.ram.server.model.entity.Post;
import com.ram.server.model.network.Header;
import com.ram.server.model.network.request.PostApiRequest;
import com.ram.server.model.network.response.PostApiResponse;
import com.ram.server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PostApiLogicService extends BaseService<PostApiRequest, PostApiResponse, Post> {
    @Autowired
    private PostRepository postRepository;
    @Override
    public Header<PostApiResponse> create(Header<PostApiRequest> request) {
        return null;
    }

    @Override
    public Header<PostApiResponse> read(Long id) {
        return null;
    }

    @Override
    public Header<PostApiResponse> update(Header<PostApiRequest> request) {
        return null;
    }

    @Override
    public Header<PostApiResponse> delete(Long id) {
        return null;
    }
}
