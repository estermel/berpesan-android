package com.berpesan.activity;

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

import com.berpesan.R;

public class HelpActivity extends AppCompatActivity {

    TextView petunjuk, privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        petunjuk = (TextView) findViewById(R.id.petunjuk);

        privacy_policy = (TextView) findViewById(R.id.privacy_policy);

        petunjuk.setText("\n\n\n" +
                "SMS yang dikegorikan adalah SMS/pesan masuk dibagi ke dalam 6 label/kategori/kelompok:" +
                "\n\n" +
                "1. HAM\n" +
                "Label yang diberikan kepada SMS yang berasal dari pengirim yang nomornya tersimpan pada kontak atau isi pesannya berisi pesan tanpa promosi, penipuan, operator dan banking.\n\n" +
                "2. PROMOSI\n" +
                "Label yang diberikan kepada SMS yang isi pesannya berisi pesan promosi.\n\n" +
                "3. BANKING\n" +
                "Label yang diberikan kepada SMS yang pengirimnya berasal dari bank atau isi pesannya berisi pesan banking.\n\n" +
                "4. OPERATOR\n" +
                "Label yang diberikan kepada SMS yang pengirimnya berasal dari operator telepon, website atau perusahaan yang mengirimkan kode verifikasi.\n\n" +
                "5. PENIPUAN\n" +
                "Label yang diberikan kepada SMS yang pengirimnya tidak dikenali dan isi pesannya kemungkinan mengandung pesan penipuan.\n\n" +
                "6. UNKNOWN\n" +
                "Label yang diberikan kepada SMS yang pengirimnya tidak dikenali dan isi pesannya tidak tergolong ke dalam kategori ham, promosi, banking, operator maupun penipuan.");

