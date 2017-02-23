package com.berpesan.berpesan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class TrendingActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.trending_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.inbox:
                        Toast.makeText(getApplicationContext(),"Inbox",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent inbox = new Intent(TrendingActivity.this, MainActivity.class);
                        startActivity(inbox);
                        break;
                    case R.id.promosi:
                        Toast.makeText(getApplicationContext(),"Promosi",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent promotion = new Intent(TrendingActivity.this, PromosiActivity.class);
                        startActivity(promotion);
                        break;
                    case R.id.smsBanking:
                        Toast.makeText(getApplicationContext(),"SMS Banking",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent smsBanking = new Intent(TrendingActivity.this, SMSBankingActivity.class);
                        startActivity(smsBanking);
                        break;
                    case R.id.operator:
                        Toast.makeText(getApplicationContext(),"Operator",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent operator = new Intent(TrendingActivity.this, OperatorActivity.class);
                        startActivity(operator);
                        break;
                    case R.id.penipuan:
                        Toast.makeText(getApplicationContext(),"Penipuan",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent penipuan = new Intent(TrendingActivity.this, PenipuanActivity.class);
                        startActivity(penipuan);
                        break;
                    case R.id.unknown:
                        Toast.makeText(getApplicationContext(),"Tidak diketahui",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent unknown = new Intent(TrendingActivity.this, UnknownActivity.class);
                        startActivity(unknown);
                        break;
                    case R.id.blacklist:
                        drawerLayout.closeDrawers();
                        Intent blacklist = new Intent(TrendingActivity.this, BlacklistActivity.class);
                        startActivity(blacklist);
                        break;
                    case R.id.trending:
                        Toast.makeText(getApplicationContext(),"Trending",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent trending = new Intent(TrendingActivity.this, TrendingActivity.class);
                        startActivity(trending);
                        break;
                    case R.id.refresh:
                        Toast.makeText(getApplicationContext(),"Perbarui data",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent refresh = new Intent(TrendingActivity.this, TrendingActivity.class);
                        startActivity(refresh);
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("Username");
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

}
