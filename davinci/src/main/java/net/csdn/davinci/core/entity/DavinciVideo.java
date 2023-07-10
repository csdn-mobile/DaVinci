package net.csdn.davinci.core.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class DavinciVideo extends DavinciMedia implements Parcelable {
    public int duration;

    public DavinciVideo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DavinciVideo)) return false;
        DavinciVideo that = (DavinciVideo) o;
        return uri.toString().equals(that.uri.toString());
    }

    /**
     * Parcelable相关
     */
    protected DavinciVideo(Parcel in) {
        this.id = in.readString();
        this.path = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(path);
        dest.writeParcelable(uri, flags);
    }

    public static final Parcelable.Creator<DavinciVideo> CREATOR = new Parcelable.Creator<DavinciVideo>() {
        @Override
        public DavinciVideo createFromParcel(Parcel in) {
            return new DavinciVideo(in);
        }

        @Override
        public DavinciVideo[] newArray(int size) {
            return new DavinciVideo[size];
        }
    };
}