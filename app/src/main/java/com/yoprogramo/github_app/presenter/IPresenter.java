package com.yoprogramo.github_app.presenter;

import com.yoprogramo.github_app.entities.RepoUser;

import java.util.List;

/**
 * Created by User on 5/16/2017.
 */

public interface IPresenter {

    public interface iMainPresenter{
        void onButtonClicked(String user);
        void getDetailUser(String user);
    }

    public interface IFolloPresenter{

        void getRepositories(List<RepoUser> repoUserList, String user);

    }
}
