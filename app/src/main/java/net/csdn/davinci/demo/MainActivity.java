package net.csdn.davinci.demo;

import static net.csdn.davinci.DaVinci.KEY_SELECTED_PHOTOS;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import net.csdn.davinci.DaVinci;

import java.util.ArrayList;
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

        findViewById(R.id.btn_sel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaVinci.select()
                        .maxSelectable(1)
                        .showGif(true)
                        .showCamera(true)
                        .column(4)
                        .start(MainActivity.this, 10000);
            }
        });

        findViewById(R.id.btn_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> paths = new ArrayList<>();
                paths.add("https://csdn-app.csdn.net/1111_activity_2.png");
                paths.add("https://userblink.csdnimg.cn/20220311/programmerjob/pic/c2a20d0577e12cdd1706fc70fa260f4f-0.jpg");
                paths.add("https://img-blog.csdnimg.cn/img_convert/a69b7a16d351387a4c79125277088aa0.gif");
                paths.add("https://img-blog.csdnimg.cn/img_convert/c4ea67f19b79bec7678cafdd9c81556d.png");
                paths.add("https://csdn-app.csdn.net/2dcode/magaz.png");
                paths.add("https://csdn-app.csdn.net/6.5英寸 4@3x.png");

                DaVinci.preview()
                        .previewPhotos(paths)
                        .saveFolderName("CSDN")
                        .needQrScan(true)
                        .qrScanCallback(new DaVinci.QrScanCallback() {
                            @Override
                            public void onResult(String result) {
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                            }
                        })
                        .start(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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