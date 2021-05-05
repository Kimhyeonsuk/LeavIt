package com.ram.server.controller;

import com.ram.server.model.SearchParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    @PostMapping(value="/postMethod")
    public SearchParam postMethod(@RequestBody SearchParam searchParam){
        return searchParam;
    }
}
