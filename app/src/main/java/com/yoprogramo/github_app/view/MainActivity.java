package com.yoprogramo.github_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yoprogramo.github_app.R;
import com.yoprogramo.github_app.entities.User;
import com.yoprogramo.github_app.presenter.IPresenter;
import com.yoprogramo.github_app.presenter.MainPresenterImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements Iview.IMainView {

    TextView tvName;
    ImageView imageViewUser;
    TextView tvFollowers;
    Button btnFollowers;
    private String user;
    IPresenter.iMainPresenter iMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iMainPresenter = new MainPresenterImp(this);
        initComponent();
        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_scand);

        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                user = query;
                iMainPresenter.getDetailUser(user);
                MainActivity.this.setTitle(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void obtainUserInfo(User person) {
        tvName.setText(person.getName());
        tvFollowers.setText(person.getFollowers().toString());
        String uri = person.getAvatarUrl();
        insertUserImage(uri);
    }

    private void insertUserImage(String uri) {
        Picasso.with(this).load(uri).into(imageViewUser, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
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

    private void initComponent() {
        tvName = ((TextView) findViewById(R.id.textview_user));
        imageViewUser = ((ImageView) findViewById(R.id.imageview_user));
        tvFollowers = ((TextView) findViewById(R.id.textview_followers));
    }

    public void getRepositories(View view) {
        iMainPresenter.onButtonClicked(user);
    }

    @Override
    public void goToRepositoriesActivity(String user) {
        Intent intent = new Intent(this, FolloActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
