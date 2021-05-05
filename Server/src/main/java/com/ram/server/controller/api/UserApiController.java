package com.ram.server.controller.api;

import com.ram.server.controller.CrudContorller;
import com.ram.server.model.entity.User;
import com.ram.server.model.network.Header;
import com.ram.server.model.network.request.UserApiRequest;
import com.ram.server.model.network.response.UserApiResponse;
import com.ram.server.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j///디버깅시 로그 확인 가능
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudContorller<UserApiRequest, UserApiResponse, User> {
    @Autowired
    UserApiLogicService userApiLogicService;

    /*@GetMapping("")
    public Header<List<UserApiResponse>> search(@PageableDefault(sort="id",direction = Sort.Direction.ASC,size=15)Pageable pageable){
        //return userApiLogicService.search(pageable);
    }*/

}
