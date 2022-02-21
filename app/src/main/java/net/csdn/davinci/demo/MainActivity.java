package net.csdn.davinci.demo;

import static net.csdn.davinci.DaVinci.KEY_SELECTED_PHOTOS;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import net.csdn.davinci.DaVinci;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] storage = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

        };
        ActivityCompat.requestPermissions(this, storage, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000) {
            DaVinci.create()
                    .showGif(true)
                    .showCamera(true)
                    .column(4)
                    .select(this, 10000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000 && resultCode == RESULT_OK) {
            List<String> photos = data.getStringArrayListExtra(KEY_SELECTED_PHOTOS);
            Log.e("DaVinci", "========" + photos.toString());
        }
    }
}