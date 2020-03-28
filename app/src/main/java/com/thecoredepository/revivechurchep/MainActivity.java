package com.thecoredepository.revivechurchep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    private WebView webView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        context = this;

        final ConstraintLayout splashView = findViewById(R.id.splashView);
        splashView.setVisibility(View.VISIBLE);
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText("Version: " + BuildConfig.VERSION_NAME);


        webView = findViewById(R.id.webview);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("URL", url);
                if (url.equals("https://m.facebook.com/EPReviveChurch/?ref=br_rs")) //check if that's a url you want to load internally
                {
                    Log.d("URL", "URL = Facebook request");
                    Intent fbIntent = getOpenFacebookIntent(context);
                    startActivity(fbIntent);
                    return false;
                }
                else
                {
                    view.loadUrl(url);
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                splashView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                splashView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                TextView txtTextSplash = findViewById(R.id.txtTextSplash);
                txtTextSplash.setText("No Internet... Please try again.");
            }
        });

        webView.loadUrl("https://www.revivechurchep.com/");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public static Intent getOpenFacebookIntent(Context context)
    {
        try
        {
            Log.d("URL", "Trying you open app");
            context.getPackageManager().getPackageInfo("com.facebook.katana",0);
            Log.d("URL", "Opened in Facebook App");
            return new Intent(Intent.ACTION_VIEW,Uri.parse("fb://page/1093885720794176"));
        }catch (Exception e)
        {
            Log.d("URL", "App loading failed. Opening URL.");
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/EPReviveChurch"));
        }
    }
}
