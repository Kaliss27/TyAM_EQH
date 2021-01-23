package com.example.proyectomov;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class RClinica extends Activity
{
    private static final int SELECT_IMAGE_REQUEST_CODE = 2001;
    public static final int REQUEST_CAMERA_OPEN = 4001;
    public static final int REQUEST_PERMISSION_CAMERA = 3001;

    public EditText edNombre;
    public EditText edDirr;
    public EditText edPhone;

    public TextView tvUbi;
    double lat = 0.0;
    double lng = 0.0;

    public ImageButton btnTake;
    public ImageButton btnGallery;
    public ImageButton btnUbi;

    FirebaseUser user;
    FirebaseStorage storage;

    ImageView iv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_perfil_c);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance ();

        edNombre=findViewById(R.id.etNombre);
        edDirr=findViewById(R.id.etDirr);
        edPhone=findViewById(R.id.etPhone);


        iv=findViewById(R.id.ivSource);

        btnTake=findViewById(R.id.imageButtonC);
        btnTake.setOnClickListener(v->{

            int perm = checkSelfPermission (Manifest.permission.CAMERA);

            if (perm != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA
                );
                return;
            }

            tomarFotoP();
        });

        tvUbi=findViewById(R.id.tvUbi2);
        btnUbi=findViewById(R.id.imageButtonU);
        btnUbi.setOnClickListener(v->{
            miUbicacion();
        });

        btnGallery=findViewById(R.id.imageButtonG);
        btnGallery.setOnClickListener(v->{
            buscarFotoP();
        });


        Button regBtn= findViewById(R.id.btnRegister);
        regBtn.setOnClickListener( v ->{
                    guardarClinica();
                }
        );
    }

    private void tomarFotoP() {
        Intent intentC = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult (intentC, REQUEST_CAMERA_OPEN);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 4001:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
                {
                    tomarFotoP ();
                }
            case 1001:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
                {
                    buscarFotoP ();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_OPEN && resultCode == RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap (bitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void buscarFotoP()
    {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType ("image/*");

        String [] mimeTypes = { "image/jpeg", "image/png" };
        intent.putExtra (Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult (intent, SELECT_IMAGE_REQUEST_CODE);
    }

    private void guardarPFenStorage(){
        StorageReference imagesFolder = storage.getReference ("clinicsPhotos/");
        StorageReference  image = imagesFolder.child (user.getUid()+"_cP.png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        Bitmap bitmap = getBitmapFromDrawable (iv.getDrawable ());
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, bos);
        byte [] buffer = bos.toByteArray ();

        image.putBytes (buffer)
                .addOnFailureListener (e -> {
                    Toast.makeText (getBaseContext (), "Error al guardar la imagen: " + e.getMessage (), Toast.LENGTH_LONG).show ();
                })
                .addOnCompleteListener (task -> {
                    if (task.isComplete ()) {
                        Task<Uri> getUriTask = image.getDownloadUrl ();

                        getUriTask.addOnCompleteListener (t -> {
                            Uri uri = t.getResult ();
                            if (uri == null) return;

                            Toast.makeText (getBaseContext (), "Download URL " + uri.toString (), Toast.LENGTH_LONG).show ();
                            Log.i ("TYAM", "Download URL " + uri.toString ());
                        });
                    }
                });

    }

    private Bitmap getBitmapFromDrawable (Drawable drble)
    {
        if (drble instanceof BitmapDrawable)
        {
            return  ((BitmapDrawable) drble).getBitmap ();
        }
        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);
        return bitmap;
    }

    private void miUbicacion()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,loclistener);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
            tvUbi.setText("lat:"+lat+"/ lng:"+lng);
        }
    }

    LocationListener loclistener = new LocationListener()
    {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {}
    };

    private void guardarClinica() {

        Clinica clinica = new Clinica();

        Editable eedNombre=edNombre.getText();
        Editable eeDirr=edDirr.getText();
        Editable eedPhone=edPhone.getText();



        if (user != null) {
            clinica.nombreC = eedNombre.toString();
            clinica.dirrC = eeDirr.toString();
            clinica.phoneC=eedPhone.toString();

            guardarPFenStorage();

           // miUbicacion();
            clinica.lat=lat;
            clinica.lng=lng;


        } else {
            Toast.makeText(getBaseContext(), "No hay permiso de escritura", Toast.LENGTH_LONG).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Clinica");

        HashMap<String, Object> node = new HashMap<>();
        node.put(user.getUid()+"_C", clinica);

        users.updateChildren(node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getBaseContext(), "Datos registrados correctamente", Toast.LENGTH_LONG).show();
                    Intent actMaps=new Intent(this,MapsActivity.class);
                    startActivity(actMaps);
                })
                .addOnFailureListener(e -> Toast.makeText(getBaseContext(), "Error al registrar los datos " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

class Clinica
{
    public String nombreC;
    public String dirrC;
    public String phoneC;
    public double lat;
    public double lng;
}
