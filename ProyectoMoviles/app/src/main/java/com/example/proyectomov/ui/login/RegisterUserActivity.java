package com.example.proyectomov.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectomov.PerfilActivity;
import com.example.proyectomov.R;
import com.example.proyectomov.RPerfilActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static java.security.AccessController.getContext;

public class RegisterUserActivity extends Activity {
    private FirebaseAuth auth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.only_top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        auth = FirebaseAuth.getInstance ();

        EditText edtEmail = findViewById (R.id.etEmailLogin);
        EditText edtPassword = findViewById (R.id.etPasswordlLogin);

        Button btnRegister = findViewById (R.id.btnRegister);
        btnRegister.setOnClickListener (v -> {
            registerUser (edtEmail.getText().toString (), edtPassword.getText().toString ());
        });

    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword (email, password)
                .addOnCompleteListener (task -> {
                    if (task.isSuccessful ()) {
                        Toast.makeText ( this, "Registro Realizado!", Toast.LENGTH_LONG).show ();
                        Intent intPerfil = new Intent(this, RPerfilActivity.class);
                        startActivity(intPerfil);
                    } else {
                        if (task.getException () != null) {
                            Log.e("TYAM", task.getException().getMessage());
                        }

                        Toast.makeText (this, "Registro fallido!", Toast.LENGTH_LONG).show ();
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
