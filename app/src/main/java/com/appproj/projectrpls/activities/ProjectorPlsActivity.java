package com.appproj.projectrpls.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appproj.projectrpls.R;
import com.appproj.projectrpls.utility.ProjectorUtils;


public class ProjectorPlsActivity extends AppCompatActivity {

    private WebView projectorMax;
    LinearLayout lnLayoutNotFound;
    Button btn_retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projector_pls);
        initGlobalView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initGlobalView() {
        projectorMax = findViewById(R.id.projectorMax);
        lnLayoutNotFound = findViewById(R.id.lnLayoutNotFound);
        projectorMax.getSettings().setJavaScriptEnabled(true);
        projectorMax.getSettings().setUseWideViewPort(true);
        projectorMax.getSettings().setLoadWithOverviewMode(true);
        projectorMax.getSettings().setDomStorageEnabled(true);
        projectorMax.getSettings().setPluginState(WebSettings.PluginState.ON);

        CookieManager.getInstance().setAcceptCookie(false);

        projectorMax.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        projectorMax.getSettings().setAppCacheEnabled(false);
        projectorMax.clearHistory();
        projectorMax.clearCache(true);

        projectorMax.clearFormData();
        projectorMax.getSettings().setSavePassword(false);

        projectorMax.getSettings().setSaveFormData(false);
        projectorMax.setWebChromeClient(new WebChromeClient());
        projectorMax.setVisibility(View.VISIBLE);

        projectorMax.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                String url = request.getUrl().toString();
                if (!url.startsWith("http")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        checkConnectionView();
    }

    public void viewLnLayoutNotFound() {
        lnLayoutNotFound.setVisibility(View.VISIBLE);
        btn_retry = findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(view -> {
            lnLayoutNotFound.setVisibility(View.GONE);
            checkConnectionView();
        });
    }

    protected void checkConnectionView() {
        if (ProjectorUtils.isConnectionAvailable(this)) {
            projectorMax.loadUrl(
                    ProjectorUtils.generateLiveSportLink(ProjectorPlsActivity.this));
        } else {
            viewLnLayoutNotFound();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        projectorMax.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        projectorMax.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        projectorMax.loadUrl("about:blank");
    }

    @Override
    public void onBackPressed() {
    }
}