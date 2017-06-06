package com.berpesan.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.berpesan.R;
import com.berpesan.model.DataSms;
import com.berpesan.rest.RequestInterface;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    String classified_sms;
    String isiPesan;
    private SimpleCursorAdapter dataAdapter;
    ListView messages;
    private static final int PERMISSION_READ_SMS = 99;
    private static final int PERMISSION_READ_CONTACTS = 100;
    private static final int PERMISSION_WRITE_CONTACTS = 101;
    private static final int PERMISSION_SEND_SMS = 102;
    private static final int DEF_SMS_REQ = 0;
    private String mDefaultSmsApp;
    private static MainActivity inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_SMS},PERMISSION_READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS},PERMISSION_SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_CONTACTS},PERMISSION_WRITE_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS},PERMISSION_READ_CONTACTS);
        }

        inst = this;
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuatPesan(view);
            }
        });

        displayListMessages();
        initNavigationDrawer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mDefaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);
            if (!getPackageName().equals(mDefaultSmsApp)) {

            }
        }
    }

    private void displayListMessages(){
        Uri uriSMS = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSMS, null, null, null, null);
        String[] columns = new String[]{"address", "body", "date"};
        int[] to = new int[] {R.id.sender, R.id.content, R.id.received_date};
        dataAdapter = new SimpleCursorAdapter(this, R.layout.message, cursor, columns, to, 0);
        messages = (ListView) findViewById(R.id.messages);
        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (columnIndex == 2) {
                    isiPesan = cursor.getString(cursor
                            .getColumnIndex("body"));
                    TextView tv = (TextView) view;
                    String pengirimDB = cursor.getString(cursor
                            .getColumnIndex("address"));
                    String view_idpesan = cursor.getString(cursor
                            .getColumnIndexOrThrow("_id"));
                    String view_thread = cursor.getString(cursor
                            .getColumnIndexOrThrow("thread_id"));
                    Uri contactUri = Uri.withAppendedPath(
                            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(pengirimDB));
                    Cursor cur = getContentResolver().query(contactUri, null,
                            null, null, null);
                    ContentResolver contect_resolver = getContentResolver();
                    int size = cur.getCount();

                    if (size > 0 && cur != null) {

                        try{
                            for (int i = 0; i < size; i++) {
                                cur.moveToPosition(i);
                                String id1 = cur.getString(cur
                                        .getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                                Cursor phoneCur = contect_resolver
                                        .query(contactUri,
                                                null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = ?",
                                                new String[] { id1 }, null);
                                if(phoneCur.moveToPosition(i)) {
                                    String namaKontak = phoneCur.getString(phoneCur
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    tv.setText(namaKontak + " - HAM");
                                }
                                cur.moveToNext();
                            }
                        } catch (Exception e){
                            Log.e("ERROR: ", e.toString());
                        }
                    } else {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("99986", "PROMOSI");
                        hashMap.put("111", "OPERATOR");
                        hashMap.put("809", "OPERATOR");
                        hashMap.put("3Care", "OPERATOR");
                        hashMap.put("PAPRIKA", "OPERATOR");
                        hashMap.put("GRAB ID", "OPERATOR");
                        hashMap.put("NNN", "PROMOSI");
                        hashMap.put("98888", "OPERATOR");
                        hashMap.put("2828", "OPERATOR");
                        hashMap.put("ROMP", "PROMOSI");
                        hashMap.put("PopCall", "OPERATOR");
                        hashMap.put("1213", "OPERATOR");
                        hashMap.put("88222", "OPERATOR");
                        hashMap.put("88330", "OPERATOR");
                        hashMap.put("88331", "OPERATOR");
                        hashMap.put("88332", "OPERATOR");
                        hashMap.put("88550", "OPERATOR");
                        hashMap.put("88551", "OPERATOR");
                        hashMap.put("3636", "OPERATOR");
                        hashMap.put("8999", "OPERATOR");
                        hashMap.put("234", "OPERATOR");
                        hashMap.put("3", "OPERATOR");
                        hashMap.put("Info Kartu 3", "OPERATOR");
                        hashMap.put("Telegram", "OPERATOR");
                        hashMap.put("simPATI", "OPERATOR");
                        hashMap.put("MKIOS", "OPERATOR");
                        hashMap.put("OPTIN TSEL", "OPERATOR");
                        hashMap.put("MyTelkomsel", "OPERATOR");
                        hashMap.put("TELKOMSEL", "OPERATOR");
                        hashMap.put("TSEL INFO", "OPERATOR");
                        hashMap.put("Google", "OPERATOR");
                        hashMap.put("Yahoo", "OPERATOR");
                        hashMap.put("Microsoft", "OPERATOR");
                        hashMap.put("MiTalk", "OPERATOR");
                        hashMap.put("WhatsApp", "OPERATOR");
                        hashMap.put("Whatsapp", "OPERATOR");
                        hashMap.put("LINE", "OPERATOR");
                        hashMap.put("Twitter", "OPERATOR");
                        hashMap.put("FACEBOOK", "OPERATOR");
                        hashMap.put("Facebook", "OPERATOR");
                        hashMap.put("Facebook-Xp", "PROMOSI");
                        hashMap.put("LinkedIn", "OPERATOR");
                        hashMap.put("NSP1212", "PROMOSI");
                        hashMap.put("KEMENDIKBUD", "PROMOSI");
                        hashMap.put("VGSMS", "OPERATOR");
                        hashMap.put("ttSMS", "OPERATOR");
                        hashMap.put("1818", "PROMOSI");
                        hashMap.put("Tokopedia", "OPERATOR");
                        hashMap.put("TOKOPEDIA", "OPERATOR");
                        hashMap.put("EXCELSO", "PROMOSI");
                        hashMap.put("CFC", "PROMOSI");
                        hashMap.put("KFC", "PROMOSI");
                        hashMap.put("NXSMS", "OPERATOR");
                        hashMap.put("3300", "OPERATOR");
                        hashMap.put("CNET", "OPERATOR");
                        hashMap.put("4444", "OPERATOR");
                        hashMap.put("3Topup", "OPERATOR");
                        hashMap.put("234", "OPERATOR");
                        hashMap.put("8999", "OPERATOR");
                        hashMap.put("858", "OPERATOR");
                        hashMap.put("btpnWOW", "BANKING");
                        hashMap.put("02140000440", "BANGKING");
                        hashMap.put("+622149053046", "BANGKING");
                        hashMap.put("02180629600", "BANGKING");
                        hashMap.put("025180633499", "BANGKING");
                        hashMap.put("02140460046", "BANGKING");
                        hashMap.put("0312983700", "BANGKING");
                        hashMap.put("02149054994", "BANGKING");
                        hashMap.put("02180629888", "BANGKING");
                        hashMap.put("0224271319", "BANGKING");
                        hashMap.put("02140200368", "BANGKING");
                        hashMap.put("02140101457", "BANGKING");
                        hashMap.put("02130069752", "BANGKING");
                        hashMap.put("02130068400", "BANGKING");
                        hashMap.put("02130069713", "BANGKING");
                        hashMap.put("02129393563", "BANGKING");
                        hashMap.put("02129393594", "BANGKING");
                        hashMap.put("08119706019", "BANGKING");
                        hashMap.put("02150161811", "BANGKING");
                        hashMap.put("03128954700", "BANGKING");
                        hashMap.put("0312955400", "BANGKING");
                        hashMap.put("02121888299", "BANGKING");
                        hashMap.put("02129393529", "BANGKING");
                        hashMap.put("02150592289", "BANGKING");
                        hashMap.put("02129291300", "BANGKING");
                        hashMap.put("02125514600", "BANGKING");
                        hashMap.put("02128095200", "BANGKING");
                        hashMap.put("02129800100", "BANGKING");
                        hashMap.put("02130069797", "BANGKING");
                        hashMap.put("02130069721", "BANGKING");
                        hashMap.put("02130431531", "BANGKING");
                        hashMap.put("02130069771", "BANGKING");
                        hashMap.put("02180681140", "BANGKING");
                        hashMap.put("02121884700", "BANGKING");
                        hashMap.put("02125506500", "BANGKING");
                        hashMap.put("02129494202", "BANGKING");
                        hashMap.put("02129348831", "BANGKING");
                        hashMap.put("02180668000", "BANGKING");
                        hashMap.put("+622129494400", "BANGKING");
                        hashMap.put("02150337789", "BANGKING");
                        hashMap.put("+622128508100", "BANGKING");
                        hashMap.put("02180682046", "BANGKING");
                        hashMap.put("0215246868", "BANGKING");
                        hashMap.put("02130499812", "BANGKING");
                        hashMap.put("02129910700", "BANGKING");
                        hashMap.put("02284254000", "BANGKING");
                        hashMap.put("02129940123", "BANGKING");
                        hashMap.put("+622129535555", "BANGKING");
                        hashMap.put("02125531700", "BANGKING");
                        hashMap.put("03129805100", "BANGKING");
                        hashMap.put("02129819516", "BANGKING");
                        hashMap.put("02518306300", "BANGKING");
                        hashMap.put("02124180567", "BANGKING");
                        hashMap.put("02129960025", "BANGKING");
                        hashMap.put("02125656400", "BANGKING");
                        hashMap.put("02150101299", "BANGKING");
                        hashMap.put("02131922400", "BANGKING");
                        hashMap.put("02150101149", "BANGKING");
                        hashMap.put("02150540104", "BANGKING");
                        hashMap.put("02150101452", "BANGKING");
                        hashMap.put("02129950697", "BANGKING");
                        hashMap.put("02129490671", "BANGKING");
                        hashMap.put("0217232225", "BANGKING");
                        hashMap.put("02180635000", "BANGKING");
                        hashMap.put("+622140002222", "BANGKING");
                        hashMap.put("0216232225", "BANGKING");
                        hashMap.put("021234510000", "BANGKING");
                        hashMap.put("02130023000", "BANGKING");
                        hashMap.put("+622125508182", "BANGKING");
                        hashMap.put("02129350000", "BANGKING");
                        hashMap.put("02129293969", "BANGKING");
                        hashMap.put("02126565100", "BANGKING");
                        hashMap.put("02129293100", "BANGKING");
                        hashMap.put("02130436900", "BANGKING");
                        hashMap.put("02129977016", "BANGKING");
                        hashMap.put("02180682280", "BANGKING");
                        hashMap.put("02127586100", "BANGKING");
                        hashMap.put("02150301130", "BANGKING");
                        hashMap.put("02125547900", "BANGKING");
                        hashMap.put("02130419500", "BANGKING");
                        hashMap.put("02129351600", "BANGKING");
                        hashMap.put("02230001430", "BANGKING");
                        hashMap.put("02180682090", "BANGKING");
                        hashMap.put("02129494262", "BANGKING");
                        hashMap.put("02129293985", "BANGKING");
                        hashMap.put("02231201245", "BANGKING");
                        hashMap.put("+622129977000", "BANGKING");
                        hashMap.put("02129220300", "BANGKING");
                        hashMap.put("02130440500", "BANGKING");
                        hashMap.put("02130447600", "BANGKING");
                        hashMap.put("02130447500", "BANGKING");
                        hashMap.put("02129536100", "BANGKING");
                        hashMap.put("02130009524", "BANGKING");
                        hashMap.put("0312950600", "BANGKING");
                        hashMap.put("+622130009526", "BANGKING");
                        hashMap.put("++622130009536", "BANGKING");
                        hashMap.put("+622126565300", "BANGKING");
                        hashMap.put("02129636000", "BANGKING");
                        hashMap.put("07116059600", "BANGKING");
                        hashMap.put("02123506100", "BANGKING");
                        hashMap.put("02130009535", "BANGKING");
                        hashMap.put("02129636100", "BANGKING");
                        hashMap.put("02130009527", "BANGKING");
                        hashMap.put("02125515000", "BANGKING");
                        hashMap.put("02127891300", "BANGKING");
                        hashMap.put("02150111001", "BANGKING");
                        hashMap.put("02140101061", "BANGKING");
                        hashMap.put("02125506000", "BANGKING");
                        hashMap.put("02129103000", "BANGKING");
                        hashMap.put("02140101066", "BANGKING");
                        hashMap.put("02150500233", "BANGKING");
                        hashMap.put("02125526500", "BANGKING");
                        hashMap.put("02129977010", "BANGKING");
                        hashMap.put("02125527500", "BANGKING");
                        hashMap.put("0227331204", "BANGKING");
                        hashMap.put("02129603888", "BANGKING");
                        hashMap.put("02129702100", "BANGKING");
                        hashMap.put("02150570234", "BANGKING");
                        hashMap.put("02125547800", "BANGKING");
                        hashMap.put("02129809300", "BANGKING");
                        hashMap.put("03160004700", "BANGKING");
                        hashMap.put("02130029200", "BANGKING");
                        hashMap.put("02129977300", "BANGKING");
                        hashMap.put("02150573505", "BANGKING");
                        hashMap.put("02127586200", "BANGKING");
                        hashMap.put("+622125531800", "BANGKING");
                        hashMap.put("02129275876", "BANGKING");
                        hashMap.put("02129885066", "BANGKING");
                        hashMap.put("02135874691", "BANGKING");
                        hashMap.put("02130483500", "BANGKING");
                        hashMap.put("02135874697", "BANGKING");
                        hashMap.put("0312939800", "BANGKING");
                        hashMap.put("02129275857", "BANGKING");
                        hashMap.put("02121887200", "BANGKING");
                        hashMap.put("+02129940308", "BANGKING");
                        hashMap.put("02129940317", "BANGKING");
                        hashMap.put("02129293988", "BANGKING");
                        hashMap.put("02129293958", "BANGKING");
                        hashMap.put("02129852812", "BANGKING");
                        hashMap.put("02129802900", "BANGKING");
                        hashMap.put("02717659799", "BANGKING");
                        hashMap.put("0315506100", "BANGKING");
                        hashMap.put("02140001270", "BANGKING");
                        hashMap.put("02130272183", "BANGKING");
                        hashMap.put("02130447607", "BANGKING");
                        hashMap.put("+622125546100", "BANGKING");
                        hashMap.put("02129103600", "BANGKING");
                        hashMap.put("02130050100", "BANGKING");
                        hashMap.put("+622125563700", "BANGKING");
                        hashMap.put("+622129220200", "BANGKING");
                        hashMap.put("02129969325", "BANGKING");
                        hashMap.put("02129858900", "BANGKING");
                        hashMap.put("02130414300", "BANGKING");
                        hashMap.put("02129936500", "BANGKING");
                        hashMap.put("02130069728", "BANGKING");
                        hashMap.put("02150598899", "BANGKING");
                        hashMap.put("02129494263", "BANGKING");
                        hashMap.put("02129109600", "BANGKING");
                        hashMap.put("08124111531", "BANGKING");
                        hashMap.put("02129934231", "BANGKING");
                        hashMap.put("0811504141", "BANGKING");
                        hashMap.put("02129295849", "BANGKING");
                        hashMap.put("64722", "BANGKING");
                        hashMap.put("64700 ", "BANGKING");
                        hashMap.put("64722", "BANGKING");
                        hashMap.put("3269", "BANGKING");
                        hashMap.put("3399", "BANGKING");
                        hashMap.put("63399", "BANGKING");
                        hashMap.put("62265", "BANGKING");
                        hashMap.put("3354", "BANGKING");
                        hashMap.put("3828", "BANGKING");
                        hashMap.put("3344", "BANGKING");
                        hashMap.put("3488", "BANGKING");
                        hashMap.put("3555", "BANGKING");
                        hashMap.put("68000", "BANGKING");
                        hashMap.put("3338", "BANGKING");
                        hashMap.put("3377", "BANGKING");
                        hashMap.put("60010", "BANGKING");
                        hashMap.put("60001", "BANGKING");
                        hashMap.put("14041", "BANGKING");
                        hashMap.put("1418", "BANGKING");
                        hashMap.put("14042", "BANGKING");
                        hashMap.put("62265", "BANGKING");
                        hashMap.put("3339", "BANGKING");
                        hashMap.put("3346", "BANGKING");
                        hashMap.put("68888", "BANGKING");
                        hashMap.put("3654", "BANGKING");
                        hashMap.put("69811", "BANGKING");
                        hashMap.put("3355", "BANGKING");
                        hashMap.put("14000", "BANGKING");
                        hashMap.put("3989", "BANGKING");
                        hashMap.put("3663", "BANGKING");
                        hashMap.put("3316", "BANGKING");
                        hashMap.put("67777", "BANGKING");
                        hashMap.put("3435", "BANGKING");
                        hashMap.put("3300", "BANGKING");
                        hashMap.put("3311", "BANGKING");
                        hashMap.put("69123", "BANGKING");
                        hashMap.put("69222", "BANGKING");
                        hashMap.put("69888", "BANGKING");
                        hashMap.put("6199", "BANGKING");
                        hashMap.put("3122", "BANGKING");
                        hashMap.put("3344", "BANGKING");
                        hashMap.put("3117", "BANGKING");
                        hashMap.put("3345", "BANGKING");
                        hashMap.put("60678", "BANGKING");
                        hashMap.put("3726", "BANGKING");
                        hashMap.put("3132", "BANGKING");
                        hashMap.put("3135", "BANGKING");
                        hashMap.put("3130", "BANGKING");
                        hashMap.put("3332", "BANGKING");
                        hashMap.put("69327", "BANGKING");
                        hashMap.put("14008", "BANGKING");
                        hashMap.put("69999", "BANGKING");
                        hashMap.put("3348", "BANGKING");
                        hashMap.put("66999", "BANGKING");
                        hashMap.put("085293497236", "PENIPUAN");
                        hashMap.put("082333333587", "PENIPUAN");
                        hashMap.put("02125508284", "PENIPUAN");
                        hashMap.put("02129854128", "PENIPUAN");
                        hashMap.put("02139834475", "PENIPUAN");
                        hashMap.put("085236543325", "PENIPUAN");
                        hashMap.put("02129854322", "PENIPUAN");
                        hashMap.put("08091000400", "PENIPUAN");
                        hashMap.put("082111207613", "PENIPUAN");
                        hashMap.put("02180680416", "PENIPUAN");
                        hashMap.put("0811002812", " PENIPUAN");
                        hashMap.put("08041680068", "PENIPUAN");
                        hashMap.put("0811004804", "PENIPUAN");
                        hashMap.put("02180009000", "PENIPUAN");
                        hashMap.put("02518202000", "PENIPUAN");
                        hashMap.put("02130056800", "PENIPUAN");
                        hashMap.put("085336186465", "PENIPUAN");
                        hashMap.put("+6281100000", "PENIPUAN");
                        hashMap.put("0213832222", "PENIPUAN");
                        hashMap.put("02140101057", "PENIPUAN");
                        hashMap.put("085336104000", "PENIPUAN");
                        hashMap.put("08091234007", "PENIPUAN");
                        hashMap.put("02188888888", "PENIPUAN");
                        hashMap.put("0217777777", "PENIPUAN ");
                        hashMap.put("02129776100", "PENIPUAN ");
                        hashMap.put("08041000123", "PENIPUAN");
                        hashMap.put("082112344489", "PENIPUAN ");
                        hashMap.put("02130009000", "PENIPUAN ");
                        hashMap.put("081222752287", "PENIPUAN ");
                        hashMap.put("085794173867", "PENIPUAN ");
                        hashMap.put("0816215056", "PENIPUAN");
                        hashMap.put("022129854128", "PENIPUAN ");
                        hashMap.put("02150681873", "PENIPUAN ");
                        hashMap.put("02150286087", "PENIPUAN ");
                        hashMap.put("02140237999", "PENIPUAN ");
                        hashMap.put("02150101378", "PENIPUAN ");
                        hashMap.put("08041401022", "PENIPUAN ");
                        hashMap.put("02129050154", "PENIPUAN");
                        hashMap.put("082379671212", "PENIPUAN ");
                        hashMap.put("0218352881", "PENIPUAN ");
                        hashMap.put("082197450777", "PENIPUAN ");
                        hashMap.put("082290846906", "PENIPUAN ");
                        hashMap.put("02149053085", "PENIPUAN ");
                        hashMap.put("082278401212", "PENIPUAN ");
                        hashMap.put("081355587441", "PENIPUAN");
                        hashMap.put("08128880902", "PENIPUAN ");
                        hashMap.put("08128646611", "PENIPUAN ");
                        hashMap.put("081227616161", "PENIPUAN ");
                        hashMap.put("085213995599", "PENIPUAN ");
                        hashMap.put("081278444544", "PENIPUAN ");
                        hashMap.put("081584876669", "PENIPUAN ");
                        hashMap.put("0312982100", "PENIPUAN ");
                        hashMap.put("02129554210", "PENIPUAN ");
                        hashMap.put("085718812860", "PENIPUAN ");
                        hashMap.put("+62341147", "PENIPUAN ");
                        hashMap.put("08091232004", "PENIPUAN ");
                        hashMap.put("02180635955", "PENIPUAN ");
                        hashMap.put("081312304304", "PENIPUAN ");
                        hashMap.put("0811003804", "PENIPUAN ");
                        hashMap.put("02130068700", "PENIPUAN ");
                        hashMap.put("08118151999", "PENIPUAN ");
                        hashMap.put("085340012733", "PENIPUAN ");
                        hashMap.put("08091111333", "PENIPUAN");
                        hashMap.put("08091000809", "PENIPUAN ");
                        hashMap.put("0212943400", "PENIPUAN ");
                        hashMap.put("085399526574", "PENIPUAN ");
                        hashMap.put("02156103693", "PENIPUAN ");
                        hashMap.put("02127515069", "PENIPUAN ");
                        hashMap.put("02150651649", "PENIPUAN");
                        hashMap.put("02183705729", "PENIPUAN ");
                        hashMap.put("02140090236", "PENIPUAN ");
                        hashMap.put("0217816514", "PENIPUAN ");
                        hashMap.put("02180682053", "PENIPUAN ");
                        hashMap.put("08118108427", "PENIPUAN ");
                        hashMap.put("081328566553", "PENIPUAN ");
                        hashMap.put("083878507929", "PENIPUAN");
                        hashMap.put("0318482076", "PENIPUAN ");
                        hashMap.put("02129295830", "PENIPUAN ");
                        hashMap.put("02182564112", "PENIPUAN ");
                        hashMap.put("02129204392", "PENIPUAN ");
                        hashMap.put("+6287887898243", "PENIPUAN ");
                        hashMap.put("08091234001", "PENIPUAN ");
                        hashMap.put("08118252752", "PENIPUAN");
                        hashMap.put("02129274104", "PENIPUAN ");
                        hashMap.put("081291515575", "PENIPUAN ");
                        hashMap.put("081375911777", "PENIPUAN ");
                        hashMap.put("03160022", "PENIPUAN ");
                        hashMap.put("0218298921", "PENIPUAN ");
                        hashMap.put("081218454761", "PENIPUAN ");
                        hashMap.put("08561386916", "PENIPUAN");
                        hashMap.put("08118103760", "PENIPUAN ");
                        hashMap.put("03192212667", "PENIPUAN ");
                        hashMap.put("085891495650", "PENIPUAN ");
                        hashMap.put("08124115107", "PENIPUAN ");
                        hashMap.put("081310555962", "PENIPUAN ");
                        hashMap.put("02180635953", "PENIPUAN ");
                        hashMap.put("0818444800", "PENIPUAN");
                        hashMap.put("02130009587", "PENIPUAN ");
                        hashMap.put("02149050888", "PENIPUAN ");
                        hashMap.put("089601500500", "PENIPUAN ");
                        hashMap.put("085762413059", "PENIPUAN ");
                        hashMap.put("0816593419", "PENIPUAN ");
                        hashMap.put("02180622499", "PENIPUAN");
                        hashMap.put("02129264018", "PENIPUAN ");
                        hashMap.put("082324575028", "PENIPUAN ");
                        hashMap.put("08119706029", "PENIPUAN ");
                        hashMap.put("02156192057", "PENIPUAN ");
                        hashMap.put("0818444800", "PENIPUAN ");
                        hashMap.put("0895384622880", "PENIPUAN ");
                        hashMap.put("02125547600", "PENIPUAN");
                        hashMap.put("08041401028", "PENIPUAN ");
                        hashMap.put("085718191076", "PENIPUAN ");
                        hashMap.put("02128638000", "PENIPUAN ");
                        hashMap.put("+622486458900", "PENIPUAN ");
                        hashMap.put("02150286083", "PENIPUAN ");
                        hashMap.put("02150200245", "PROMOSI ");
                        hashMap.put("02130009779", "PROMOSI ");
                        hashMap.put("02171673388", "PROMOSI ");
                        hashMap.put("02180625500", "PROMOSI ");
                        hashMap.put("02125541200", "PROMOSI ");
                        hashMap.put("08118285999", "PROMOSI ");
                        hashMap.put("02130434949", "PROMOSI ");
                        hashMap.put("02180681170", "PROMOSI ");
                        hashMap.put("02140101422", "PROMOSI ");
                        hashMap.put("04113661654", "PROMOSI ");
                        hashMap.put("02129274117", "PROMOSI ");
                        hashMap.put("02129393573", "PROMOSI ");
                        hashMap.put("02129274154", "PROMOSI ");
                        hashMap.put("08161601746", "PROMOSI ");
                        hashMap.put("02130481944", "PROMOSI ");
                        hashMap.put("02130483304", "PROMOSI ");
                        hashMap.put("02129265954", "PROMOSI ");
                        hashMap.put("02150652059", "PROMOSI ");
                        hashMap.put("02130068818", "PROMOSI ");
                        hashMap.put("02149055077", "PROMOSI ");
                        hashMap.put("02124155500", "PROMOSI ");
                        hashMap.put("02129351400", "PROMOSI ");
                        hashMap.put("02180625588", "PROMOSI ");
                        hashMap.put("+622129496500", "PROMOSI ");
                        hashMap.put("08091114444", "PROMOSI ");
                        hashMap.put("02150300591", "PROMOSI ");
                        hashMap.put("02152914800", "PROMOSI ");
                        hashMap.put("02130496191", "PROMOSI ");
                        hashMap.put("02129771121", "PROMOSI ");
                        hashMap.put("02130401880", "PROMOSI ");
                        hashMap.put("02130027700", "PROMOSI ");
                        hashMap.put("+622125695722", "PROMOSI ");
                        hashMap.put("02150504599", "PROMOSI ");
                        hashMap.put("02180680752", "PROMOSI ");
                        hashMap.put("02151300420", "PROMOSI ");
                        hashMap.put("+622125545000", "PROMOSI ");
                        hashMap.put("02180682233", "PROMOSI ");
                        hashMap.put("02180633701", "PROMOSI ");
                        hashMap.put("02129633000", "PROMOSI ");
                        hashMap.put("02121888266", "PROMOSI ");
                        hashMap.put("021290758877", "PROMOSI ");
                        hashMap.put("02129557258", "PROMOSI ");
                        hashMap.put("08111462226", "PROMOSI ");
                        hashMap.put("02140090225", "PROMOSI ");
                        hashMap.put("02125536100", "PROMOSI ");
                        hashMap.put("02130401844", "PROMOSI ");
                        hashMap.put("08118108424", "PROMOSI ");
                        hashMap.put("085221263343", "PROMOSI ");
                        hashMap.put("082277277527", "PROMOSI ");
                        hashMap.put("0215764122", "PROMOSI ");
                        hashMap.put("02129521111", "PROMOSI ");
                        hashMap.put("02175912401", "PROMOSI ");
                        hashMap.put("+622129911200", "PROMOSI ");
                        hashMap.put("08091000111", "PROMOSI ");
                        hashMap.put("081514863715", "PROMOSI ");
                        hashMap.put("02129295848", "PROMOSI ");
                        hashMap.put("02130069703", "PROMOSI ");
                        hashMap.put("02129295833", "PROMOSI ");
                        hashMap.put("02131996778", "PROMOSI ");
                        hashMap.put("02150652127", "PROMOSI ");
                        hashMap.put("02124180561", "PROMOSI ");
                        hashMap.put("02129393526", "PROMOSI ");
                        hashMap.put("02129295839", "PROMOSI ");
                        hashMap.put("02188975770", "PROMOSI ");
                        hashMap.put("08119706052", "PROMOSI ");
                        hashMap.put("08091006060", "PROMOSI ");
                        hashMap.put("081290784791", "PROMOSI ");
                        hashMap.put("082140297999", "PROMOSI ");
                        hashMap.put("02129632400", "PROMOSI ");
                        hashMap.put("0213001120", "PROMOSI ");
                        hashMap.put("+622129490700", "PROMOSI ");
                        hashMap.put("+622129554145", "PROMOSI ");
                        hashMap.put("02130033014", "PROMOSI ");
                        hashMap.put("02127586400", "PROMOSI ");
                        hashMap.put("02130490900", "PROMOSI ");
                        hashMap.put("02130449050", "PROMOSI ");
                        hashMap.put("087874901169", "PROMOSI ");
                        hashMap.put("02149051037", "PROMOSI ");
                        hashMap.put("08091401023", "PROMOSI ");
                        hashMap.put("08111713035", "PROMOSI ");
                        hashMap.put("02150651278", "PROMOSI ");
                        hashMap.put("081217934447", "PROMOSI ");
                        hashMap.put("0315324209", "PROMOSI ");
                        hashMap.put("081514863716", "PROMOSI ");
                        hashMap.put("0211500257", "PROMOSI ");
                        hashMap.put("02124180580", "PROMOSI ");
                        hashMap.put("02129950696", "PROMOSI ");
                        hashMap.put("08119706054", "PROMOSI ");
                        hashMap.put("02129716300", "PROMOSI ");
                        hashMap.put("08179825680", "PROMOSI ");
                        hashMap.put("082347354789", "PROMOSI ");
                        hashMap.put("06141000100", "PROMOSI ");
                        hashMap.put("02149050019", "PROMOSI ");
                        hashMap.put("02129706000", "PROMOSI ");
                        hashMap.put("0361220100", "PROMOSI ");
                        hashMap.put("02130422804", "PROMOSI ");
                        hashMap.put("04113666800", "PROMOSI ");
                        hashMap.put("085349289689", "PROMOSI ");
                        hashMap.put("02180640090", "PROMOSI ");
                        hashMap.put("+622125552200", "PROMOSI ");
                        hashMap.put("02180635999", "PROMOSI ");
                        hashMap.put("+626130018000", "PROMOSI ");
                        hashMap.put("02180635063", "PROMOSI ");
                        hashMap.put("02125695400", "PROMOSI ");
                        hashMap.put("02183787835", "PROMOSI ");
                        hashMap.put("02129539600", "PROMOSI ");
                        hashMap.put("02129705000", "PROMOSI ");
                        hashMap.put("02150627889", "PROMOSI ");
                        hashMap.put("02150533366", "PROMOSI ");
                        hashMap.put("02129934300", "PROMOSI ");
                        hashMap.put("02129802600", "PROMOSI ");
                        hashMap.put("02150368368", "PROMOSI ");
                        hashMap.put("0811016800", "PROMOSI ");
                        hashMap.put("02129911100", "PROMOSI ");
                        hashMap.put("+622124575300", "PROMOSI ");
                        hashMap.put("02121881000", "PROMOSI ");
                        hashMap.put("02149056800", "PROMOSI ");
                        hashMap.put("02121881500", "PROMOSI ");
                        hashMap.put("02129348400", "PROMOSI ");
                        hashMap.put("02130495777", "PROMOSI ");
                        hashMap.put("+622129265555", "PROMOSI ");
                        hashMap.put("02129494200", "PROMOSI ");
                        hashMap.put("02140005555", "PROMOSI ");
                        hashMap.put("02129653300", "PROMOSI ");
                        hashMap.put("02127625000", "PROMOSI ");
                        hashMap.put("02149054545", "PROMOSI ");
                        hashMap.put("02130413400", "PROMOSI ");
                        hashMap.put("02130413600", "PROMOSI ");
                        hashMap.put("02130069400", "PROMOSI ");
                        hashMap.put("+622129240800", "PROMOSI ");
                        hashMap.put("02180662500", "PROMOSI ");
                        hashMap.put("02130413500", "PROMOSI ");
                        hashMap.put("02130010811", "PROMOSI ");
                        hashMap.put("02130413800", "PROMOSI ");
                        hashMap.put("02130068460", "PROMOSI ");
                        hashMap.put("02150313123", "PROMOSI ");
                        hashMap.put("02180628500", "PROMOSI ");
                        hashMap.put("02149050010", "PROMOSI ");
                        hashMap.put("02180662400", "PROMOSI ");
                        hashMap.put("02129499400", "PROMOSI ");
                        hashMap.put("02128098000", "PROMOSI ");
                        hashMap.put("02125544200", "PROMOSI ");
                        hashMap.put("02150109393", "PROMOSI ");
                        hashMap.put("02129758800", "PROMOSI ");
                        hashMap.put("02150275500", "PROMOSI ");
                        hashMap.put("+622129554150", "PROMOSI ");
                        hashMap.put("02180631221", "PROMOSI ");
                        hashMap.put("02150501123", "PROMOSI ");
                        hashMap.put("+622125986000", "PROMOSI ");
                        hashMap.put("02129753100", "PROMOSI ");
                        hashMap.put("02180681268", "PROMOSI ");
                        hashMap.put("02150267889", "PROMOSI ");
                        hashMap.put("02127837500", "PROMOSI ");
                        hashMap.put("02180681046", "PROMOSI ");
                        hashMap.put("02180680320", "PROMOSI ");
                        hashMap.put("02129497600", "PROMOSI ");
                        hashMap.put("02129770301", "PROMOSI ");
                        hashMap.put("02129340829", "PROMOSI ");
                        hashMap.put("+622114005", "PROMOSI ");
                        hashMap.put("02129274198", "PROMOSI ");
                        hashMap.put("02129546400", "PROMOSI ");
                        hashMap.put("02129809100", "PROMOSI ");
                        hashMap.put("02140027878", "PROMOSI ");
                        hashMap.put("+622129915222", "PROMOSI ");
                        hashMap.put("02180662300", "PROMOSI ");
                        hashMap.put("02230003300", "PROMOSI ");
                        hashMap.put("02180682080", "PROMOSI ");
                        hashMap.put("02129962200", "PROMOSI ");
                        hashMap.put("08111530174", "PROMOSI ");
                        hashMap.put("02129808500", "PROMOSI ");
                        hashMap.put("02130494100", "PROMOSI ");
                        hashMap.put("02130413300", "PROMOSI ");
                        hashMap.put("02129752000", "PROMOSI ");
                        hashMap.put("02150606500", "PROMOSI ");
                        hashMap.put("02130414400", "PROMOSI ");
                        hashMap.put("02149050988", "PROMOSI ");
                        hashMap.put("02129752400", "PROMOSI ");
                        hashMap.put("02130413200", "PROMOSI ");
                        hashMap.put("02180645500", "PROMOSI ");
                        hashMap.put("0312880020", "PROMOSI ");
                        hashMap.put("+622129394200", "PROMOSI ");
                        hashMap.put("02129632500", "PROMOSI ");
                        hashMap.put("+622129546500", "PROMOSI ");
                        hashMap.put("02125536000", "PROMOSI ");
                        hashMap.put("02129785700", "PROMOSI ");
                        hashMap.put("02129752100", "PROMOSI ");
                        hashMap.put("02129534400", "PROMOSI ");
                        hashMap.put("02180635195", "PROMOSI ");
                        hashMap.put("0212918716", "PROMOSI ");
                        hashMap.put("02130413700", "PROMOSI ");
                        hashMap.put("02180635959", "PROMOSI ");
                        hashMap.put("02150291122", "PROMOSI ");
                        hashMap.put("02180681286", "PROMOSI ");
                        hashMap.put("+622129341829", "PROMOSI ");
                        hashMap.put("02180635907", "PROMOSI ");
                        hashMap.put("02128507300", "PROMOSI ");
                        hashMap.put("02140007777", "PROMOSI ");
                        hashMap.put("+622130404800", "PROMOSI ");
                        hashMap.put("02129784400", "PROMOSI ");
                        hashMap.put("02129264000", "PROMOSI ");
                        hashMap.put("02149050989", "PROMOSI ");
                        hashMap.put("02150500505", "PROMOSI ");
                        hashMap.put("02129978000", "PROMOSI ");
                        hashMap.put("02129241400", "PROMOSI ");
                        hashMap.put("02129752200", "PROMOSI ");
                        hashMap.put("08111728000", "PROMOSI ");
                        hashMap.put("02150605353", "PROMOSI ");
                        hashMap.put("02150500505", "PROMOSI ");
                        hashMap.put("02171673889", "PROMOSI ");
                        hashMap.put("02182570505", "PROMOSI ");
                        hashMap.put("02130010810", "PROMOSI ");
                        hashMap.put("+622129940115", "PROMOSI ");
                        hashMap.put("02149050998", "PROMOSI ");
                        hashMap.put("02129859000", "PROMOSI ");
                        hashMap.put("02130403300", "PROMOSI ");
                        hashMap.put("02145500046", "PROMOSI ");
                        hashMap.put("+622129554125", "PROMOSI ");
                        hashMap.put("02128095000", "PROMOSI ");
                        hashMap.put("02121888244", "PROMOSI ");
                        hashMap.put("02130010727", "PROMOSI ");
                        hashMap.put("02130003005", "PROMOSI ");
                        hashMap.put("02180671727", "PROMOSI ");
                        hashMap.put("02149054888", "PROMOSI ");
                        hashMap.put("02129187000", "PROMOSI ");
                        hashMap.put("02140427878", "PROMOSI ");
                        hashMap.put("02129940108", "PROMOSI ");
                        hashMap.put("02180613418", "PROMOSI ");
                        hashMap.put("02129351200", "PROMOSI ");
                        hashMap.put("+622130481930", "PROMOSI ");
                        hashMap.put("02180629161", "PROMOSI ");
                        hashMap.put("02129355100", "PROMOSI ");
                        hashMap.put("02180629575", "PROMOSI ");
                        hashMap.put("02130431500", "PROMOSI ");
                        hashMap.put("02150404888", "PROMOSI ");
                        hashMap.put("02121888211", "PROMOSI ");
                        hashMap.put("02180629644", "PROMOSI ");
                        hashMap.put("02130062100", "PROMOSI ");
                        hashMap.put("02130450100", "PROMOSI ");
                        hashMap.put("02129785800", "PROMOSI ");
                        hashMap.put("02129981800", "PROMOSI ");
                        hashMap.put("02180629533", "PROMOSI ");
                        hashMap.put("+622127535600", "PROMOSI ");
                        hashMap.put("02150113123", "PROMOSI ");
                        hashMap.put("02171673585", "PROMOSI ");
                        hashMap.put("02130431560", "PROMOSI ");
                        hashMap.put("02150529988", "PROMOSI ");
                        hashMap.put("02180680111", "PROMOSI ");
                        hashMap.put("02128098700", "PROMOSI ");
                        hashMap.put("02150515288", "PROMOSI ");
                        hashMap.put("02129969300", "PROMOSI ");
                        hashMap.put("02127836300", "PROMOSI ");
                        hashMap.put("02129958300", "PROMOSI ");
                        hashMap.put("02140101060", "PROMOSI ");
                        hashMap.put("02121888233", "PROMOSI ");
                        hashMap.put("02130003035", "PROMOSI ");
                        hashMap.put("02180681240", "PROMOSI ");
                        hashMap.put("03160005100", "PROMOSI ");
                        hashMap.put("08118167400", "PROMOSI ");
                        hashMap.put("02180680750", "PROMOSI ");
                        hashMap.put("02149054777", "PROMOSI ");
                        hashMap.put("02180681321", "PROMOSI ");
                        hashMap.put("02129494300", "PROMOSI ");
                        hashMap.put("02129911000", "PROMOSI ");
                        hashMap.put("02140090220", "PROMOSI ");
                        hashMap.put("02149055000", "PROMOSI ");
                        hashMap.put("02125531600", "PROMOSI ");
                        hashMap.put("02130483300", "PROMOSI ");
                        hashMap.put("02130067500", "PROMOSI ");
                        hashMap.put("02124103600", "PROMOSI ");
                        hashMap.put("02130053300", "PROMOSI ");
                        hashMap.put("02129940105", "PROMOSI ");
                        hashMap.put("02180625555", "PROMOSI ");
                        hashMap.put("02121888222", "PROMOSI ");
                        hashMap.put("08111535252", "PROMOSI ");
                        hashMap.put("02130000900", "PROMOSI ");
                        hashMap.put("02129758300", "PROMOSI ");
                        hashMap.put("02150304949", "PROMOSI ");
                        hashMap.put("02180625544", "PROMOSI ");
                        hashMap.put("02140300420", "PROMOSI ");
                        hashMap.put("02180633400", "PROMOSI ");
                        hashMap.put("0214000555", "PROMOSI ");
                        hashMap.put("02121884900", "PROMOSI ");
                        hashMap.put("02180635903", "PROMOSI ");
                        hashMap.put("02129274100", "PROMOSI ");
                        hashMap.put("02130005816", "PROMOSI ");
                        hashMap.put("02130010714", "PROMOSI ");
                        hashMap.put("02130431530", "PROMOSI ");
                        hashMap.put("02149054996", "PROMOSI ");
                        hashMap.put("02140101065", "PROMOSI ");
                        hashMap.put("02149054997", "PROMOSI ");
                        hashMap.put("02130485959", "PROMOSI ");
                        hashMap.put("02121885700", "PROMOSI ");
                        hashMap.put("+622130412800", "PROMOSI ");
                        hashMap.put("0614578221", "PROMOSI ");
                        hashMap.put("02180625060", "PROMOSI ");
                        hashMap.put("02129109200", "PROMOSI ");
                        hashMap.put("02130500900", "PROMOSI ");
                        hashMap.put("02180633499", "PROMOSI ");
                        hashMap.put("0215700420", "PROMOSI ");
                        hashMap.put("02129554130", "PROMOSI ");
                        hashMap.put("02129918200", "PROMOSI ");
                        hashMap.put("02130402929", "PROMOSI ");
                        hashMap.put("02130010802", "PROMOSI ");
                        hashMap.put("02129341300", "PROMOSI ");
                        hashMap.put("02121880600", "PROMOSI ");
                        hashMap.put("02180629889", "PROMOSI ");
                        hashMap.put("02140400420", "PROMOSI ");
                        hashMap.put("02149050757", "PROMOSI ");
                        hashMap.put("02130439800", "PROMOSI ");
                        hashMap.put("+622129554135", "PROMOSI ");
                        hashMap.put("02130412400", "PROMOSI ");
                        hashMap.put("02129758700", "PROMOSI ");
                        hashMap.put("+622129940127", "PROMOSI ");
                        hashMap.put("02121888255", "PROMOSI ");
                        hashMap.put("02129182151", "PROMOSI ");
                        hashMap.put("02140101404", "PROMOSI ");
                        hashMap.put("0315481213", "PROMOSI ");
                        hashMap.put("02140080420", "PROMOSI ");
                        hashMap.put("02129752300", "PROMOSI ");
                        hashMap.put("02130005848", "PROMOSI ");
                        hashMap.put("02123546000", "PROMOSI ");
                        hashMap.put("02127887388", "PROMOSI ");
                        hashMap.put("02130066470", "PROMOSI ");
                        hashMap.put("02130450111", "PROMOSI ");
                        hashMap.put("02129276100", "PROMOSI ");
                        hashMap.put("02140200420", "PROMOSI ");
                        hashMap.put("02150200941", "PROMOSI ");
                        hashMap.put("+622129354600", "PROMOSI ");
                        hashMap.put("02128098600", "PROMOSI ");
                        hashMap.put("02140470046", "PROMOSI ");
                        hashMap.put("02140009999", "PROMOSI ");
                        hashMap.put("021290758888", "PROMOSI ");
                        hashMap.put("02129819500", "PROMOSI ");
                        hashMap.put("+622123546400", "PROMOSI ");
                        hashMap.put("02129784800", "PROMOSI ");
                        hashMap.put("02150540050", "PROMOSI ");
                        hashMap.put("+622129940100", "PROMOSI ");
                        hashMap.put("02140100566", " PROMOSI ");
                        hashMap.put("+622125657400", " PROMOSI ");
                        hashMap.put("02140101402", " PROMOSI ");
                        hashMap.put("02129950600", " PROMOSI ");
                        hashMap.put("02150530234", " PROMOSI ");
                        hashMap.put("04113661200", " PROMOSI ");
                        hashMap.put("02180635966", " PROMOSI ");
                        hashMap.put("02129753200", " PROMOSI ");
                        hashMap.put("02180615555", " PROMOSI ");
                        hashMap.put("0251147", " PROMOSI ");
                        hashMap.put("02129714900", " PROMOSI ");
                        hashMap.put("02180635555", " PROMOSI ");
                        hashMap.put("08111716637", " PROMOSI ");
                        hashMap.put("08111534947", " PROMOSI ");
                        hashMap.put("02129958400", " PROMOSI ");
                        hashMap.put("02130010046", " PROMOSI ");
                        hashMap.put("02129273200", " PROMOSI ");
                        hashMap.put("02129494500", " PROMOSI ");
                        hashMap.put("02129852800", " PROMOSI ");
                        hashMap.put("02130482130", " PROMOSI ");
                        hashMap.put("02129554250", " PROMOSI ");
                        hashMap.put("02129758400", " PROMOSI ");
                        hashMap.put("02129539400", " PROMOSI ");
                        hashMap.put("02150550234", " PROMOSI ");
                        hashMap.put("02150590233", " PROMOSI ");
                        hashMap.put("02129341400", " PROMOSI ");
                        hashMap.put("07116059100", " PROMOSI ");
                        hashMap.put("02129773300", " PROMOSI ");
                        hashMap.put("+622125695733", " PROMOSI ");
                        hashMap.put("02129773250", " PROMOSI ");
                        hashMap.put("02129640400", " PROMOSI ");
                        hashMap.put("02129351500", " PROMOSI ");
                        hashMap.put("02130409888", " PROMOSI ");
                        hashMap.put("02129960099", " PROMOSI ");
                        hashMap.put("08999666999", " PROMOSI ");
                        hashMap.put("02129753400", " PROMOSI ");
                        hashMap.put("02129554110", " PROMOSI ");
                        hashMap.put("02127891100", " PROMOSI ");
                        hashMap.put("+622129960100", " PROMOSI ");
                        hashMap.put("02129274170", " PROMOSI ");
                        hashMap.put("02180635993", " PROMOSI ");
                        hashMap.put("02180671412", " PROMOSI ");
                        hashMap.put("02150660033", " PROMOSI ");
                        hashMap.put("02180671721", " PROMOSI ");
                        hashMap.put("02180680760", " PROMOSI ");
                        hashMap.put("02129753300", " PROMOSI ");
                        hashMap.put("02129958787", " PROMOSI ");
                        hashMap.put("02129752500", " PROMOSI ");
                        hashMap.put("02129494100", " PROMOSI ");
                        hashMap.put("021209900", " PROMOSI ");
                        hashMap.put("02129701300", " PROMOSI ");
                        hashMap.put("02140082001", " PROMOSI ");
                        hashMap.put("02125531500", " PROMOSI ");
                        hashMap.put("082880500046", " PROMOSI ");
                        hashMap.put("02125525900", " PROMOSI ");
                        hashMap.put("02140001155", " PROMOSI ");
                        hashMap.put("02130014700", " PROMOSI ");
                        hashMap.put("02130066471", " PROMOSI ");
                        hashMap.put("02127891200", " PROMOSI ");
                        hashMap.put("087888555000", " PROMOSI ");
                        hashMap.put("02150510100", " PROMOSI ");
                        hashMap.put("02150201180", " PROMOSI ");
                        hashMap.put("+622129496600", " PROMOSI ");
                        hashMap.put("02129851300", " PROMOSI ");
                        hashMap.put("08111750089", " PROMOSI ");
                        hashMap.put("02129940131", " PROMOSI ");
                        hashMap.put("02129940136", " PROMOSI ");
                        hashMap.put("02130403900", " PROMOSI ");
                        hashMap.put("+622129934200", " PROMOSI ");
                        hashMap.put("02180682039", " PROMOSI ");
                        hashMap.put("08118511118", " PROMOSI ");
                        hashMap.put("08118167253", " PROMOSI ");
                        hashMap.put("02130483600", " PROMOSI ");
                        hashMap.put("082817932111", " PROMOSI ");
                        hashMap.put("02129773200", " PROMOSI ");
                        hashMap.put("02130412700", " PROMOSI ");
                        hashMap.put("02129913600", " PROMOSI ");
                        hashMap.put("08111535821", " PROMOSI ");
                        hashMap.put("+622129940119", " PROMOSI ");
                        hashMap.put("08111534935", " PROMOSI ");
                        hashMap.put("021290759999", " PROMOSI ");
                        hashMap.put("0224271316", " PROMOSI ");
                        hashMap.put("02180681040", " PROMOSI ");
                        hashMap.put("02180681045", " PROMOSI ");
                        hashMap.put("0811150988", " PROMOSI ");
                        hashMap.put("02128095100", " PROMOSI ");
                        hashMap.put("02129292100", " PROMOSI ");
                        hashMap.put("02125987500", " PROMOSI ");
                        hashMap.put("02129534100", " PROMOSI ");
                        hashMap.put("021300058888", " PROMOSI ");
                        hashMap.put("02129706100", " PROMOSI ");
                        hashMap.put("0361728287", " PROMOSI ");
                        hashMap.put("02180633497", " PROMOSI ");
                        hashMap.put("08111714870", " PROMOSI ");
                        hashMap.put("02127893709", " PROMOSI ");
                        hashMap.put("089603002701", " PROMOSI ");
                        hashMap.put("02129103700", " PROMOSI ");
                        hashMap.put("+622129940111", " PROMOSI ");
                        hashMap.put("+622130483400", " PROMOSI ");
                        hashMap.put("02180633496", " PROMOSI ");
                        hashMap.put("02180868300", " PROMOSI ");
                        hashMap.put("+622129295400", " PROMOSI ");
                        hashMap.put("02140003046", " PROMOSI ");
                        hashMap.put("02140090221", " PROMOSI ");
                        hashMap.put("02129758200", " PROMOSI ");
                        hashMap.put("02180682170", " PROMOSI ");
                        hashMap.put("02129343500", " PROMOSI ");
                        hashMap.put("082880500046", " PROMOSI ");
                        hashMap.put("02180681322", " PROMOSI ");
                        hashMap.put("082880500060", " PROMOSI ");
                        hashMap.put("062315481213", " PROMOSI ");
                        hashMap.put("02130495400", " PROMOSI ");
                        hashMap.put("02129109300", " PROMOSI ");
                        hashMap.put("0216324456", " PROMOSI ");
                        hashMap.put("02140090222", " PROMOSI ");
                        hashMap.put("02127886200", " PROMOSI ");
                        hashMap.put("02129589898", " PROMOSI ");
                        hashMap.put("02129264001", " PROMOSI ");
                        hashMap.put("02150502083", " PROMOSI ");
                        hashMap.put("02140090223", " PROMOSI ");
                        hashMap.put("02149051019", " PROMOSI ");
                        hashMap.put("08111614041", " PROMOSI ");
                        hashMap.put("+622140800204", " PROMOSI ");
                        hashMap.put("02180682129", " PROMOSI ");
                        hashMap.put("02180682135", " PROMOSI ");
                        hashMap.put("02180625589", " PROMOSI ");
                        hashMap.put("02129773350", " PROMOSI ");
                        hashMap.put("02180633498", " PROMOSI ");
                        hashMap.put("02129493476", " PROMOSI ");
                        hashMap.put("08118108413", " PROMOSI ");
                        hashMap.put("08118108411", " PROMOSI ");
                        hashMap.put("02129819511", " PROMOSI ");
                        hashMap.put("02180682133", " PROMOSI ");
                        hashMap.put("02150540234", " PROMOSI ");
                        hashMap.put("02140090228", " PROMOSI ");
                        hashMap.put("+628981648888", " PROMOSI ");
                        hashMap.put("02149054747", " PROMOSI ");
                        hashMap.put("02140090239", "PROMOSI");
                        hashMap.put("02125544234", "PROMOSI");
                        hashMap.put("02150651352", "PROMOSI");
                        hashMap.put("02129494219", "PROMOSI");
                        hashMap.put("02140090237", "PROMOSI");
                        hashMap.put("02129972997", "PROMOSI");
                        hashMap.put("02180635805", "PROMOSI");
                        hashMap.put("29220200", "PROMOSI");
                        hashMap.put("02140090226", "PROMOSI");
                        hashMap.put("02140090232", "PROMOSI");
                        hashMap.put("02130069725", "PROMOSI");
                        hashMap.put("02129494218", "PROMOSI");
                        hashMap.put("0224271353", "PROMOSI");
                        hashMap.put("08111534939", "PROMOSI");
                        hashMap.put("02121885500", "PROMOSI");
                        hashMap.put("08118255811", "PROMOSI");
                        hashMap.put("021292655555", "PROMOSI");
                        hashMap.put("0224271357", "PROMOSI");
                        hashMap.put("02129648400", "PROMOSI");
                        hashMap.put("081213590741", "PROMOSI");
                        hashMap.put("02130065900", "PROMOSI");
                        hashMap.put("02129228200", "PROMOSI");
                        hashMap.put("02149051035", "PROMOSI");
                        hashMap.put("08118108400", "PROMOSI");
                        hashMap.put("02129706200", "PROMOSI");
                        hashMap.put("08118108448", "PROMOSI");
                        hashMap.put("02130069783", "PROMOSI");
                        hashMap.put("02129345000", "PROMOSI");
                        hashMap.put("02130069766", "PROMOSI");
                        hashMap.put("02129265000", "PROMOSI");
                        hashMap.put("08118108450", "PROMOSI");
                        hashMap.put("0224271326", "PROMOSI");
                        hashMap.put("02130069719", "PROMOSI");
                        hashMap.put("02129264031", "PROMOSI");
                        hashMap.put("02130481948", "PROMOSI");
                        hashMap.put("081514863714", "PROMOSI");
                        hashMap.put("02130069762", "PROMOSI");
                        hashMap.put("02186353922", "PROMOSI");
                        hashMap.put("02150200953", "PROMOSI");
                        hashMap.put("08118108459", "PROMOSI");
                        hashMap.put("08118108440", "PROMOSI");
                        hashMap.put("08119706090", "PROMOSI");
                        hashMap.put("+622130486300", "PROMOSI");
                        hashMap.put("088801000488", "PROMOSI");
                        hashMap.put("08118108458", "PROMOSI");
                        hashMap.put("021888233", "PROMOSI");
                        hashMap.put("0224267141", "PROMOSI");
                        hashMap.put("02129773356", "PROMOSI");
                        hashMap.put("02120001946", "PROMOSI");
                        hashMap.put("02130497900", "PROMOSI");
                        hashMap.put("02130481951", "PROMOSI");
                        hashMap.put("02140200355", "PROMOSI");
                        hashMap.put("02129972888", "PROMOSI");
                        hashMap.put("02744601000", "PROMOSI");
                        hashMap.put("02171673587", "PROMOSI");
                        hashMap.put("02129852811", "PROMOSI");
                        hashMap.put("08119706059", "PROMOSI");
                        hashMap.put("02130069772", "PROMOSI");
                        hashMap.put("08118108416", "PROMOSI");
                        hashMap.put("02129934234", "PROMOSI");
                        hashMap.put("02162310577", "PROMOSI");
                        hashMap.put("021301057948", "PROMOSI");
                        hashMap.put("081514863717", "PROMOSI");
                        hashMap.put("+622129263200", "PROMOSI");
                        hashMap.put("08118108363", "PROMOSI");
                        hashMap.put("02140800272", "PROMOSI");
                        hashMap.put("02140200386", "PROMOSI");
                        hashMap.put("0224271347", "PROMOSI");
                        hashMap.put("02129494294", "PROMOSI");
                        hashMap.put("081514863728", "PROMOSI");
                        hashMap.put("02129942888", "PROMOSI");
                        hashMap.put("02129494259", "PROMOSI");
                        hashMap.put("+622125515800", "PROMOSI");
                        hashMap.put("030450100", "PROMOSI");
                        hashMap.put("08118108348", "PROMOSI");
                        hashMap.put("02129265945", "PROMOSI");
                        hashMap.put("08118108407", "PROMOSI");
                        hashMap.put("081584441692", "PROMOSI");
                        hashMap.put("02129298425", "PROMOSI");
                        hashMap.put("02130412426", "PROMOSI");
                        hashMap.put("08118108418", "PROMOSI");
                        hashMap.put("08118108346", "PROMOSI");
                        hashMap.put("08118108364", "PROMOSI");
                        hashMap.put("02130062124", "PROMOSI");
                        hashMap.put("08159196800", "PROMOSI");
                        hashMap.put("081584441693", "PROMOSI");
                        hashMap.put("02129298359", "PROMOSI");
                        hashMap.put("02129393551", "PROMOSI");
                        hashMap.put("02130062110", "PROMOSI");
                        hashMap.put("02150101302", "PROMOSI");
                        hashMap.put("08159166800", "PROMOSI");
                        hashMap.put("02150101316", "PROMOSI");
                        hashMap.put("02149054374", "PROMOSI");
                        hashMap.put("02162310572", "PROMOSI");
                        hashMap.put("08161601646", "PROMOSI");
                        hashMap.put("+85229128038", "PROMOSI");
                        hashMap.put("081514863720", "PROMOSI");
                        hashMap.put("02130062130", "PROMOSI");
                        hashMap.put("02130067546", "PROMOSI");
                        hashMap.put("02130401852", "PROMOSI");
                        hashMap.put("02171673886", "PROMOSI");
                        hashMap.put("08159846800", "PROMOSI");
                        hashMap.put("08118108357", "PROMOSI");
                        hashMap.put("02130401834", "PROMOSI");
                        hashMap.put("02130407511", "PROMOSI");
                        hashMap.put("02127514777", "PROMOSI");
                        hashMap.put("08118108358", "PROMOSI");
                        hashMap.put("02129393587", "PROMOSI");
                        hashMap.put("02130401837", "PROMOSI");
                        hashMap.put("02129348845", "PROMOSI");
                        hashMap.put("02130407500", "PROMOSI");
                        hashMap.put("02150300134", "PROMOSI");
                        hashMap.put("08161601346", "PROMOSI");
                        hashMap.put("02130067514", "PROMOSI");
                        hashMap.put("02130412429", "PROMOSI");
                        hashMap.put("02150100738", "PROMOSI");
                        hashMap.put("08159856800", "PROMOSI");
                        hashMap.put("08118103861", "PROMOSI");
                        hashMap.put("053124395", "PROMOSI");
                        hashMap.put("08118671888", "PROMOSI");
                        hashMap.put("021300057939", "PROMOSI");
                        hashMap.put("08118108410", "PROMOSI");
                        hashMap.put("02129265961", "PROMOSI");
                        hashMap.put("08161600046", "PROMOSI");
                        hashMap.put("02129393640", "PROMOSI");
                        hashMap.put("02137731617", "PROMOSI");
                        hashMap.put("02125572277", "PROMOSI");
                        hashMap.put("081514863726", "PROMOSI");
                        hashMap.put("08119706055", "PROMOSI");
                        hashMap.put("02129348843", "PROMOSI");
                        hashMap.put("02180681133", "PROMOSI");
                        hashMap.put("0213807380", "PROMOSI");
                        hashMap.put("02129276200", "PROMOSI");
                        hashMap.put("08118758900", "PROMOSI");
                        hashMap.put("08111714993", "PROMOSI");
                        hashMap.put("0212300575", "PROMOSI");
                        hashMap.put("028098000", "PROMOSI");
                        hashMap.put("02129050204", "PROMOSI");
                        hashMap.put("0818890227", "PROMOSI");
                        hashMap.put("03133100183", "PROMOSI");
                        hashMap.put("082817068466", "PROMOSI");
                        hashMap.put("08119706049", "PROMOSI");
                        hashMap.put("02180898994", "PROMOSI");
                        hashMap.put("076124050", "PROMOSI");
                        hashMap.put("02129122254", "PROMOSI");
                        hashMap.put("081515321101", "PROMOSI");
                        hashMap.put("02129293915", "PROMOSI");
                        hashMap.put("0217696008", "PROMOSI");
                        hashMap.put("02129348899", "PROMOSI");
                        hashMap.put("08965388777", "PROMOSI");
                        hashMap.put("02130481300", "PROMOSI");
                        hashMap.put("02129940102", "PROMOSI");
                        hashMap.put("0811066881", "UNKNOWN");
                        hashMap.put("02140101401", "UNKNOWN");
                        hashMap.put("02180635811", "UNKNOWN");
                        hashMap.put("02180680033", "UNKNOWN");
                        hashMap.put("0000000000", "UNKNOWN");
                        hashMap.put("02129552698", "UNKNOWN");
                        hashMap.put("02130033012", "UNKNOWN");
                        hashMap.put("02130010710", "UNKNOWN");
                        hashMap.put("02124155551", "UNKNOWN");
                        hashMap.put("+622287351000", "UNKNOWN");
                        hashMap.put("02130001620", "UNKNOWN");
                        hashMap.put("02130421300", "UNKNOWN");
                        hashMap.put("+622127548000", "UNKNOWN");
                        hashMap.put("02150541505", "UNKNOWN");
                        hashMap.put("02150575288", "UNKNOWN");
                        hashMap.put("0211500861", "UNKNOWN");
                        hashMap.put("02129493403", "UNKNOWN");
                        hashMap.put("0895347181201", "UNKNOWN");
                        hashMap.put("+625423002000", "UNKNOWN");
                        hashMap.put("0312930120", "UNKNOWN");
                        hashMap.put("+62811038800", "UNKNOWN");
                        hashMap.put("0811071803", "UNKNOWN");
                        hashMap.put("02150200952", "UNKNOWN");
                        hashMap.put("08111716303", "UNKNOWN");
                        hashMap.put("02157957688", "UNKNOWN");
                        hashMap.put("02129808400", "UNKNOWN");
                        hashMap.put("02150570050", "UNKNOWN");
                        hashMap.put("02180682400", "UNKNOWN");
                        hashMap.put("0210000007", "UNKNOWN");
                        hashMap.put("0254369500", "UNKNOWN");
                        hashMap.put("02180680011", "UNKNOWN");
                        hashMap.put("02129810999", "UNKNOWN");
                        hashMap.put("02129532500", "UNKNOWN");
                        hashMap.put("022455446006", "UNKNOWN");
                        hashMap.put("0211500127", "UNKNOWN");
                        hashMap.put("08001262526", "UNKNOWN");
                        hashMap.put("0211500141", "UNKNOWN");
                        hashMap.put("02179184491", "UNKNOWN");
                        hashMap.put("02180682070", "UNKNOWN");
                        hashMap.put("02149051025", "UNKNOWN");
                        hashMap.put("02124180572", "UNKNOWN");
                        hashMap.put("03160001953", "UNKNOWN");
                        hashMap.put("08091234009", "UNKNOWN");
                        hashMap.put("082132390111", "UNKNOWN");
                        hashMap.put("02150651298", "UNKNOWN");
                        hashMap.put("02129651700", "UNKNOWN");
                        hashMap.put("02129633400", "UNKNOWN");
                        hashMap.put("0811156562", "UNKNOWN");
                        hashMap.put("08091009999", "UNKNOWN");
                        hashMap.put("02130001254", "UNKNOWN");
                        hashMap.put("02130001278", "UNKNOWN");
                        hashMap.put("02157932928", "UNKNOWN");
                        hashMap.put("087874986184", "UNKNOWN");
                        hashMap.put("02150135710", "UNKNOWN");
                        hashMap.put("08119599350", "UNKNOWN");
                        hashMap.put("02129236557", "UNKNOWN");
                        hashMap.put("+62411841146", "UNKNOWN");
                        hashMap.put("022455446005", "UNKNOWN");
                        hashMap.put("02180635933", "UNKNOWN");
                        hashMap.put("03160001950", "UNKNOWN");
                        hashMap.put("02126585858", "UNKNOWN");
                        hashMap.put("02129950640", "UNKNOWN");
                        hashMap.put("02180631000", "UNKNOWN");
                        hashMap.put("02130401440", "UNKNOWN");
                        hashMap.put("089529568017", "UNKNOWN");
                        hashMap.put("082126952172", "UNKNOWN");
                        hashMap.put("02180681292", "UNKNOWN");
                        hashMap.put("02129274158", "UNKNOWN");
                        hashMap.put("02129274155", "UNKNOWN");
                        hashMap.put("02516900526", "UNKNOWN");
                        hashMap.put("0215523239", "UNKNOWN");
                        hashMap.put("03160001919", "UNKNOWN");
                        hashMap.put("02130430111", "UNKNOWN");
                        hashMap.put("02129293976", "UNKNOWN");
                        hashMap.put("08551340046", "UNKNOWN");
                        hashMap.put("08119557239", "UNKNOWN");
                        hashMap.put("02129278674", "UNKNOWN");
                        hashMap.put("02123506113", "UNKNOWN");
                        hashMap.put("085891748090", "UNKNOWN");
                        hashMap.put("02129278634", "UNKNOWN");
                        hashMap.put("02129912012", "UNKNOWN");
                        hashMap.put("030414400", "UNKNOWN");
                        hashMap.put("085290923726", "UNKNOWN");
                        hashMap.put("081355577676", "UNKNOWN");
                        hashMap.put("02130450197", "UNKNOWN");
                        hashMap.put("085883430219", "UNKNOWN");
                        hashMap.put("082280384272", "UNKNOWN");
                        hashMap.put("02130412407", "UNKNOWN");
                        hashMap.put("02130014535", "UNKNOWN");
                        hashMap.put("02129278581", "UNKNOWN");
                        hashMap.put("081289974078", "UNKNOWN");
                        hashMap.put("08179825672", "UNKNOWN");
                        hashMap.put("0816218738", "UNKNOWN");
                        hashMap.put("02122083958", "UNKNOWN");
                        hashMap.put("081218253951", "UNKNOWN");
                        hashMap.put("0895353991095", "UNKNOWN");
                        hashMap.put("0214208614", "UNKNOWN");
                        hashMap.put("02173450533", "UNKNOWN");
                        hashMap.put("08118103796", "UNKNOWN");
                        hashMap.put("087897778976", "UNKNOWN");
                        hashMap.put("02171796731", "UNKNOWN");
                        hashMap.put("085664003809", "UNKNOWN");
                        hashMap.put("02287346473", "UNKNOWN");
                        hashMap.put("087814037952", "UNKNOWN");
                        hashMap.put("0811201041", "UNKNOWN");
                        hashMap.put("081281809204", "UNKNOWN");
                        hashMap.put("02125572230", "UNKNOWN");
                        hashMap.put("085358305822", "UNKNOWN");
                        hashMap.put("081617557713", "UNKNOWN");
                        hashMap.put("085693310135", "UNKNOWN");
                        hashMap.put("082817059579", "UNKNOWN");
                        hashMap.put("085275077594", "UNKNOWN");
                        hashMap.put("081217818684", "UNKNOWN");
                        hashMap.put("0895364885676", "UNKNOWN");
                        hashMap.put("02129294688", "UNKNOWN");
                        hashMap.put("+622129493400", "UNKNOWN");
                        hashMap.put("0210000007", "UNKNOWN");
                        hashMap.put("+622157957699", "UNKNOWN");
                        hashMap.put("02151339510", "UNKNOWN");
                        hashMap.put("+62312880011", "UNKNOWN");
                        hashMap.put("077800007", "UNKNOWN");
                        hashMap.put("02129493422", "UNKNOWN");
                        hashMap.put("08091911911", "UNKNOWN");
                        hashMap.put("+622129981400", "UNKNOWN");
                        hashMap.put("+622149050505", "UNKNOWN");
                        hashMap.put("02153655135", "UNKNOWN");
                        hashMap.put("02140003333", "UNKNOWN");
                        hashMap.put("0310000007", "UNKNOWN");
                        hashMap.put("02518308600", "UNKNOWN");
                        hashMap.put("+622129293900", "UNKNOWN");
                        hashMap.put("+0210000007", "UNKNOWN");
                        hashMap.put("02130499810", "UNKNOWN");
                        hashMap.put("02129546700", "UNKNOWN");
                        hashMap.put("+622130487450", "UNKNOWN");
                        hashMap.put("082817938888", "UNKNOWN");
                        hashMap.put("08033218184", "UNKNOWN");
                        hashMap.put("0224556677", "UNKNOWN");
                        hashMap.put("02129963500", "UNKNOWN");
                        hashMap.put("02180629545", "UNKNOWN");
                        hashMap.put("085737752699", "UNKNOWN");
                        hashMap.put("02180662121", "UNKNOWN");
                        hashMap.put("02129264111", "UNKNOWN");
                        hashMap.put("08976068455", "UNKNOWN");
                        hashMap.put("02129960034", "UNKNOWN");
                        hashMap.put("089653986226", "UNKNOWN");
                        hashMap.put("08091123123", "UNKNOWN");
                        hashMap.put("082327547165", "UNKNOWN");
                        hashMap.put("02129278656", "UNKNOWN");
                        hashMap.put("085311798491", "UNKNOWN");
                        hashMap.put("08091401027", "UNKNOWN");
                        hashMap.put("081343876829", "UNKNOWN");
                        hashMap.put("085225885999", "UNKNOWN");
                        hashMap.put("02125500900", "UNKNOWN");
                        hashMap.put("02180861111", "UNKNOWN");
                        hashMap.put("081299371446", "UNKNOWN");
                        hashMap.put("0895701737609", "UNKNOWN");
                        hashMap.put("081291005565", "UNKNOWN");
                        hashMap.put("0213103680", "UNKNOWN");
                        hashMap.put("02180615170", "UNKNOWN");
                        hashMap.put("02180680222", "UNKNOWN");
                        hashMap.put("06141000700", "UNKNOWN");
                        hashMap.put("08211465143", "UNKNOWN");
                        hashMap.put("+622152914800", "UNKNOWN");
                        hashMap.put("02516900505", "UNKNOWN");
                        hashMap.put("0214600104", "UNKNOWN");
                        hashMap.put("02140800240", "UNKNOWN");
                        hashMap.put("0248664598", "UNKNOWN");
                        hashMap.put("02130056171", "UNKNOWN");
                        hashMap.put("081911766075", "UNKNOWN");
                        hashMap.put("08568794350", "UNKNOWN");
                        hashMap.put("08119706021", "UNKNOWN");
                        hashMap.put("082317681256", "UNKNOWN");
                        hashMap.put("02171315000", "UNKNOWN");
                        hashMap.put("+622130401400", "UNKNOWN");
                        hashMap.put("02140300125", "UNKNOWN");
                        hashMap.put("02140001276", "UNKNOWN");
                        hashMap.put("02129539656", "UNKNOWN");
                        hashMap.put("03160007500", "UNKNOWN");
                        hashMap.put("02130401412", "UNKNOWN");
                        hashMap.put("02130489000", "UNKNOWN");
                        hashMap.put("+622125509800", "UNKNOWN");
                        hashMap.put("02124155507", "UNKNOWN");
                        hashMap.put("02130009534", "UNKNOWN");
                        hashMap.put("02180631251", "UNKNOWN");
                        hashMap.put("02433000800", "UNKNOWN");
                        hashMap.put("02180681269", "UNKNOWN");
                        hashMap.put("02125559200", "UNKNOWN");
                        hashMap.put("02150540505", "UNKNOWN");
                        hashMap.put("02129931999", "UNKNOWN");
                        hashMap.put("02129940336", "UNKNOWN");
                        hashMap.put("02140800245", "UNKNOWN");
                        hashMap.put("02129934238", "UNKNOWN");
                        hashMap.put("0335771967", "UNKNOWN");
                        hashMap.put("0542740845", "UNKNOWN");
                        hashMap.put("02130440330", "UNKNOWN");
                        hashMap.put("02130010640", "UNKNOWN");
                        hashMap.put("02124155550", "UNKNOWN");
                        hashMap.put("+622433001100", "UNKNOWN");
                        hashMap.put("02130009525", "UNKNOWN");
                        hashMap.put("029972400", "UNKNOWN");
                        hashMap.put("02129293000", "UNKNOWN");
                        hashMap.put("+622129294600", "UNKNOWN");
                        hashMap.put("02124155560", "UNKNOWN");
                        hashMap.put("02125537000", "UNKNOWN");
                        hashMap.put("02150209393", "UNKNOWN");
                        hashMap.put("02130023002", "UNKNOWN");
                        hashMap.put("02129771300", "UNKNOWN");
                        hashMap.put("02129493479", "UNKNOWN");
                        hashMap.put("02230001030", "UNKNOWN");
                        hashMap.put("02129713400", "UNKNOWN");
                        hashMap.put("02124155509", "UNKNOWN");
                        hashMap.put("06130014000", "UNKNOWN");
                        hashMap.put("02486460000", "UNKNOWN");
                        hashMap.put("02129916888", "UNKNOWN");
                        hashMap.put("+622129985800", "UNKNOWN");
                        hashMap.put("02130491991", "UNKNOWN");
                        hashMap.put("02129275600", "UNKNOWN");
                        hashMap.put("03160001200", "UNKNOWN");
                        hashMap.put("02180635994", "UNKNOWN");
                        hashMap.put("08118152999", "UNKNOWN");
                        hashMap.put("02150500233", "UNKNOWN");
                        hashMap.put("02150593505", "UNKNOWN");
                        hashMap.put("02150533505", "UNKNOWN");
                        hashMap.put("02129916800", "UNKNOWN");
                        hashMap.put("03128953700", "UNKNOWN");
                        hashMap.put("02129916999", "UNKNOWN");
                        hashMap.put("08111874094", "UNKNOWN");
                        hashMap.put("03128953500", "UNKNOWN");
                        hashMap.put("08119554100", "UNKNOWN");
                        hashMap.put("08118699673", "UNKNOWN");
                        hashMap.put("02130406726", "UNKNOWN");
                        hashMap.put("08118103729", "UNKNOWN");
                        hashMap.put("08118167853", "UNKNOWN");
                        hashMap.put("02433000611", "UNKNOWN");
                        hashMap.put("02130401408", "UNKNOWN");
                        hashMap.put("02227331204", "UNKNOWN");
                        hashMap.put("02486459526", "UNKNOWN");
                        hashMap.put("0215153021", "UNKNOWN");
                        hashMap.put("08979911222", "UNKNOWN");
                        hashMap.put("04113691040", "UNKNOWN");
                        hashMap.put("08111715012", "UNKNOWN");
                        hashMap.put("02127836400", "UNKNOWN");
                        hashMap.put("+622123526900", "UNKNOWN");
                        hashMap.put("089508335538", "UNKNOWN");
                        hashMap.put("+622129346400", "UNKNOWN");
                        hashMap.put("02534293454", "UNKNOWN");
                        hashMap.put("08111355000", "UNKNOWN");
                        hashMap.put("+911204648160", "UNKNOWN");
                        hashMap.put("02149055555", "UNKNOWN");
                        hashMap.put("02180635900", "UNKNOWN");
                        hashMap.put("03412891100", "UNKNOWN");
                        hashMap.put("021500250", "UNKNOWN");
                        hashMap.put("02130485930", "UNKNOWN");
                        hashMap.put("+622150117033", "UNKNOWN");
                        hashMap.put("02209900", "UNKNOWN");
                        hashMap.put("08119599344", "UNKNOWN");
                        hashMap.put("0217167007", "UNKNOWN");
                        hashMap.put("0811007805", "UNKNOWN");
                        hashMap.put("02150200901", "UNKNOWN");
                        hashMap.put("021000007", "UNKNOWN");
                        hashMap.put("08091401054", "UNKNOWN");
                        hashMap.put("0243344555", "UNKNOWN");
                        hashMap.put("02150651226", "UNKNOWN");
                        hashMap.put("+6282817011150", "UNKNOWN");
                        hashMap.put("08091401053", "UNKNOWN");
                        hashMap.put("03128935300", "UNKNOWN");
                        hashMap.put("082324929343", "UNKNOWN");
                        hashMap.put("08988129996", "UNKNOWN");
                        hashMap.put("08119597790", "UNKNOWN");
                        hashMap.put("02130412600", "UNKNOWN");
                        hashMap.put("02130033013", "UNKNOWN");
                        hashMap.put("0811010802", "UNKNOWN");
                        hashMap.put("029294114", "UNKNOWN");
                        hashMap.put("082327374943", "UNKNOWN");
                        hashMap.put("08091017001", "UNKNOWN");
                        hashMap.put("085311798490", "UNKNOWN");
                        hashMap.put("02130412500", "UNKNOWN");
                        hashMap.put("+628988988999", "UNKNOWN");
                        hashMap.put("085225876281", "UNKNOWN");
                        hashMap.put("0816341619", "UNKNOWN");
                        hashMap.put("08118108345", "UNKNOWN");
                        hashMap.put("02183486255", "UNKNOWN");
                        hashMap.put("085151488482", "UNKNOWN");
                        hashMap.put("0812063330", "UNKNOWN");
                        hashMap.put("085225876236", "UNKNOWN");
                        hashMap.put("087794390805", "UNKNOWN");
                        hashMap.put("02140458108", "UNKNOWN");
                        hashMap.put("085335868584", "UNKNOWN");
                        hashMap.put("081284643888", "UNKNOWN");
                        hashMap.put("081212223533", "UNKNOWN");
                        hashMap.put("085371555999", "UNKNOWN");
                        hashMap.put("087738434732", "UNKNOWN");
                        hashMap.put("081289015847", "UNKNOWN");
                        hashMap.put("02199284000", "UNKNOWN");
                        hashMap.put("082280606096", "UNKNOWN");
                        hashMap.put("02122983990", "UNKNOWN");
                        hashMap.put("081316332105", "UNKNOWN");
                        hashMap.put("085647777838", "UNKNOWN");
                        hashMap.put("081298801010", "UNKNOWN");
                        hashMap.put("081276262346", "UNKNOWN");
                        hashMap.put("082247839920", "UNKNOWN");
                        hashMap.put("085762133002", "UNKNOWN");
                        hashMap.put("081361202467", "UNKNOWN");
                        hashMap.put("02121880666", "UNKNOWN");
                        hashMap.put("08568799063", "UNKNOWN");
                        hashMap.put("081354888137", "UNKNOWN");
                        hashMap.put("085212286525", "UNKNOWN");
                        hashMap.put("081361611799", "UNKNOWN");
                        hashMap.put("081297468007", "UNKNOWN");
                        hashMap.put("081369513296", "UNKNOWN");
                        hashMap.put("082344646996", "UNKNOWN");
                        hashMap.put("0895605195777", "UNKNOWN");
                        hashMap.put("087855913803", "UNKNOWN");
                        hashMap.put("085335474359", "UNKNOWN");
                        hashMap.put("02171670077", "UNKNOWN");
                        hashMap.put("02123546450", "UNKNOWN");
                        hashMap.put("02149055088", "UNKNOWN");
                        hashMap.put("02125676000", "UNKNOWN");
                        hashMap.put("0811821088", "UNKNOWN");

                        String label = hashMap.get(pengirimDB);

                        if(label == null){
                            RequestInterface request = RequestInterface.retrofit.create(RequestInterface.class);
                            Call<DataSms> call = request.classify(isiPesan);
                            call.enqueue(new Callback<DataSms>() {
                                @Override
                                public void onResponse(Call<DataSms> call, Response<DataSms> response) {
                                    classified_sms = response.body().getLabel();
                                    Log.d("Label", classified_sms);
                                }

                                @Override
                                public void onFailure(Call<DataSms> call, Throwable t) {
                                    t.printStackTrace();
                                    Log.e("Error", t.getMessage());
                                }
                            });
                            if(classified_sms != null){
                                tv.setText(pengirimDB + " - " + classified_sms);
                            }
                            else {
                                tv.setText(pengirimDB + " - " + "UNKNOWN");
                            }
                        }
                        else {
                            tv.setText(pengirimDB + " - " + label);
                        }

                        Log.d("PENGIRIM: ", pengirimDB + " = " + label);
                    }
                    cur.close();
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

                if(columnIndex==12){
                    TextView tv = (TextView) view;
                    int maxlength = 40;

                    Log.d("ISI PESAN", isiPesan);

//                    trim the string into only 60 chars
                    if(isiPesan.length() > maxlength){
                        String res = isiPesan.substring(0, Math.min(isiPesan.length(), maxlength)) + "...";
                        tv.setText(res);
                    }
                    else {
                        tv.setText(isiPesan);
                    }
                    return true;
                }
                return false;
            }
        });

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

                Intent click = new Intent(MainActivity.this, MessageDetail.class);

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
                //click.putExtra("label", label);
                Intent i = getIntent();
                click.putExtra("asal", i.getStringExtra("tipepesan"));
                startActivity(click);
            }
        });
    }

    public void updateInbox() {
        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        dataAdapter.notifyDataSetChanged();
    }

    public void onBuatPesan(View view){
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivity(intent);
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.inbox:
                        drawerLayout.closeDrawers();
                        Intent inbox = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(inbox);
                        break;
                    case R.id.outbox:
                        drawerLayout.closeDrawers();
                        Intent outbox = new Intent(MainActivity.this, SentBox.class);
                        startActivity(outbox);
                        break;
                    case R.id.trending:
                        drawerLayout.closeDrawers();
                        Intent trending = new Intent(MainActivity.this, TrendingActivity.class);
                        startActivity(trending);
                        break;
                    case R.id.help:
                        drawerLayout.closeDrawers();
                        Intent help = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(help);
                        break;
                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close){

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
