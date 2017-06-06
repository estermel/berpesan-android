package com.berpesan;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.berpesan.activity.MainActivity;

import static android.telephony.SmsMessage.createFromPdu;

/**
 * Created by Trima W Manurung on 27/02/2017.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver{
    public static final String SMS_BUNDLE = "pdus";

    public SmsBroadcastReceiver() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; i++) {
                String format = intentExtras.getString("format");
                SmsMessage smsMessage = createFromPdu((byte[]) sms[i], format);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, smsMessage.getOriginatingAddress());
                Cursor cur = context.getContentResolver().query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

                if (cur.moveToFirst()) {
                    int nameIndex = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    String PersonName = cur.getString(nameIndex);

                    smsMessageStr += PersonName + "\n";
                    smsMessageStr += smsBody + "\n";

                    NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context);
                    mbuilder.setContentTitle("Pesan Baru");
                    mbuilder.setContentText(smsMessageStr);
                    mbuilder.setTicker(PersonName + "\n" + smsBody);
                    mbuilder.setSmallIcon(R.drawable.images2);
                    Intent resulIntent = new Intent(context, MainActivity.class);
                    PendingIntent resulPendingIntent = PendingIntent.getActivity(context, 0, resulIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mbuilder.setContentIntent(resulPendingIntent);
                    mbuilder.setAutoCancel(true);

                    NotificationManager notifyMngr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    notifyMngr.notify(1, mbuilder.build());

                } else {
                    smsMessageStr += address + "\n";
                    smsMessageStr += smsBody + "\n";

                    NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context);
                    mbuilder.setContentTitle("Pesan Baru");
                    mbuilder.setContentText(smsMessageStr);
                    mbuilder.setTicker(address + "\n" + smsBody);
                    mbuilder.setSmallIcon(R.drawable.images2);

                    Intent resulIntent = new Intent(context, MainActivity.class);
                    PendingIntent resulPendingIntent = PendingIntent.getActivity(context, 0, resulIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mbuilder.setContentIntent(resulPendingIntent);
                    mbuilder.setAutoCancel(true);

                    NotificationManager notifyMngr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    notifyMngr.notify(1, mbuilder.build());
                }
                cur.close();
            }
        }
    }

}