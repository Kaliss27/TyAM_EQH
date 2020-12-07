package com.example.appfb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ListUsers extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authentication);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setActionBar (Objects.requireNonNull (myToolbar));


    }
}
