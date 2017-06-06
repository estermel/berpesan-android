package com.berpesan.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.berpesan.R;

public class ComposeActivity extends AppCompatActivity {

    Button sendSMSBtn;
    EditText toPhoneNumberET;
    EditText smsMessageET;
    ImageButton contactBtn;

    private static final int CONTACT_PICKER_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        sendSMSBtn = (Button) findViewById(R.id.btnSendSMS);
        toPhoneNumberET = (EditText) findViewById(R.id.editTextPhoneNo);
        smsMessageET = (EditText) findViewById(R.id.editTextSMS);
        contactBtn = (ImageButton) findViewById(R.id.contactButton);
        //send sms
        sendSMSBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMS();
            }
        });
        //show contact
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLaunchContactPicker();
            }
        });
        //forward
        Intent intent = getIntent();
        if(intent.getStringExtra("forward") != null){
            smsMessageET.setText(intent.getStringExtra("forward"));
        }
    }

    // phonecontact
    public void doLaunchContactPicker() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = "";
        Cursor contacts = null;
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case CONTACT_PICKER_RESULT:
                        // gets the uri of selected contact
                        Uri result = data.getData();
                        // get the contact id from the Uri (last part is contact id)
                        String id = result.getLastPathSegment();
                        // queries the contacts DB for phone no
                        contacts = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone._ID + "=?",
                                new String[]{id}, null);
                        // gets index of phone no
                        int phoneIdx = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
                        if (contacts.moveToFirst()) {
                            // gets the phone no
                            phone = contacts.getString(phoneIdx);
                            EditText phoneTxt = (EditText) findViewById(R.id.editTextPhoneNo);
                            // assigns phone no to EditText field phoneno
                            phoneTxt.setText(phone);
                        } else {
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            } else {

            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (contacts != null) {
                contacts.close();
            }
        }
    }

    protected void sendSMS() {
        String toPhoneNumber = toPhoneNumberET.getText().toString();
        String smsMessage = smsMessageET.getText().toString();
        String telkomsel = "^(\\+62\\s?|0)8(1[123]|52|53|21|22|23)-?([0-9]{4}-?)[0-9]{3,4}$";
        String indosat = "^(\\+62\\s?|0)8(1[456]|58)-?([0-9]{4}-?)[0-9]{3,4}$";
        String imtri = "^(\\+62\\s?)8(5[567])-?([0-9]{4}-?)[0-9]{3,4}$";
        String xl = "^(\\+62\\s?)8(1[789]|59|7[78])-?([0-9]{4}-?)[0-9]{3,4}$";
        try {
            if(toPhoneNumber.matches(telkomsel) || toPhoneNumber.matches(indosat) || toPhoneNumber.matches(imtri) || toPhoneNumber.matches(xl)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
                Toast.makeText(getApplicationContext(), "SMS terkirim",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Masukkan nomor yang valid", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Gagal mengirim SMS",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}

