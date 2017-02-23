package com.berpesan.berpesan;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PromosiActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promosi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
    }


    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.promosi_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.inbox:
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(PromosiActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.promosi:
                        drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(PromosiActivity.this, PromosiActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.smsBanking:
                        drawerLayout.closeDrawers();
                        Intent banking = new Intent(PromosiActivity.this, SMSBankingActivity.class);
                        startActivity(banking);
                        break;
                    case R.id.operator:
                        drawerLayout.closeDrawers();
                        Intent operator = new Intent(PromosiActivity.this, OperatorActivity.class);
                        startActivity(operator);
                        break;
                    case R.id.unknown:
                        drawerLayout.closeDrawers();
                        Intent unknown = new Intent(PromosiActivity.this, UnknownActivity.class);
                        startActivity(unknown);
                        break;
                    case R.id.penipuan:
                        drawerLayout.closeDrawers();
                        Intent penipuan = new Intent(PromosiActivity.this, PenipuanActivity.class);
                        startActivity(penipuan);
                        break;
                    case R.id.blacklist:
                        drawerLayout.closeDrawers();
                        Intent blacklist = new Intent(PromosiActivity.this, BlacklistActivity.class);
                        startActivity(blacklist);
                        break;
                    case R.id.trending:
                        drawerLayout.closeDrawers();
                        Intent trending = new Intent(PromosiActivity.this, TrendingActivity.class);
                        startActivity(trending);
                        break;
                    case R.id.refresh:
                        drawerLayout.closeDrawers();
                        Intent refresh = new Intent(PromosiActivity.this, TrendingActivity.class);
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
