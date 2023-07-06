package net.csdn.davinci.core.entity;

import android.net.Uri;

public class DavinciPhoto extends DavinciMedia {

    public DavinciPhoto() {
    }

    public DavinciPhoto(Uri uri) {
        this.uri = uri;
    }
}
