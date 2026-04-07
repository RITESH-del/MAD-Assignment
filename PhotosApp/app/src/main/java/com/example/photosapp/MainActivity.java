package com.example.photosapp;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity {
    private Uri photoUri;
    public static Uri selectedFolderUri;

    private ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> filePicker;
    ImageButton openCameraBtn, selectFolderBtn;
    List<Uri> imgUris;
    ImageAdapter adapter;



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

        openCameraBtn = findViewById(R.id.openCameraBtn);
        selectFolderBtn = findViewById(R.id.selectFolderBtn);
        imgUris = new ArrayList<>();


/* RecyclerView */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        int columnWidth = 300;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int spanCount = Math.max(3, metrics.widthPixels / columnWidth);

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageAdapter(imgUris);
        recyclerView.setAdapter(adapter);

/* Loading folderUri after activity restart */
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        String uriStr = prefs.getString("folder_uri", null);

        if (uriStr != null) {
            selectedFolderUri = Uri.parse(uriStr);
            loadImagesFromFolder(); // reload images automatically
        }

        // registering for the camera activity result
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK){
                imgUris.add(photoUri);
                adapter.notifyItemInserted(imgUris.size() - 1);
            }
        });

        // for directory Uri
        filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK){
                Uri treeUri = result.getData().getData();

                // Persist permission
                getContentResolver().takePersistableUriPermission(
                        treeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );


                // Save URI string,
                getSharedPreferences("app", MODE_PRIVATE)
                        .edit()
                        .putString("folder_uri", treeUri.toString())
                        .apply();

                selectedFolderUri = treeUri;

                // load all previous images from the folder
                loadImagesFromFolder();

                Toast.makeText(this, "Folder Selected", Toast.LENGTH_SHORT).show();
            }
        });


        selectFolderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            filePicker.launch(intent);
        });



        openCameraBtn.setOnClickListener(v -> {
            // For checking Runtime permission
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
                return;
            }

            try {
                photoUri = createImageUri();

                if (photoUri == null) return;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Toast.makeText(this, "Opening Camera", Toast.LENGTH_LONG).show();


                if (intent.resolveActivity(getPackageManager()) != null) {
                    cameraLauncher.launch(intent);
                }
            } catch (Exception e){
                Toast.makeText(this, "Failed to Open Camera", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

    }

    private void loadImagesFromFolder(){
        if (selectedFolderUri == null) {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // create usable object from the Uri
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedFolderUri);

        if (pickedDir == null || !pickedDir.isDirectory()) {
            Toast.makeText(this, "Invalid folder", Toast.LENGTH_SHORT).show();
            return;
        }

        imgUris.clear();

        for (DocumentFile file: pickedDir.listFiles()){
            // Check if file is an image
            if (file.isFile() && file.getType() != null && file.getType().startsWith("image/")) {
                imgUris.add(file.getUri());
            }
        }

        adapter.notifyDataSetChanged();


    }


    private Uri createImageUri() {
        if (selectedFolderUri == null) {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            return null;
        }


        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedFolderUri);

        if (pickedDir == null) {
            Toast.makeText(this, "Invalid folder", Toast.LENGTH_SHORT).show();
            return null;
        }

        DocumentFile newFile = pickedDir.createFile(
                "image/jpeg",
                "IMG_" + System.currentTimeMillis()
        );

        if (newFile == null) {
            Toast.makeText(this, "File creation failed", Toast.LENGTH_SHORT).show();
            return null;
        }

        return newFile.getUri();
    }
}