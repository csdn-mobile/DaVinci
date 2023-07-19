package net.csdn.davinci.core.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class DavinciPhoto extends DavinciMedia implements Parcelable {

    public boolean isNetworkPhoto;

    private static final String MEDIA_CONTENT = "content://media/external/images/media/";
    private static final String FILE_CONTENT = "content://media/external/file/";

    public DavinciPhoto() {
    }

    public DavinciPhoto(Uri uri) {
        this.uri = uri;
    }

    public DavinciPhoto(String url) {
        this.path = url;
        this.isNetworkPhoto = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DavinciPhoto)) return false;
        DavinciPhoto that = (DavinciPhoto) o;
        if (uri != null && !TextUtils.isEmpty(uri.toString())) {
            String uriStr = uri.toString();
            String thatUriStr = that.uri.toString();
            if (uriStr.startsWith(MEDIA_CONTENT)) {
                uriStr = uriStr.replace(MEDIA_CONTENT, FILE_CONTENT);
            }
            if (thatUriStr.startsWith(MEDIA_CONTENT)) {
                thatUriStr = thatUriStr.replace(MEDIA_CONTENT, FILE_CONTENT);
            }
            return uriStr.equals(thatUriStr);
        } else if (path != null && !TextUtils.isEmpty(path)) {
            return path.equals(that.path);
        }
        return false;
    }

    /**
     * Parcelable相关
     */
    protected DavinciPhoto(Parcel in) {
        this.path = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.isNetworkPhoto = in.readInt() == 1;
        this.width = in.readInt();
        this.height = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeParcelable(uri, flags);
        dest.writeInt(isNetworkPhoto ? 1 : 0);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    public static final Parcelable.Creator<DavinciPhoto> CREATOR = new Parcelable.Creator<DavinciPhoto>() {
        @Override
        public DavinciPhoto createFromParcel(Parcel in) {
            return new DavinciPhoto(in);
        }

        @Override
        public DavinciPhoto[] newArray(int size) {
            return new DavinciPhoto[size];
        }
    };
}
