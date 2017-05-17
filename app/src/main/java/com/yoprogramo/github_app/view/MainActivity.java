package com.yoprogramo.github_app.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import com.yoprogramo.github_app.utilities.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvName;
    ImageView imageViewUser;
    TextView tvFollowers;
    Button btnFollowers;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
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

                //iPresenterMainActivity.getDetailObserver(query);

                obtainUserInfo(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void obtainUserInfo(final String user) {

        Call<User> resultingUser = RetrofitHelper.Factory.createUser(user);
        resultingUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("Response", "onResponse: " + response.body().getName());
                tvName.setText(response.body().getName());
                tvFollowers.setText(response.body().getFollowers().toString());
                String uri = response.body().getAvatarUrl();
                insertUserImage(uri);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        MainActivity.this.setTitle(user);
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
    }
}
