package com.example.login.ifs;

import com.example.login.network.responseDto.PostResult;
import com.example.login.network.responseDto.UserResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitService {

    // @Get(EndPoint- μμμμΉ(URI))
    @GET("users/{id}")
    Call<UserResult> getUsers(@Path("user") String user);

}
