package net.csdn.davinci.core.album;

import net.csdn.davinci.core.bean.Album;

import java.util.ArrayList;

public interface AlbumResultCallback {

    void onResult(ArrayList<Album> albums);
}
