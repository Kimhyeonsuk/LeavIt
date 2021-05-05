package com.ram.server.controller;

import com.ram.server.model.SearchParam;
import com.ram.server.model.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")//localhost:8080/api
public class GetController {
    @RequestMapping(method= RequestMethod.GET,path="/getMethod")
    public String getRequest(){return "Hi getMethod";}
    @GetMapping("/getParameter")
    public String getParameter(@RequestParam String id,@RequestParam(name="password") String password){
        System.out.println("id : "+id);
        System.out.println("password : "+password);

        return id+password;
    }

    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParameter(SearchParam searchParam){
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());
        return searchParam;
    }

    @GetMapping("/header")
    public Header getHeader()
    {
        return Header.builder().resultCode("OK").description("OK").build();
    }
}
