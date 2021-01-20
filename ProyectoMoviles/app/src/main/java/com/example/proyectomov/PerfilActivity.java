package com.example.proyectomov;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Objects;


public class PerfilActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil1);

        //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent nextAct;
        switch (item.getItemId()) {
            case R.id.perfil:
                //Toast.makeText ( this, "Perfil!", Toast.LENGTH_LONG).show ();
                nextAct= new Intent(this,PerfilActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.my_list:
                nextAct= new Intent(this,MyListActivity.class);
                startActivity(nextAct);
                return true;
            case R.id.my_cont:
                nextAct= new Intent(this,MyContActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.recibidas:
                nextAct= new Intent(this,RecepcionesActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.map:
                nextAct= new Intent(this,MapsActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.config:
                nextAct= new Intent(this,ConfigActivity.class);
                startActivity(nextAct);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
