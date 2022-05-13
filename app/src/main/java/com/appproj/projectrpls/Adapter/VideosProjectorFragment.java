package com.appproj.projectrpls.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appproj.projectrpls.R;
import com.appproj.projectrpls.utility.ProjectorUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideosProjectorFragment extends Fragment {

    private View view;
    private Context context;
    LinearLayout layoutError;
    private RecyclerView rvVideosShow;

    @SuppressLint("StaticFieldLeak")
    public static VideosProjectorAdapter videosProjectorAdapter;

    GridLayoutManager gridLayoutManager;
    TextView txt_error_msg;
    private int type = 0;

    private final ArrayList<VideosProjectorData> videosProjectorDataArrayList = new ArrayList<>();
    private String typeValue = "WowEnvironment.json";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.projector_fragment_videos, container, false);
        context = getActivity();
        readExtra();
        initGlobalList();
        return view;
    }

    public void readExtra() {
        if (getArguments() != null) {
            type = getArguments().getInt("Type");
        }


        if (type == 0) {
            typeValue = "WildAnimal.json";
        } else if (type == 1) {
            typeValue = "TouristPlaces.json";
        } else if (type == 2) {
            typeValue = "HeavyFails.json";
        } else if (type == 3) {
            typeValue = "WowEnvironment.json";
        } else if (type == 4) {
            typeValue = "HowItWorks.json";
        } else if (type == 5) {
            typeValue = "ActionSports.json";
        } else if (type == 6) {
            typeValue = "UnderWaterLife.json";
        } else if (type == 7) {
            typeValue = "FootPop.json";
        }
    }

    public static VideosProjectorFragment getNewInstance(final int usageType) {
        VideosProjectorFragment videosShowFragment = new VideosProjectorFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt("Type", usageType);
        videosShowFragment.setArguments(mBundle);
        return videosShowFragment;
    }


    private void initGlobalList() {
        layoutError = view.findViewById(R.id.layoutErrors);
        rvVideosShow = view.findViewById(R.id.rvVideosShow);

        gridLayoutManager = new GridLayoutManager(context, 2);

        rvVideosShow.setLayoutManager(gridLayoutManager);
        txt_error_msg = view.findViewById(R.id.txt_error_msg);

        if (ProjectorUtils.isConnectionAvailable(context)) {
            new GetAmzVideosX().execute();
        } else {
            txt_error_msg.setText(R.string.no_internet_conn);
            layoutError.setVisibility(View.VISIBLE);
            rvVideosShow.setVisibility(View.GONE);
        }

    }


    private void loadVideosProjector() {
        videosProjectorAdapter = new VideosProjectorAdapter(videosProjectorDataArrayList, context);
        rvVideosShow.setAdapter(videosProjectorAdapter);
        videosProjectorAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    public class GetAmzVideosX extends AsyncTask<String, Integer, JSONObject> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                return ProjectorUtils.loadJSONFromAsset(context, typeValue);
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                try {
                    if (jsonObject != null && jsonObject.has("Content") && !jsonObject.isNull("Content")) {
                        final JSONArray jsonArrayAction = jsonObject.getJSONArray("Content");
                        videosProjectorDataArrayList.addAll(VideosProjectorData.getVideosProjectorList(jsonArrayAction));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadVideosProjector();
        }

    }

}