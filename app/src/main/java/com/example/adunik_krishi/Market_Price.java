package com.example.adunik_krishi;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


public class Market_Price extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market__price);

        webView = findViewById(R.id.webViewPrice);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl("http://www.dam.gov.bd/damweb/PublicPortal/MarketDisplayFullScreenBangla.php");
        //  webView.setWebViewClient(new WebViewClient());

        String userAgent = webView.getSettings().getUserAgentString();

        try {
            String androidString = webView.getSettings().getUserAgentString().
                    substring(userAgent.indexOf("("), userAgent.indexOf(")") + 1);

            userAgent = webView.getSettings().getUserAgentString().replace(androidString, "X11; Linux x86_64");

        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.getSettings().setUserAgentString(userAgent);
        webView.reload();


    }
}