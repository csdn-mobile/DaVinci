package net.csdn.davinci.utils;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_EXTERNAL_READ = 2;
    public static final int REQUEST_EXTERNAL_WRITE = 3;

    public static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] PERMISSIONS_EXTERNAL_WRITE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static final String[] PERMISSIONS_EXTERNAL_READ = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static boolean checkReadStoragePermission(Activity activity) {
        int readStoragePermissionState = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        boolean readStoragePermissionGranted = readStoragePermissionState == PackageManager.PERMISSION_GRANTED;
        if (!readStoragePermissionGranted) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_READ,
                    REQUEST_EXTERNAL_READ);
        }
        return readStoragePermissionGranted;
    }

    public static boolean checkWriteStoragePermission(Activity activity) {
        int writeStoragePermissionState = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        boolean writeStoragePermissionGranted = writeStoragePermissionState == PackageManager.PERMISSION_GRANTED;
        if (!writeStoragePermissionGranted) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_WRITE, REQUEST_EXTERNAL_WRITE);
        }
        return writeStoragePermissionGranted;
    }

    public static boolean checkCameraPermission(Activity activity) {
        int cameraPermissionState = ContextCompat.checkSelfPermission(activity, CAMERA);
        boolean cameraPermissionGranted = cameraPermissionState == PackageManager.PERMISSION_GRANTED;
        if (!cameraPermissionGranted) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CAMERA);
        }
        return cameraPermissionGranted;
    }

}
