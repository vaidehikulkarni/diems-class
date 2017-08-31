package org.diems.diemsapp;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;


public class RedhatAcademy extends Fragment {

    ImageView imageView;
//    YouTubeThumbnailView youTubeThumbnailView;

//    public static final String DEVELOPER_KEY = "AIzaSyCZ7BpYSLMkexH6BbBIH1jOFLg3KnokAR8";
//    private static final String VIDEO_ID = "JWy0m8jbaw4";

    public RedhatAcademy() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redhat_academy, container, false);

//        youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtubeThumbnailView);
        imageView = (ImageView) view.findViewById(R.id.imageview2);

//        youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                youTubeThumbnailLoader.setVideo(VIDEO_ID);
//                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                    @Override
//                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                        youTubeThumbnailLoader.release();
//                    }
//
//                    @Override
//                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                        Toast.makeText(getActivity(), "Thumbnail Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                Toast.makeText(getActivity(), "Initialisation Error", Toast.LENGTH_SHORT).show();
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RedhatVideo.class);
                startActivity(intent);
            }
        });
        return view;
    }
}




