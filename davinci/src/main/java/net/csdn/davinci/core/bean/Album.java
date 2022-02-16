package net.csdn.davinci.core.bean;

import java.util.ArrayList;
import java.util.List;

public class Album {
    public String id;
    public String coverPath;
    public String name;
    public List<Photo> photoList = new ArrayList<>();
}
