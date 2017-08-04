package com.example.diemsct;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class RedhatVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    public static final String DEVELOPER_KEY = "AIzaSyCZ7BpYSLMkexH6BbBIH1jOFLg3KnokAR8";
    private static final String VIDEO_ID = "JWy0m8jbaw4";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    YouTubePlayerFragment myYoutubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.redhat_video);

            Intent intent= getIntent();
            myYoutubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
            myYoutubePlayerFragment.initialize(DEVELOPER_KEY, this);
        }

        @Override
        public void onInitializationSuccess (YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored){

            if (!wasRestored) {
                youTubePlayer.cueVideo(VIDEO_ID);
            }

        }

        @Override
        public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason)
        {
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();

            } else {
                String errorMessage = String.format("There was an error initializing the youtube player", errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==RECOVERY_DIALOG_REQUEST){
            //retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY,this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider(){
        return (YouTubePlayerView)findViewById(R.id.youtubeplayerfragment);
    }
}

