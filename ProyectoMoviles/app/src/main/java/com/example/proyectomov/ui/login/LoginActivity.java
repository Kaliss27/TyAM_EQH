package com.example.proyectomov.ui.login;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.proyectomov.MapsActivity;
import com.example.proyectomov.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends Activity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        auth = FirebaseAuth.getInstance ();

        super.onStart();
    }

    private void login(String email, String password) {
        if(email.length()==0 || password.length()==0){
            Toast.makeText (this, "Rellene los campos", Toast.LENGTH_LONG).show ();
            return;
        }
        auth.signInWithEmailAndPassword (email, password)
                .addOnCompleteListener (task -> {
                    if (task.isSuccessful ()) {
                        FirebaseUser user = auth.getCurrentUser ();
                        String name = "";

                        if (user != null) {
                            name = user.getDisplayName ();
                        }

                        Toast.makeText ( this, "Usuario " + name, Toast.LENGTH_LONG).show ();
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivity (intent);

                    } else {
                        Toast.makeText (this, "Usuario y/o contraseña no reconocida!", Toast.LENGTH_LONG).show ();
                    }
                });
    }

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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        EditText edtEmail = findViewById (R.id.etEmailLogin);
        EditText edtPassword = findViewById (R.id.etPasswordlLogin);

        Button btnLogin = findViewById (R.id.btnLogin);
        btnLogin.setOnClickListener (v -> {
            login (edtEmail.getText ().toString (), edtPassword.getText().toString ());
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intentRegister= new Intent(this,RegisterUserActivity.class);
            startActivity(intentRegister);
        });

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (!Settings.System.canWrite(getApplicationContext()))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);
        }

        if (Settings.System.canWrite(getApplicationContext()))
        {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT);
            {
                int myBrightness = (int) event.values[0];
                Settings.System.putInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, myBrightness);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

}