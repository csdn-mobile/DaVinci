package net.csdn.davinci.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.R;
import net.csdn.davinci.ui.view.EmptyView;
import net.csdn.davinci.ui.view.PhotoAlbum;
import net.csdn.davinci.ui.view.PhotoNavigation;

public class PhotoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private EmptyView emptyView;
    private PhotoAlbum photoAlbum;
    private PhotoNavigation photoNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        StatusBar.Builder()
                .color(R.color.davinci_white)
                .fontMode(FontMode.DARK)
                .change(this);

        rv = findViewById(R.id.rv);
        emptyView = findViewById(R.id.empty);
        photoAlbum = findViewById(R.id.album);
        photoNavigation = findViewById(R.id.navigation);

        setListener();
    }

    @Override
    public void onBackPressed() {
        if (photoAlbum.getVisibility() == View.VISIBLE) {
            photoAlbum.closeAlbum();
            photoNavigation.setArrowDown();
        } else {
            super.onBackPressed();
        }
    }

    private void setListener() {
        photoNavigation.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        photoNavigation.setOnTitleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAlbum.getVisibility() == View.GONE) {
                    photoAlbum.openAlbum();
                    photoNavigation.setArrowUp();
                } else {
                    photoAlbum.closeAlbum();
                    photoNavigation.setArrowDown();
                }
            }
        });
    }
}
