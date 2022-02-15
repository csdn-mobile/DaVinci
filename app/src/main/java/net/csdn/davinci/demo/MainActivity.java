package net.csdn.davinci.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.csdn.davinci.DaVinci;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaVinci.create()
                .select(this, 10000);
    }
}