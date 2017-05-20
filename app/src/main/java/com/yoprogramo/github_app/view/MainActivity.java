package com.yoprogramo.github_app.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
    private String cadena;
    IPresenter.iMainPresenter iMainPresenter;
    private static final int MY_PERMISSION_USE_CAMERA_REQUEST_CODE = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPermissions();
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

                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            Log.d("scan", "onActivityResult: " + scanContent + "Scan Fromat" + scanFormat);

            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("ScanCode: " + scanContent + ", ScanFormat: " + scanFormat)
                    .setTitle("Scanned Code");
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    /**
     * App Permissions for Camera USe
     **/
    private void setupPermissions() {
        // If we don't have the record audio permission...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{Manifest.permission.CAMERA};
                requestPermissions(permissionsWeNeed, MY_PERMISSION_USE_CAMERA_REQUEST_CODE);
            }
        } else {
            // Otherwise, permissions were granted and we are ready to go!
            //mAudioInputReader = new AudioInputReader(mVisualizerView, this);
            Toast.makeText(this, "Permission for camera granted! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_USE_CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission was granted! Start up the visualizer!
                    Toast.makeText(this, "Permission for camera granted! ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission for Camera not granted. Visualizer can't run.", Toast.LENGTH_LONG).show();
                    finish();
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
            // Other permissions could go down here
        }
    }


}
