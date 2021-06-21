package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.ammar.tawseel.R;

public class FamilyWebSiteActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_web_site);
        WebView wb =findViewById(R.id.webView1);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl("https://osratie.com");
    }
}