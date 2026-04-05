package com.example.audiovideoplayer;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


public class MainActivity extends AppCompatActivity {
    EditText rawVideoUrl;
    Button playVideoBtn;
    Button playAudioBtn;
    Uri audioUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rawVideoUrl = findViewById(R.id.videoURL);
        playVideoBtn = findViewById(R.id.playVideoBtn);
        playAudioBtn = findViewById(R.id.playAudioBtn);

        playVideoBtn.setOnClickListener(v -> {
            String video_url = rawVideoUrl.getText().toString().trim();

            if (video_url.isEmpty()){
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
            intent.putExtra("video_url", video_url);
            startActivity(intent);
        });


        /* Audio Section */
        playAudioBtn.setOnClickListener(v -> {
            Intent audio_intent = new Intent(this, AudioActivity.class);
            startActivity(audio_intent);
        });




    }
}