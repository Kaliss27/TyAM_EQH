package com.example.proyectomov.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectomov.R;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

public class RegisterUserActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registro);

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
