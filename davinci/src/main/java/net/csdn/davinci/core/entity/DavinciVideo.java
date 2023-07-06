package net.csdn.davinci.core.entity;

public class DavinciVideo extends DavinciMedia {
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
}
