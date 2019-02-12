package com.agarwalaman.newsfeed.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agarwalaman.newsfeed.R;
import com.agarwalaman.newsfeed.util.Constants;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_webview);
            Toolbar toolbar = findViewById(R.id.toolbar);
            mWebView = findViewById(R.id.webView);
            progressBar = findViewById(R.id.progressbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.done));
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    super.shouldOverrideUrlLoading(view, url);
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progressBar.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });
            mWebView.getSettings().setJavaScriptEnabled(true);
            Intent intent = getIntent();
            if (intent != null) {
                String url = intent.getStringExtra(Constants.URL);
                mWebView.loadUrl(url);
            } else {
                Toast.makeText(this, getString(R.string.news_url_not_found), Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Log.e("WebView Exception:", e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
