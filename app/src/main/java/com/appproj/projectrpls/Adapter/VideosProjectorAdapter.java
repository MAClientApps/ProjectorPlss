package com.appproj.projectrpls.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appproj.projectrpls.R;
import com.appproj.projectrpls.activities.VideoPlayActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;

import java.util.ArrayList;

public class VideosProjectorAdapter extends RecyclerView.Adapter<VideosProjectorAdapter.ViewHolder> {

    ArrayList<VideosProjectorData> videosShowDataArrayList;
    private final Context context;

    public VideosProjectorAdapter(final ArrayList<VideosProjectorData> videosShowDataArrayList, final Context context) {
        super();
        this.videosShowDataArrayList = videosShowDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.projector_item_videos, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        try {
            final String imageUrl = videosShowDataArrayList.get(position).getThumbnail();
            Glide.with(context)
                    .load(new GlideUrl(imageUrl))
                    .into(viewHolder.imgVideosThumbnails);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.txtVideosName.setText(videosShowDataArrayList.get(position).getTitle());


        viewHolder.layoutAmzVideosData.setOnClickListener(view -> {
            Intent intent = new Intent(context, VideoPlayActivity.class);
            intent.putExtra("vimeo", videosShowDataArrayList.get(position).getContent());
            intent.putExtra("title", videosShowDataArrayList.get(position).getTitle());
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return videosShowDataArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgVideosThumbnails, imgVideosPlay;
        public TextView txtVideosName;
        public RelativeLayout layoutAmzVideosData;

        public ViewHolder(View itemView) {
            super(itemView);
            imgVideosThumbnails = itemView.findViewById(R.id.imgVideosThumbnails);
            layoutAmzVideosData = itemView.findViewById(R.id.layoutAmzVideosData);
            imgVideosPlay = itemView.findViewById(R.id.imgVideosPlayers);
            txtVideosName = itemView.findViewById(R.id.txtVideosName);
        }
    }


}
