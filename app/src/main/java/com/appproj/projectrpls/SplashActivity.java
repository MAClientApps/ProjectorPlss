package com.appproj.projectrpls;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appproj.projectrpls.activities.MainActivity;
import com.appproj.projectrpls.activities.ProjectorPlsActivity;
import com.appproj.projectrpls.utility.ProjectorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private long SPLASH_TIME = 4000L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getWindow ().setFlags (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setContentView (R.layout.splash_activity);
        LoadApiCall ();
    }


    private void gotoMainBoard() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_TIME);
    }

    public void doCheckConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.AppDialogTheme);
        builder.setTitle(R.string.no_internet_conn);
        builder.setMessage(R.string.check_your_con_msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_retry, (dialog, which) -> {
            dialog.dismiss();
            retryConnection();
        });
        builder.show();
    }

    private void retryConnection() {
        new Handler(Looper.getMainLooper()).postDelayed(this::LoadApiCall, 700);
    }

    private void LoadApiCall() {
        if (!ProjectorUtils.isConnectionAvailable(this)) {
            doCheckConnectionDialog();
        } else {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://" + ProjectorUtils.END_POINT_VALUE + "/?package=" + getPackageName())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        gotoMainBoard();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String myResponse = response.body().string();
                        try {
                            runOnUiThread(() -> {
                                try {
                                    JSONObject jsonData = new JSONObject(myResponse);
                                    if (jsonData.has("cf")) {

                                        try {
                                            if (jsonData.has("second")) {
                                                SPLASH_TIME = jsonData.getLong("second");
                                                SPLASH_TIME = SPLASH_TIME * 1000L;
                                            } else {
                                                SPLASH_TIME = 4000L;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String fileResult = null;
                                        try {
                                            fileResult = jsonData.getString("cf");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (fileResult != null && !fileResult.equals("")) {
                                            ProjectorUtils.UNITY_IS_VISIBLE = false;
                                            if (fileResult.startsWith("http")) {
                                                ProjectorUtils.setProEndPoint(SplashActivity.this, fileResult);
                                            } else {
                                                ProjectorUtils.setProEndPoint(SplashActivity.this, "https://" + fileResult);
                                            }

                                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                startActivity(new Intent(SplashActivity.this, ProjectorPlsActivity.class));
                                                finish();
                                            }, SPLASH_TIME);
                                        } else {
                                            gotoMainBoard();
                                        }
                                    } else {
                                        gotoMainBoard();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    gotoMainBoard();
                                }
                            });
                        } catch (Exception e) {
                            gotoMainBoard();
                        }
                    }
                });
            } catch (Exception e) {
                gotoMainBoard();
            }
        }
    }

}