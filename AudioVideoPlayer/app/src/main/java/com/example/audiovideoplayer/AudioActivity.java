package com.example.audiovideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageButton;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ImageButton prevBtn;
    Button playBtn, pauseBtn, selectBtn, restartBtn;
    Uri audioUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prevBtn = findViewById(R.id.prevBtn);
        selectBtn = findViewById(R.id.selectBtn);
        playBtn = findViewById(R.id.playBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        restartBtn = findViewById(R.id.restartBtn);

        mediaPlayer = new MediaPlayer();

        prevBtn.setOnClickListener(v-> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        /* Audio Logic */
        ActivityResultLauncher<Intent> audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        audioUri = result.getData().getData();

                        // persist permission
                        getContentResolver().takePersistableUriPermission(
                                audioUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    }
                }
        );

        selectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("audio/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            audioPickerLauncher.launch(intent);
        });

        // Play audio
        playBtn.setOnClickListener(v -> {
            if (audioUri != null) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(this, audioUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(this, "Audio Playing", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(this, "looks like thiers an error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        // Pause audio
        pauseBtn.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                Toast.makeText(this, "Audio Paused", Toast.LENGTH_LONG).show();
                mediaPlayer.pause();
            }
        });

        restartBtn.setOnClickListener(v->{
            if (mediaPlayer != null){
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                Toast.makeText(this, "Audio Restarted", Toast.LENGTH_SHORT).show();
            }
        });






    }
}