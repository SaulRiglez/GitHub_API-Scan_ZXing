package com.yoprogramo.github_app.presenter;

import com.yoprogramo.github_app.entities.User;
import com.yoprogramo.github_app.utilities.RetrofitHelper;
import com.yoprogramo.github_app.view.Iview;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 5/16/2017.
 */

public class MainPresenterImp implements IPresenter.iMainPresenter {

    Iview.IMainView iMainView;
    User usuario;



    public MainPresenterImp(Iview.IMainView iMainView) {
        this.iMainView = iMainView;
    }

    @Override
    public void onButtonClicked(String user) {

        iMainView.goToRepositoriesActivity(user);

    }

    @Override
    public void getDetailUser(String user) {
        Call<User> resultingUser = RetrofitHelper.Factory.createUser(user);
        resultingUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                usuario = response.body();
                EventBus.getDefault().post(usuario);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


}
