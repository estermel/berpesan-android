package com.berpesan.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.berpesan.Dialogs;
import com.berpesan.R;

public class MessageDetail extends AppCompatActivity {

    TextView nopengirim, isipesan, tanggalkirim, tampilpengirim, tampilan;
    EditText pesanreply;
    ImageButton reply;
    String id_kontak = null;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isipesan = (TextView) findViewById(R.id.tvMsg);
        tanggalkirim = (TextView) findViewById(R.id.tvDate);
        reply = (ImageButton) findViewById(R.id.imageButtonReply);
        pesanreply = (EditText) findViewById(R.id.pesanReply);

        Intent i = getIntent();
        tanggalkirim.setText(i.getStringExtra("date"));
        isipesan.setText(i.getStringExtra("msg"));
        id_kontak = i.getStringExtra("id_kontak");
        String.valueOf(id_kontak);
        reply.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                sendSMS();
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.ic_arrow_back_white_24dp);

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
            Dialogs.showConfirmation(MessageDetail.this,
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
                                Toast.makeText(MessageDetail.this,
                                        "Pesan Terhapus", Toast.LENGTH_SHORT)
                                        .show();
                                // redirect data pesan
                                onBackPressed();
        }
            });
        }
        else if (item.getItemId() == R.id.teruskanpesan) {
            Intent click = new Intent(MessageDetail.this, ComposeActivity.class);
            click.putExtra("forward", isipesan.getText());
            startActivity(click);
        } else if (item.getItemId() == R.id.simpankontak) {
            Intent a = new Intent(MessageDetail.this, TambahKontak.class);
                a.putExtra("tambahkontak", actionBar.getTitle());
                startActivity(a);
        } else {
            onBackPressed();
        }
        return true;
    }


    protected void sendSMS() {
        Intent i = getIntent();
        String toPhoneNumber = i.getStringExtra("sender").toString();
        String smsMessage = pesanreply.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
            Toast.makeText(getApplicationContext(), "Pesan terkirim",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MessageDetail.this, MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Pesan gagal dikirim",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent link = new Intent(MessageDetail.this, MainActivity.class);
        startActivity(link);
    }
}
