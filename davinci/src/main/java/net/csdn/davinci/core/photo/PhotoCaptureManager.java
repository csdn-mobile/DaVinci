package net.csdn.davinci.core.photo;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import net.csdn.davinci.core.entity.DavinciPhoto;
import net.csdn.davinci.utils.FilePathUtils;
import net.csdn.davinci.utils.PhotoUtils;

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
        File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (null == dir) {
            dir = new File(Environment.getExternalStorageDirectory(),
                    File.separator + "DCIM" + File.separator + "Camera" + File.separator);
        }
        if (!dir.isDirectory()) {
            if (!dir.mkdirs()) {
                dir = InstanceHolder.instance.mContext.getExternalFilesDir(null);
                if (null == dir || !dir.exists()) {
                    dir = InstanceHolder.instance.mContext.getFilesDir();
                    if (null == dir || !dir.exists()) {
                        dir = InstanceHolder.instance.mContext.getFilesDir();
                        if (null == dir || !dir.exists()) {
                            String cacheDirPath =
                                    File.separator + "data" + File.separator + "data" + File.separator + InstanceHolder.instance.mContext.getPackageName() + File.separator + "cache" + File.separator;
                            dir = new File(cacheDirPath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                        }
                    }
                }
            }
        }
        File image = new File(dir, imageFileName);
        return image;
    }

    private Uri createImageUri() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,
                String.valueOf(System.currentTimeMillis()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures");
        }
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        return InstanceHolder.instance.mContext.getContentResolver().insert(MediaStore.Images.Media.getContentUri("external"),
                contentValues);
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mCurrentPhotoUri = createImageUri();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                return takePictureIntent;
            }
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
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

    public DavinciPhoto getCurrentPhoto(Activity activity) {
        DavinciPhoto photo = new DavinciPhoto(mCurrentPhotoUri);
        photo.path = FilePathUtils.getFileAbsolutePath(activity, mCurrentPhotoUri);
        Point originSize = PhotoUtils.getBitmapSize(mCurrentPhotoUri, activity);
        photo.width = originSize.x;
        photo.height = originSize.y;
        return photo;
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
