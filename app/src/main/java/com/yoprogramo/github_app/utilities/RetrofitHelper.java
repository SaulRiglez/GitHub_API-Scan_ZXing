package com.yoprogramo.github_app.utilities;


import com.yoprogramo.github_app.entities.RepoUser;
import com.yoprogramo.github_app.entities.User;
import com.yoprogramo.github_app.model.GitHubService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by User on 5/16/2017.
 */

public class RetrofitHelper {

    public static class Factory{

        static Retrofit retrofit = create();
        static GitHubService service = retrofit.create(GitHubService.class);


        private static Retrofit create() {
            return new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
        }

        public static Call<User> createUser(String username){

            return service.getUser(username);
        }


        public static Observable<List<RepoUser>> createRepoDetailObservable(String nickname)
        {

            return service.getRepositories(nickname);

        }

    }
}
