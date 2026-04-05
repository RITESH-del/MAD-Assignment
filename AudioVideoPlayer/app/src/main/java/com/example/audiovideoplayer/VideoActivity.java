package com.example.audiovideoplayer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.net.Uri;
import android.widget.VideoView;
import android.widget.MediaController;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;

// https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.
//mp4
// https://www.learningcontainer.com/download/sample-video-download-mp4/?wpdmdl=8745&refresh=69d1d87b0c3ce1775360123
// https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/Sample-MP4-Video-File-for-Testing.mp4
public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    Uri uri;
    Button restartBtn;
    ImageButton prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        videoView = findViewById(R.id.videoView);
        restartBtn = findViewById(R.id.restartBtn);
        prevBtn = findViewById(R.id.prevBtn);

//        String videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4";

        Intent get_intent = getIntent();
        String video_url = get_intent.getStringExtra("video_url");
        uri = Uri.parse(video_url);


        videoPlayer();

        restartBtn.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });

        prevBtn.setOnClickListener(v -> {
            Intent intent = new Intent(VideoActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

     private void videoPlayer(){
        mediaController = new MediaController(this);

        // Attach listener for when video is ready
        videoView.setOnPreparedListener(mp -> {
            // Adjust anchor once video size is known
            mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
                mediaController.setAnchorView(videoView); //attach playback controls to videoView
            });
        });

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        videoView.start();
    }
}