package net.csdn.davinci.core.entity;

import android.net.Uri;

public class Photo {
    public String id;
    public String imgPath;
    public Uri uri;

    public Photo() {
    }

    public Photo(Uri uri) {
        this.uri = uri;
    }
}
