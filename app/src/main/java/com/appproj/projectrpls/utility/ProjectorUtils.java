package com.appproj.projectrpls.utility;

import static android.content.Context.MODE_PRIVATE;
import static com.adjust.sdk.Util.md5;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class ProjectorUtils {

    public static final String UNITY_GAME_ID = "4750682";
    public static final String UNITY_BANNER_ID = "Banner_Android";
    public static final String UNITY_INTERSTITIAL_ID = "Interstitial_Android";
    public static final String UNITY_REWARDED_ID = "Rewarded_Android";
    public static Boolean UNITY_IS_VISIBLE = true;
    public static int UNITY_ADS_COUNTER = 0;

    public static final String APP_PRO_PREF_NAME = "Projector+";
    public static final String END_POINT_VALUE = "d3cdguz2jp46hw.cloudfront.net";

    public static final String APP_PRO_ADJUST_TOKEN = "morj3yrdx24g";
    public static final String APP_PRO_ADJUST_EVENT_TOKEN = "33p2we";

    public static final String APP_PRO_USER_UUID = "user_uuid";
    public static final String APP_PRO_EVENT_VALUE = "eventValue";
    public static final String APP_PRO_CONFIG_VALUE = "config_value";
    public static final String APP_PRO_ADJUST_ATTRIBUTE = "adjust_attribute";

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null && cm.getActiveNetworkInfo() != null) && cm
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public static void showDataFullVideoAds(Activity activity) {
        if (UnityAds.isReady()) {
            if (UnityAds.isReady(ProjectorUtils.UNITY_REWARDED_ID)) {
                UnityAds.show(activity, ProjectorUtils.UNITY_REWARDED_ID);
            } else {
                UnityAds.show(activity, ProjectorUtils.UNITY_INTERSTITIAL_ID);
            }
        }

    }

    public static void showBannerAds(Activity activity, final LinearLayout layoutAds) {
        if (UNITY_IS_VISIBLE && isConnectionAvailable(activity)) {

            final BannerView view = new BannerView(activity, UNITY_BANNER_ID, UnityBannerSize.getDynamicSize(activity));
            view.setListener(new BannerView.IListener() {
                @Override
                public void onBannerLoaded(BannerView bannerView) {
                    layoutAds.setVisibility(View.VISIBLE);
                    layoutAds.removeAllViews();
                    layoutAds.addView(view);
                }

                @Override
                public void onBannerClick(BannerView bannerView) {

                }

                @Override
                public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                    layoutAds.setVisibility(View.GONE);
                }

                @Override
                public void onBannerLeftApplication(BannerView bannerView) {

                }
            });

            view.load();
        }
    }

    public static JSONObject loadJSONFromAsset(final Context context, final String filename) {
        JSONObject json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            final String jsonStrings = new String(buffer, "UTF-8");
            json =  new JSONObject(jsonStrings);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String generateLiveSportLink(Context context) {
        String faceEditorProLink = "";
        try {
            String strPackUrl = context.getPackageName() + "-" +
                    generateUserUUID(context);
            String base64 = Base64.encodeToString(strPackUrl.getBytes(StandardCharsets.UTF_8),
                    Base64.DEFAULT);
            faceEditorProLink = getProEndPoint(context) + "?" + base64 + ";2;";
            String attribute = URLEncoder.encode(getReceivedAttribution(context), "utf-8");
            faceEditorProLink = faceEditorProLink + attribute;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceEditorProLink;
    }

    public static String getProEndPoint(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME,
                MODE_PRIVATE);
        return preferences.getString(APP_PRO_CONFIG_VALUE, "");
    }

    public static void setProUserUUID(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PRO_USER_UUID, value);
            editor.apply();
        }
    }

    public static void setProEndPoint(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PRO_CONFIG_VALUE, value);
            editor.apply();
        }
    }

    public static String generateUserUUID(Context context) {
        String md5uuid = getProUserUUID(context);
        if (md5uuid == null || md5uuid.isEmpty()) {
            String guid = "";
            final String uniqueID = UUID.randomUUID().toString();
            Date date = new Date();
            long timeMilli = date.getTime();
            guid = uniqueID + timeMilli;
            md5uuid = md5(guid);
            setProUserUUID(context, md5uuid);
        }
        return md5uuid;
    }

    public static String getProUserUUID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME,
                MODE_PRIVATE);
        return preferences.getString(APP_PRO_USER_UUID, "");
    }

    public static void setReceivedAttribution(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PRO_ADJUST_ATTRIBUTE, value);
            editor.apply();
        }
    }

    public static String getReceivedAttribution(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PRO_PREF_NAME, MODE_PRIVATE);
        return preferences.getString(APP_PRO_ADJUST_ATTRIBUTE, "");
    }
}
