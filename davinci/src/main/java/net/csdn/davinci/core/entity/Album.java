package net.csdn.davinci.core.entity;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Album {
    public String id;
    public String coverPath;
    public Uri uri;
    public String name;
    public List<Photo> photoList = new ArrayList<>();
}
