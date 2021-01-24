package com.example.proyectomov;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class RPerfilActivity extends Activity implements SensorEventListener
{
    SensorManager sensorManager;
    Sensor sensor;

    public static final int SELECT_IMAGE_REQUEST_CODE = 2001;
    public static final int REQUEST_CAMERA_OPEN = 4001;
    public static final int REQUEST_PERMISSION_CAMERA = 3001;

    public EditText edNombre;
    public EditText edEstado;
    public EditText edPhone;

    public CheckBox cbEsc;
    public CheckBox cbInd;
    public ImageButton btnTake;
    public ImageButton btnGallery;

    FirebaseUser user;
    FirebaseStorage storage;

    String pPuri;

    ImageView iv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_perfil);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance ();

        edNombre=findViewById(R.id.etNombre);
        edEstado=findViewById(R.id.etEstado);
        edPhone=findViewById(R.id.etPhone);

        cbEsc=findViewById(R.id.checkbox_esc);
        cbInd=findViewById(R.id.checkbox_ind);

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


        btnGallery = findViewById(R.id.imageButtonG);

        btnGallery.setOnClickListener(v->{
            int perm = checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE);

            if (perm != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions (
                        new String [] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        1001
                );
                return;
            }

            buscarFotoP();
        });

        Button regBtn= findViewById(R.id.btnRegister);
        regBtn.setOnClickListener( v ->{
                    guardarUsuario();
                }
        );

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
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
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == REQUEST_CAMERA_OPEN && resultCode == RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bitmap);
        }

        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) return;

            Uri uri = data.getData ();
            iv.setImageURI (uri);
        }

        super.onActivityResult (requestCode, resultCode, data);
    }

    private void tomarFotoP()
    {
        Intent intentC = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult (intentC, REQUEST_CAMERA_OPEN);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void buscarFotoP()
    {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType ("image/*");

        String [] mimeTypes = { "image/jpeg", "image/png", "image/jpg" };
        intent.putExtra (Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult (intent, SELECT_IMAGE_REQUEST_CODE);
    }

    private void guardarPFenStorage()
    {
        StorageReference imagesFolder = storage.getReference ("profilePhotos/");
        StorageReference  image = imagesFolder.child (user.getUid()+"_pP.png");

        Bitmap bitmap = getBitmapFromDrawable (iv.getDrawable ());
        ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.JPEG, 100, bos);
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

                            pPuri=getUriTask.toString();
                          //  Toast.makeText (getBaseContext (), "Download URL " + uri.toString (), Toast.LENGTH_LONG).show ();
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

    private void guardarUsuario()
    {
        Usuario duser = new Usuario();

        Editable eedNombre=edNombre.getText();
        Editable eedEstado=edEstado.getText();
        Editable eedPhone=edPhone.getText();


        if (user != null)
        {
            duser.nombre = eedNombre.toString();
            duser.ciudad = eedEstado.toString();
            duser.appPhone = eedPhone.toString();

            guardarPFenStorage();

            duser.appPhoto=pPuri;

            if (cbEsc.isChecked())
                duser.esc = 1;
            if (cbInd.isChecked())
                duser.ind = 1;
        }
        else
        {
            Toast.makeText(getBaseContext(), "No hay permiso de escritura", Toast.LENGTH_LONG).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Usuarios");

        HashMap<String, Object> node = new HashMap<>();
        node.put(user.getUid(), duser);

        users.updateChildren(node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getBaseContext(), "Datos registrados correctamente", Toast.LENGTH_LONG).show();
                    if(duser.esc!=1)
                    {
                        Intent actMaps=new Intent(this,MapsActivity.class);
                        startActivity(actMaps);
                    }else{
                        Intent actRC=new Intent(this,RClinica.class);
                        startActivity(actRC);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getBaseContext(), "Error al registrar los datos " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onStart() {
        super.onStart();
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

class Usuario
{
    public String nombre;
    public String ciudad;
    public String appPhoto;
    public String appPhone;
    public int esc;
    public int ind;

    public Usuario (String nombre, String ciudad, String appPhoto, String appPhone, int esc, int ind)
    {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.appPhoto = appPhoto;
        this.appPhone = appPhone;
        this.esc = esc;
        this.ind = ind;
    }
    public Usuario(){}

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getCiudad()
    {
        return ciudad;
    }

    public void setCiudad(String ciudad)
    {
        this.ciudad = ciudad;
    }

    public String getPhoto()
    {
        return appPhoto;
    }

    public void setPhoto(String appPhoto)
    {
        this.appPhoto = appPhoto;
    }

    public String getPhone()
    {
        return appPhone;
    }

    public void setPhone(String appPhone)
    {
        this.appPhone = appPhone;
    }

    public int getEsc()
    {
        return esc;
    }

    public void setEsc(int esc)
    {
        this.esc = esc;
    }

    public int getInd()
    {
        return ind;
    }

    public void setInd(int ind)
    {
        this.ind = ind;
    }
}

class MyAdater extends RecyclerView.Adapter<MyAdater.MyViewHolder>
{
    Vector<Usuario> data;

    public MyAdater(Vector<Usuario> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registro_perfil, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdater.MyViewHolder holder, int position)
    {
        Usuario song = data.get(position);

        holder.tvNombre.setText(song.nombre);
        holder.tvCiudad.setText(song.ciudad);
        holder.tvPhone.setText(song.appPhone);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvNombre, tvCiudad, tvPhone;
        //ImageView Photo;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.textView);
            tvCiudad = itemView.findViewById(R.id.textView2);
            //Photo = itemView.findViewById(R.id.imageView3);
            tvPhone = itemView.findViewById(R.id.textView3);
        }
    }
}


