package com.berpesan.activity;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.berpesan.R;

import java.text.DateFormat;
import java.util.Date;

public class SentBox extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    TextView pengirim, isipesan;
    ListView messages;
    private SimpleCursorAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_box);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuatPesan(view);
            }
        });

        pengirim = (TextView) findViewById(R.id.sender);
        isipesan = (TextView) findViewById(R.id.content);

        displayListView();

    }

    public void onBuatPesan(View view){
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivity(intent);
    }

    private void displayListView() {

        Intent i = getIntent();
        Uri uriSMS = Uri.parse("content://sms/sent");
        Cursor cursor = getContentResolver().query(uriSMS, null, null, null, null);

        String[] columns = new String[]{"address", "body", "date"};
        int[] to = new int[]{R.id.sender, R.id.content, R.id.received_date};

        dataAdapter = new SimpleCursorAdapter(this, R.layout.message, cursor, columns, to, 0);

        messages = (ListView) findViewById(R.id.outboxmessages);
        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {

                // ubah nomer hape dengan nama yang ada dikontak
                if (columnIndex == 2) {
                    TextView tv = (TextView) view;
                    String pengirimDB = cursor.getString(cursor
                            .getColumnIndex("address"));

                    // get contact name
                    Uri contactUri = Uri.withAppendedPath(
                            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(pengirimDB));
                    Cursor cur = getContentResolver().query(contactUri, null,
                            null, null, null);
                    ContentResolver contect_resolver = getContentResolver();

                    int size = cur.getCount();
                    if (size > 0 && cur != null) {
                        for (int i = 0; i < size; i++) {
                            cur.moveToPosition(i);

                            String id1 = cur.getString(cur
                                    .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                            Cursor phoneCur = contect_resolver
                                    .query(contactUri,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                    + " = ?",
                                            new String[]{id1}, null);

                            if (phoneCur.moveToFirst()) {
                                String namaKontak = phoneCur.getString(phoneCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                phoneCur.close();
                                tv.setText(namaKontak);
                            } else {
                                tv.setText(pengirimDB);
                            }

                        }

                        cur.close();
                    } else {
                        tv.setText(pengirimDB);
                    }

                    return true;
                }

                if(columnIndex==4){
                    TextView tv = (TextView) view;
                    String waktu = cursor.getString(cursor
                            .getColumnIndex("date"));
                    long l = Long.parseLong(waktu);
                    Date d = new Date(l);
                    String date = DateFormat.getDateInstance(DateFormat.LONG)
                            .format(d);
                    tv.setText(date);
                    return true;
                }

                if (columnIndex == 12) {
                    TextView tv = (TextView) view;
                    int maxlength = 25;
                    String isiPesan = cursor.getString(cursor
                            .getColumnIndex("body"));
                    if (isiPesan.length() > maxlength) {
                        String res = isiPesan.substring(0, Math.min(isiPesan.length(), maxlength)) + "...";
                        tv.setText(res);
                    } else {
                        tv.setText(isiPesan);
                    }
                    return true;
                }


                return false;
            }
        });


        // menampilkan daftar pesan
        messages.setAdapter(dataAdapter);

        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> messages, View view, int position, long id) {

                Cursor cursor = (Cursor)messages.getItemAtPosition(position);

                String view_pengirim = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String view_isipesan = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));

                String waktu = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));

                long l = Long.parseLong(waktu);
                Date d = new Date(l);
                String date = DateFormat.getDateInstance(DateFormat.LONG)
                        .format(d);
                String time = DateFormat.getTimeInstance().format(d);
                String view_waktu = date + " " + time;

                String view_idpesan = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String view_thread = cursor.getString(cursor
                        .getColumnIndexOrThrow("thread_id"));
                Intent click = new Intent(SentBox.this, SentBoxDetail.class);

                Uri contactUri = Uri.withAppendedPath(
                        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                        Uri.encode(view_pengirim));
                Cursor cur = getContentResolver().query(contactUri, null, null,
                        null, null);
                ContentResolver contect_resolver = getContentResolver();

                int size = cur.getCount();
                String id1 = null;
                if (size > 0 && cur != null) {
                    for (int i = 0; i < size; i++) {
                        cur.moveToPosition(i);

                        id1 = cur.getString(cur
                                .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        Cursor phoneCur = contect_resolver
                                .query(contactUri,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?", new String[] { id1 },
                                        null);

                        if (phoneCur.moveToFirst()) {
                            String namaKontak = phoneCur.getString(phoneCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            phoneCur.close();
                            click.putExtra("no", namaKontak);
                        } else {
                            click.putExtra("sender", view_pengirim);
                        }

                    }

                    cur.close();
                } else {
                    click.putExtra("sender", view_pengirim);
                }

                click.putExtra("msg", view_isipesan);
                click.putExtra("idpesan", view_idpesan);
                click.putExtra("idthread", view_thread);
                click.putExtra("date", view_waktu);
                click.putExtra("sender", view_pengirim);
                click.putExtra("id_kontak", id1);
                Intent i = getIntent();
                click.putExtra("asal", i.getStringExtra("tipepesan"));
                startActivity(click);
            }
        });

    }

//

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.sent_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.inbox:
                        drawerLayout.closeDrawers();
                        Intent inbox = new Intent(SentBox.this, MainActivity.class);
                        startActivity(inbox);
                        break;
                    case R.id.outbox:
                        drawerLayout.closeDrawers();
                        Intent outbox = new Intent(SentBox.this, SentBox.class);
                        startActivity(outbox);
                        break;
                    case R.id.trending:
                        drawerLayout.closeDrawers();
                        Intent trending = new Intent(SentBox.this, TrendingActivity.class);
                        startActivity(trending);
                        break;
                    case R.id.help:
                        drawerLayout.closeDrawers();
                        Intent help = new Intent(SentBox.this, HelpActivity.class);
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

}
