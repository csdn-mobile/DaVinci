package net.csdn.davinci.core.entity;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Album {
    public String id;
    public String coverPath;
    public String name;
    public Uri uri;
    public List<DavinciPhoto> photoList = new ArrayList<>();
    public List<DavinciVideo> videoList = new ArrayList<>();
}
