package com.yoprogramo.github_app.presenter;

import android.util.Log;

import com.yoprogramo.github_app.entities.RepoUser;
import com.yoprogramo.github_app.utilities.RetrofitHelper;
import com.yoprogramo.github_app.view.Iview;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by User on 5/17/2017.
 */

public class FolloPresenterImp implements IPresenter.IFolloPresenter{

    List<RepoUser>  repoList = new ArrayList<RepoUser>();
    Iview.IFolloView iFolloView;

    public FolloPresenterImp(Iview.IFolloView iFolloView) {
        this.iFolloView = iFolloView;
    }

    @Override
    public void getRepositories(List<RepoUser> repoUserList, String user) {


        Observable<List<RepoUser>> resultObservableRepos = RetrofitHelper.Factory.createRepoDetailObservable(user);
        Observer observerRepos = new Observer<List<RepoUser>>() {
            @Override
            public void onCompleted() {
               // repoAdapter.notifyDataSetChanged();
                Log.d("onComple", "onCompleted: ");

            }

            @Override
            public void onError(Throwable e) {

                Log.d("onError", "onError: " + e.getMessage());

            }

            @Override
            public void onNext(List<RepoUser> repoUsers) {
                // repoList = repoUsers;


                for (RepoUser repo : repoUsers) {
                    //repo.setName(repo.getName() + "@ Added");
                    repoUserList.add(repo);
                    Log.d("next", "onNext: "+repo.getName());
                }

                repoList = repoUserList;

                iFolloView.notifyAdapter();
            }

        };

        resultObservableRepos.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .map(new Func1<RepoUser, Object>() {
                    @Override
                    public Object call(RepoUser repoUser) {
                        repoUser.setName(repoUser.getName() + " @ Added");
                        return repoUser;
                    }
                })
                .toList()
                .subscribe(observerRepos);

    }
}
