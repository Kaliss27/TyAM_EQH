package com.example.proyectomov;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class RPerfilActivity extends Activity {
    public static final int REQUEST_CAMERA_OPEN = 4001;
    public static final int REQUEST_PERMISSION_CAMERA = 3001;

    public EditText edNombre;
    public EditText edEstado;
    public EditText edPhone;

    public CheckBox cbEsc;
    public CheckBox cbInd;
    public ImageButton btnTake;
    public ImageButton btnGallery;


    ImageView iv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_perfil);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

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


        btnGallery=findViewById(R.id.imageButtonG);
        btnGallery.setOnClickListener(v->{
            buscarFotoP();
        });

        Button regBtn= findViewById(R.id.btnRegister);
        regBtn.setOnClickListener( v ->{
                    guardarUsuario();
                }
        );
    }

    private void tomarFotoP() {
       Intent intentC = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult (intentC, REQUEST_CAMERA_OPEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA)
        {
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarFotoP ();
            }
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



    private void buscarFotoP() {


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

    private void guardarUsuario() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Usuario duser = new Usuario();

        Editable eedNombre=edNombre.getText();
        Editable eedEstado=edEstado.getText();
        Editable eedPhone=edPhone.getText();

        if (user != null) {
            duser.nombre = eedNombre.toString();
            duser.ciudad = eedEstado.toString();
            duser.appPhoto="/";
            duser.appPhone=eedPhone.toString();


            if (cbEsc.isChecked())
                duser.esc = 1;
            if (cbInd.isChecked())
                duser.ind = 1;
        } else {
            Toast.makeText(getBaseContext(), "No hay permiso de escritura", Toast.LENGTH_LONG).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Usuarios");

        HashMap<String, Object> node = new HashMap<>();
        node.put(user.getUid(), duser);

        users.updateChildren(node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getBaseContext(), "Datos registrados correctamente", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getBaseContext(), "Error al registrar los datos " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /*public void onCheckboxClicked(View view) {
        if(cbEsc.isChecked())
        {
            frgDC.setVisibility(View.VISIBLE);
            edNombreC=frgDC.findViewById(R.id.etCNombre);
            edCiudadC=frgDC.findViewById(R.id.etCiudad);
            edPhoneC=frgDC.findViewById(R.id.etCPhone);
        }
        else
            frgDC.setVisibility(View.GONE);
    }*/
    @Override
    protected void onStart() {
        super.onStart();
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
}

class Clinica
{
    public String nombreC;
    public String ciudadC;
    public String appPhotoC;
    public String fk_user;
    public String phoneC;
}
