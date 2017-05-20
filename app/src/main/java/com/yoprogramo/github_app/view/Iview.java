package com.yoprogramo.github_app.view;

/**
 * Created by User on 5/16/2017.
 */

public interface Iview {

    public interface IMainView{
        void goToRepositoriesActivity(String user);
    }

    public interface IFolloView{
        void notifyAdapter();

    }

}
