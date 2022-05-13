package com.appproj.projectrpls.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class VideosProjectorData {

    String Title, Thumbnail, Content;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public static ArrayList<VideosProjectorData> getVideosProjectorList(JSONArray jsonArray) {
        ArrayList<VideosProjectorData> videosProjectorDataArrayList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    videosProjectorDataArrayList.add(getContentFromJson(jsonObject));
                }
            }
            return videosProjectorDataArrayList;
        } catch (Exception e) {
            return videosProjectorDataArrayList;
        }
    }

    private static VideosProjectorData getContentFromJson(JSONObject jsonObject) {
        VideosProjectorData videosProjectorData = new VideosProjectorData();
        try {
            if (jsonObject.has("Title") && !jsonObject.isNull("Title"))
                videosProjectorData.setTitle(jsonObject.getString("Title"));

            if (jsonObject.has("Thumbnail") && !jsonObject.isNull("Thumbnail"))
                videosProjectorData.setThumbnail(jsonObject.getString("Thumbnail"));

            if (jsonObject.has("Content") && !jsonObject.isNull("Content"))
                videosProjectorData.setContent(jsonObject.getString("Content"));

            return videosProjectorData;
        } catch (JSONException e) {
            return videosProjectorData;
        }
    }


}
