package com.example.proyectomov;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class PerfilClinicaActivity extends FragmentActivity implements SensorEventListener
{
    SensorManager sensorManager;
    Sensor sensor;

    Clinica clinica;
    FirebaseUser user;

    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_c);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        FirebaseStorage storage = FirebaseStorage.getInstance ();

        user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference folder = storage.getReference ("clinicsPhotos");
        StorageReference imageFile = folder.child (user.getUid()+"_cP.png");

        iv=findViewById(R.id.imageView3);

        final long SIZE_BUFFER = 1024 * 1024;
        imageFile.getBytes (SIZE_BUFFER)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess (byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray (bytes, 0, bytes.length);
                        iv.setImageBitmap (bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e) {
                Toast.makeText (getBaseContext (), "Error: " + e.getMessage (), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent nextAct;
        switch (item.getItemId())
        {
            case R.id.perfil:
                nextAct= new Intent(this, PerfilClinicaActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.map:
                nextAct= new Intent(this,MapsActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.my_list:
                nextAct= new Intent(this,MedicSolicitadoActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.perfil_c:
                nextAct= new Intent(this,Clinica.class);
                startActivity(nextAct);
                return true;

            case R.id.c_sesion:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
