package com.example.appfb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends Activity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setActionBar (Objects.requireNonNull (myToolbar));

    }
    

}
