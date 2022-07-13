package net.csdn.davinci.core.photo;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoCaptureManager {

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;

    private Uri mCurrentPhotoUri;
    private Application mContext;

    private PhotoCaptureManager() {
    }

    private static class InstanceHolder {
        private static final PhotoCaptureManager instance = new PhotoCaptureManager();
    }

    public static PhotoCaptureManager getInstance(Application context) {
        if (InstanceHolder.instance.mContext == null && context != null) {
            InstanceHolder.instance.mContext = context;
        }
        return InstanceHolder.instance;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                Log.e("TAG", "Throwing Errors....");
                throw new IOException();
            }
        }
        File image = new File(storageDir, imageFileName);
        return image;
    }

    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return mContext.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mCurrentPhotoUri = createImageUri();
            } else {
                File file = createImageFile();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String authority = mContext.getApplicationInfo().packageName + ".provider";
                    mCurrentPhotoUri = FileProvider.getUriForFile(this.mContext.getApplicationContext(), authority, file);
                } else {
                    mCurrentPhotoUri = Uri.fromFile(file);
                }

            }
            if (mCurrentPhotoUri != null) {
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
            }
        }
        return takePictureIntent;
    }


    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        if (null == mCurrentPhotoUri) {
            return;
        }
        mediaScanIntent.setData(mCurrentPhotoUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    public Uri getCurrentPhotoUri() {
        return mCurrentPhotoUri;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mCurrentPhotoUri != null) {
            savedInstanceState.putParcelable(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoUri);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoUri = savedInstanceState.getParcelable(CAPTURED_PHOTO_PATH_KEY);
        }
    }

}
