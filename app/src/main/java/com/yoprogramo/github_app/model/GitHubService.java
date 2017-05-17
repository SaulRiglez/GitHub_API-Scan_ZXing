package com.yoprogramo.github_app.model;

import com.yoprogramo.github_app.entities.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by User on 5/16/2017.
 */

public interface GitHubService {
    @GET("users/{user}")
    Call<User> getUser(@Path("user") String user);
}
