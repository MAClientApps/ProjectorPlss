package com.appproj.projectrpls.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appproj.projectrpls.R;
import com.appproj.projectrpls.utility.ProjectorUtils;


public class VideoPlayActivity extends AppCompatActivity {

    String contentLink = null;
    WebView videoPlayer;
    LinearLayout Unity_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.video_play_activity);
        Toolbar toolbar_proj = (Toolbar) findViewById (R.id.toolbar_proj);
        setSupportActionBar (toolbar_proj);
        contentLink = getIntent ().getStringExtra ("vimeo");
        videoPlayer = findViewById (R.id.videoPlayers);
        Unity_ad=findViewById(R.id.Unity_ad);
        getSupportActionBar ().setTitle (getIntent ().getStringExtra ("title"));
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setDisplayShowHomeEnabled (true);
        getSupportActionBar ().setHomeAsUpIndicator (R.drawable.ic_back_arrow);

        if (!contentLink.isEmpty ()) {

            CookieManager.getInstance ().setAcceptCookie (true);
            videoPlayer.getSettings ().setJavaScriptEnabled (true);
            videoPlayer.getSettings ().setUseWideViewPort (true);
            videoPlayer.getSettings ().setLoadWithOverviewMode (true);
            videoPlayer.getSettings ().setDomStorageEnabled (true);
            videoPlayer.getSettings ().setPluginState (WebSettings.PluginState.ON);
            videoPlayer.setWebChromeClient (new WebChromeClient ());
            videoPlayer.setVisibility (View.VISIBLE);
            videoPlayer.setWebViewClient (new WebViewClient () {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished (view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading (view, url);
                }
            });


            try {
                videoPlayer.loadUrl (contentLink);
            } catch (
                    Exception e) {
                e.printStackTrace ();
            }
        }

        if (ProjectorUtils.UNITY_IS_VISIBLE && ProjectorUtils.isConnectionAvailable(VideoPlayActivity.this)) {
                ProjectorUtils.showBannerAds(VideoPlayActivity.this,Unity_ad);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId () == android.R.id.home) {
            onBackPressed ();
        }
        return super.onOptionsItemSelected (item);
    }


}


