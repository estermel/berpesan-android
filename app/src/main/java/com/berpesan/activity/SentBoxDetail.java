package com.berpesan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.berpesan.Dialogs;
import com.berpesan.R;

public class SentBoxDetail extends AppCompatActivity {
    TextView nopengirim, isipesan, tanggalkirim, tampilpengirim;
    String id_kontak = null;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_box_detail);

        isipesan = (TextView) findViewById(R.id.outMsg);
        tanggalkirim = (TextView) findViewById(R.id.outDate);

        Intent i = getIntent();
        tanggalkirim.setText(i.getStringExtra("date"));
        isipesan.setText(i.getStringExtra("msg"));
        id_kontak = i.getStringExtra("id_kontak");
        String.valueOf(id_kontak);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(i.getStringExtra("no"));
        actionBar.setSubtitle(i.getStringExtra("sender"));

        if(id_kontak==null){
            actionBar.setTitle(i.getStringExtra("sender"));
        }
        else {
            actionBar.setTitle(i.getStringExtra("no"));
            actionBar.setSubtitle(i.getStringExtra("sender"));
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        if(id_kontak == null){

            MenuItem item = menu.findItem(R.id.simpankontak);
            item.setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.hapuspesan){
            Dialogs.showConfirmation(SentBoxDetail.this,
                    R.string.hapuspesan_dialog,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Intent i = getIntent();
                            String id_pesan_hapus = i
                                    .getStringExtra("idpesan");
                            String id_thread_hapus = i
                                    .getStringExtra("idthread");

                            // hapus pesan
                            Uri deleteUri = Uri.parse("content://sms");
                            getContentResolver()
                                    .delete(deleteUri,
                                            "thread_id=? and _id=?",
                                            new String[] {
                                                    String.valueOf(id_thread_hapus),
                                                    String.valueOf(id_pesan_hapus) });
                            finish();
                            Toast.makeText(SentBoxDetail.this,
                                    "Pesan Terhapus", Toast.LENGTH_SHORT)
                                    .show();
                            // redirect data pesan
                            onBackPressed();
                        }
                    });
        }
        else if (item.getItemId() == R.id.teruskanpesan) {
            Intent click = new Intent(SentBoxDetail.this, ComposeActivity.class);
            click.putExtra("forward", isipesan.getText());
            startActivity(click);
        } else if (item.getItemId() == R.id.simpankontak) {
            Intent a = new Intent(SentBoxDetail.this, TambahKontak.class);
            a.putExtra("tambahkontak", actionBar.getTitle());
            startActivity(a);
        } else {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent link = new Intent(SentBoxDetail.this, SentBox.class);
        startActivity(link);
    }
}
