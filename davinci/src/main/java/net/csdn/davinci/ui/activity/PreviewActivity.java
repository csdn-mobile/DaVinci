package net.csdn.davinci.ui.activity;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.csdn.statusbar.StatusBar;
import com.csdn.statusbar.annotation.FontMode;

import net.csdn.davinci.Config;
import net.csdn.davinci.R;
import net.csdn.davinci.utils.PhotoUtils;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);

        StatusBar.Builder()
                .transparent(true)
                .fontMode(FontMode.LIGHT)
                .change(this);

        ImageViewTouch iv = findViewById(R.id.iv);
        iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);


        Point size = PhotoUtils.getBitmapSize(Uri.fromFile(new File(Config.currentPath)), this);
        Config.imageEngine.loadImage(this, size.x, size.y, iv, Config.currentPath);
//        Glide.with(this)
//                .load(Config.currentPath)
//                .apply(new RequestOptions()
//                        .priority(Priority.HIGH)
//                        .fitCenter())
//                .into(iv);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.davinci_fade_in, R.anim.davinci_fade_out);
    }
}
