package com.example.appfb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends Activity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Toolbar mytoolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (mytoolbar));
    }

    @Override
    protected void onStart() {
        auth = FirebaseAuth.getInstance ();

        EditText edtEmail = findViewById (R.id.etEmailLogin);
        EditText edtPassword = findViewById (R.id.etPasswordlLogin);

        Button btnLogin = findViewById (R.id.btnLogin);
        btnLogin.setOnClickListener (v -> {
            login (edtEmail.getText ().toString (), edtPassword.getText().toString ());
        });
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_ab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.mnuUser:
                Intent intent = new Intent(this, UserRegister.class);
                startActivity (intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login (String email, String password) {
        auth.signInWithEmailAndPassword (email, password)
                .addOnCompleteListener (task -> {
                    if (task.isSuccessful ()) {
                        FirebaseUser user = auth.getCurrentUser ();
                        String name = "";

                        if (user != null) {
                            name = user.getDisplayName ();
                        }
                        Toast.makeText ( this, "Usuario " + name, Toast.LENGTH_LONG).show ();
                        Intent intent = new Intent(this, ListUsers.class);
                        startActivity (intent);

                    } else {
                        Toast.makeText (this, "Usuario y/o contraseña no reconocida!", Toast.LENGTH_LONG).show ();
                    }
                });
    }
}
