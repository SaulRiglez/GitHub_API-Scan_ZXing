package com.yoprogramo.github_app.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yoprogramo.github_app.R;
import com.yoprogramo.github_app.entities.RepoUser;
import com.yoprogramo.github_app.utilities.ReposAdapter;
import com.yoprogramo.github_app.utilities.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FolloActivity extends AppCompatActivity {

    String user;
    int i = 0;

    List<RepoUser> repoList = new ArrayList<RepoUser>();
    ReposAdapter repoAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        Intent intent = getIntent();

        if (intent != null) {
            user = intent.getStringExtra("user");

            Log.d("user", "onCreate: " + user);


        }


        repoAdapter = new ReposAdapter(repoList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repoAdapter);

        Log.d("afuera", "onNext: " + repoList.size());


        Observable<List<RepoUser>> resultObservableRepos = RetrofitHelper.Factory.createRepoDetailObservable(user);
        Observer observerRepos = new Observer<List<RepoUser>>() {
            @Override
            public void onCompleted() {
                repoAdapter.notifyDataSetChanged();
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
                    repoList.add(repo);
                }

                Log.d("onNext", "onNext: " + repoList.size());


                //repoAdapter.notifyDataSetChanged();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_repo, menu);
        MenuItem menuItem = menu.findItem(R.id.action_scand);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_scand:
                Toast.makeText(this, "Scaning", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
