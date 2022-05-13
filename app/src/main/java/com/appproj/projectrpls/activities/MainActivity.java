package com.appproj.projectrpls.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appproj.projectrpls.Adapter.VideosProjectorFragment;
import com.appproj.projectrpls.BuildConfig;
import com.appproj.projectrpls.R;
import com.appproj.projectrpls.utility.ProjectorUtils;
import com.google.android.material.navigation.NavigationView;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IUnityAdsListener {

    LinearLayout Unity_ad;
    @SuppressLint("StaticFieldLeak")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.main_activity);
        Toolbar toolbar_proj = (Toolbar) findViewById (R.id.toolbar_proj);
        setSupportActionBar (toolbar_proj);
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout1);
        Unity_ad=findViewById(R.id.Unity_ad);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar_proj, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ();


        NavigationView navigationView = (NavigationView) findViewById (R.id.navbarView);
        navigationView.setNavigationItemSelectedListener (this);

        displaySelectedScreen (R.id.wow_environment);
        getSupportActionBar ().setTitle (getString (R.string.app_name));

        if (ProjectorUtils.UNITY_IS_VISIBLE && ProjectorUtils.isConnectionAvailable(MainActivity.this)) {
            UnityAds.addListener(this);
            if (!UnityAds.isInitialized()) {
                UnityAds.initialize(MainActivity.this, ProjectorUtils.UNITY_GAME_ID, BuildConfig.DEBUG);
            } else {
                showBanner();
            }
        }



    }

    private void showBanner() {
        if (UnityAds.isReady(ProjectorUtils.UNITY_BANNER_ID)) {
            ProjectorUtils.showBannerAds(MainActivity.this, Unity_ad);
        } else {
            Unity_ad.setVisibility(View.GONE);
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout1);
        if (drawer.isDrawerOpen (GravityCompat.START)) {
            drawer.closeDrawer (GravityCompat.START);
        } else {
            super.onBackPressed ();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager ();

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.wild_animals:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (0))
                        .commit ();
                getSupportActionBar ().setTitle ("Wild Animal");
                break;
            case R.id.tourist_place:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (1))
                        .commit ();
                getSupportActionBar ().setTitle ("Tourist Places");
                break;
            case R.id.heavy_fails:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (2))
                        .commit ();
                getSupportActionBar ().setTitle ("Heavy Fails");
                break;
            case R.id.wow_environment:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (3))
                        .commit ();
                getSupportActionBar ().setTitle ("Wow Environment");
                break;
            case R.id.how_it_works:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (4))
                        .commit ();
                getSupportActionBar ().setTitle ("How it Works");
                break;
            case R.id.action_sports:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (5))
                        .commit ();
                getSupportActionBar ().setTitle ("Action Sports");
                break;
            case R.id.under_water_life:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (6))
                        .commit ();
                getSupportActionBar ().setTitle ("Underwater Life");
                break;
            case R.id.foot_pop:
                fragmentManager.beginTransaction ().replace (R.id.frame_content_proj, VideosProjectorFragment.getNewInstance (7))
                        .commit ();
                getSupportActionBar ().setTitle ("Foot Pop");
                break;

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager ().beginTransaction ();
            ft.replace (R.id.frame_content_proj, fragment);
            ft.commit ();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout1);
        drawer.closeDrawer (GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen (item.getItemId ());

        return true;
    }


    @Override
    public void onUnityAdsReady(String s) {
        showBanner();
    }

    @Override
    public void onUnityAdsStart(String s) {

    }

    @Override
    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

    }
}


