package com.abhiandroid.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;

/**
 * Created by uditsetia on 24/1/18.
 */

public class CreatePlayListDialog extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_playlist_dialog);

        Button btnOk = findViewById(R.id.btn);
    }
}