        privacy_policy.setText(
                " \n\n\n" +
                "Information Collection\n" +
                " \n" +
                "Information Usage\n" +
                " \n" +
                "Information Protection\n" +
                " \n" +
                "Cookie Usage\n" +
                " \n" +
                "3rd Party Disclosure\n" +
                " \n" +
                "3rd Party Links\n" +
                " \n" +
                " \n" +
                "Google AdSense\n" +
                " \n" +
                "Fair Information Practices\n" +
                "Fair information\n" +
                "Practices\n" +
                " \n" +
                "COPPA\n" +
                " \n" +
                "CalOPPA\n" +
                " \n" +
                "Our Contact Information\n" +
                "This privacy policy has been compiled to better serve those who are concerned with how their 'Personally Identifiable Information' (PII) is being used online. PII, as described in US privacy law and information security, is information that can be used on its own or with other information to identify, contact, or locate a single person, or to identify an individual in context. Please read our privacy policy carefully to get a clear understanding of how we collect, use, protect or otherwise handle your Personally Identifiable Information in accordance with our website.\n" +
                " \n" +
                "What personal information do we collect from the people that visit our app?\n" +
                " \n" +
                " \n" +
                "When using our app (Berpesan), as appropriate, you agree to send your incoming messages (SMS) to our server to be processed and return you a “label” or “category” as the result or information for your incoming messages.\n" +
                " \n" +
                "When do we collect information?\n" +
                " \n" +
                " \n" +
                "We collect information (incoming messages) from you when you use Berpesan app and services.\n" +
                " \n" +
                "How do we use your information?\n" +
                " \n" +
                " \n" +
                "We may use the information we collect from you when you use our app and connected to the Internet:\n" +
                "      • To quickly process your information and return it as a result for labeling your incoming message whether it is a ham, promotional, banking, fraud or unknown message.\n" +
                " \n" +
                "How do we protect your information?\n" +
                " \n" +
                " \n" +
                "We do not use vulnerability scanning and/or scanning to PCI standards.\n" +
                "We only provide articles and information. We never ask for credit card numbers.\n" +
                "We do not use Malware Scanning.\n" +
                "We do not use an SSL certificate\n" +
                "      • We only provide articles and information. We never ask for personal or private information like names, email addresses, or credit card numbers.\n" +
                " \n" +
                "Do we use 'cookies'?\n" +
                " \n" +
                " \n" +
                "We do not use cookies for tracking purposes\n" +
                " \n" +
                "You can choose to have your computer warn you each time a cookie is being sent, or you can choose to turn off all cookies. You do this through your browser settings. Since browser is a little different, look at your browser's Help Menu to learn the correct way to modify your cookies.\n" +
                " \n" +
                "If you turn cookies off, Some of the features that make your site experience more efficient may not function properly.that make your site experience more efficient and may not function properly.\n" +
                " \n" +
                " \n" +
                "Third-party disclosure\n" +
                " \n" +
                " \n" +
                "We do not sell, trade, or otherwise transfer to outside parties your Personally Identifiable Information.\n" +
                " \n" +
                "Third-party links\n" +
                " \n" +
                " \n" +
                "We do not include or offer third-party products or services on our website.\n" +
                " \n" +
                "Google\n" +
                " \n" +
                " \n" +
                "Google's advertising requirements can be summed up by Google's Advertising Principles. They are put in place to provide a positive experience for users. https://support.google.com/adwordspolicy/answer/1316548?hl=en\n" +
                "We use Google AdSense Advertising on our website.\n" +
                " \n" +
                "Google, as a third-party vendor, uses cookies to serve ads on our site. Google's use of the DART cookie enables it to serve ads to our users based on previous visits to our site and other sites on the Internet. Users may opt-out of the use of the DART cookie by visiting the Google Ad and Content Network privacy policy.\n" +
                " \n" +
                "We have implemented the following:\n" +
                "      • Demographics and Interests Reporting\n" +
                " \n" +
                "We, along with third-party vendors such as Google use first-party cookies (such as the Google Analytics cookies) and third-party cookies (such as the DoubleClick cookie) or other third-party identifiers together to compile data regarding user interactions with ad impressions and other ad service functions as they relate to our website.\n" +
                " \n" +
                "Opting out:\n" +
                "Users can set preferences for how Google advertises to you using the Google Ad Settings page. Alternatively, you can opt out by visiting the Network Advertising Initiative Opt Out page or by using the Google Analytics Opt Out Browser add on.\n" +
                " \n" +
                "California Online Privacy Protection Act\n" +
                " \n" +
                " \n" +
                "CalOPPA is the first state law in the nation to require commercial websites and online services to post a privacy policy. The law's reach stretches well beyond California to require any person or company in the United States (and conceivably the world) that operates websites collecting Personally Identifiable Information from California consumers to post a conspicuous privacy policy on its website stating exactly the information being collected and those individuals or companies with whom it is being shared. - See more at: http://consumercal.org/california-online-privacy-protection-act-caloppa/#sthash.0FdRbT51.dpuf\n" +
                " \n" +
                "According to CalOPPA, we agree to the following:\n" +
                "Users can visit our site anonymously.\n" +
                "Once this privacy policy is created, we will add a link to it on our home page or as a minimum, on the first significant page after entering our website.\n" +
                "Our Privacy Policy link includes the word 'Privacy' and can easily be found on the page specified above.\n" +
                " \n" +
                "You will be notified of any Privacy Policy changes:\n" +
                "      • On our Privacy Policy Page\n" +
                "Can change your personal information:\n" +
                "      • By logging in to your account\n" +
                " \n" +
                "How does our site handle Do Not Track signals?\n" +
                "We honor Do Not Track signals and Do Not Track, plant cookies, or use advertising when a Do Not Track (DNT) browser mechanism is in place.\n" +
                " \n" +
                "Does our site allow third-party behavioral tracking?\n" +
                "It's also important to note that we do not allow third-party behavioral tracking\n" +
                " \n" +
                "COPPA (Children Online Privacy Protection Act)\n" +
                " \n" +
                " \n" +
                "When it comes to the collection of personal information from children under the age of 13 years old, the Children's Online Privacy Protection Act (COPPA) puts parents in control. The Federal Trade Commission, United States' consumer protection agency, enforces the COPPA Rule, which spells out what operators of websites and online services must do to protect children's privacy and safety online.\n" +
                "We do not specifically market to children under the age of 13 years old.\n" +
                " \n" +
                "Fair Information Practices\n" +
                " \n" +
                " \n" +
                "The Fair Information Practices Principles form the backbone of privacy law in the United States and the concepts they include have played a significant role in the development of data protection laws around the globe. Understanding the Fair Information Practice Principles and how they should be implemented is critical to comply with the various privacy laws that protect personal information.\n" +
                "In order to be in line with Fair Information Practices we will take the following responsive action, should a data breach occur:\n" +
                "We will notify the users via in-site notification\n" +
                "      • Within 7 business days\n" +
                " \n" +
                "We also agree to the Individual Redress Principle which requires that individuals have the right to legally pursue enforceable rights against data collectors and processors who fail to adhere to the law. This principle requires not only that individuals have enforceable rights against data users, but also that individuals have recourse to courts or government agencies to investigate and/or prosecute non-compliance by data processors.\n" +
                " \n" +
                " \n" +
                "Contacting Us\n" +
                " \n" +
                " \n" +
                "If there are any questions regarding this privacy policy, you may contact us using the information below.\n" +
                "Berpesan\n" +
                "Del Institute of Technology\n" +
                "Sitoluama, Toba Samosir, North Sumatra 22381\n" +
                "Indonesia\n" +
                "estermel41@gmail.com\n" +
                " \n" +
                "May 2017\n");


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        return;
    }
}
