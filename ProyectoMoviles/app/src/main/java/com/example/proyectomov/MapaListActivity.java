package com.example.proyectomov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectomov.ui.login.LoginActivity;

import java.util.Objects;

public class MapaListActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setContentView(R.layout.list_clinica);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater ().inflate (R.menu.top_app_bar2, menu);
        return super.onCreateOptionsMenu(menu);

        //getMenuInflater ().inflate (R.menu.top_app_bar3, menu);
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent nextAct;
        switch (item.getItemId()) {
            case R.id.perfil:
                nextAct = new Intent(this, PerfilActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.my_list:
                nextAct = new Intent(this, MyListActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.map:
                nextAct = new Intent(this, MapaListActivity.class);
                startActivity(nextAct);
                return true;

            case R.id.c_sesion:
                finish();
                nextAct = new Intent(this, LoginActivity.class);
                startActivity(nextAct);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
