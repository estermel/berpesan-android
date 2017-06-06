package com.berpesan.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;

import com.berpesan.AnalyticsApplication;
import com.berpesan.ConnectivityReceiver;
import com.berpesan.R;
import com.berpesan.adapter.Data;
import com.berpesan.model.TrendingSpam;
import com.berpesan.model.JSONResponse;
import com.berpesan.rest.RequestInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.berpesan.rest.RequestInterface.retrofit;

public class TrendingActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<TrendingSpam> data;
    private Data adapter;
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        initViews();
    }

    @Override
    protected void onResume(){
        super.onResume();

        AnalyticsApplication.getInstance().setConnectivityListener(this);
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(TrendingActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Berpesan");
        progressDialog.setMessage("Mengambil data...");
        progressDialog.show();
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        getTrending(isConnected);
    }

    private void getTrending(boolean isConnected) {
        if(isConnected){
            loadJSON();
        } else {
            progressDialog.dismiss();
            setContentView(R.layout.no_internet_layout);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            initNavigationDrawer();
        }
    }

    private void loadJSON(){
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getJSON()));
                adapter = new Data(data, context);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t){
                progressDialog.dismiss();
                Log.e("LOG ERROR", t.getMessage());
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.trending_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.inbox:
                        drawerLayout.closeDrawers();
                        Intent inbox = new Intent(TrendingActivity.this, MainActivity.class);
                        startActivity(inbox);
                        break;
                    case R.id.outbox:
                        drawerLayout.closeDrawers();
                        Intent outbox = new Intent(TrendingActivity.this, SentBox.class);
                        startActivity(outbox);
                        break;
                    case R.id.trending:
                        drawerLayout.closeDrawers();
                        Intent trending = new Intent(TrendingActivity.this, TrendingActivity.class);
                        startActivity(trending);
                        break;
                    case R.id.help:
                        drawerLayout.closeDrawers();
                        Intent help = new Intent(TrendingActivity.this, HelpActivity.class);
                        startActivity(help);
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        getTrending(isConnected);
    }
}
