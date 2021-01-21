package com.example.proyectomov;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ConfigActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.configuracion);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
