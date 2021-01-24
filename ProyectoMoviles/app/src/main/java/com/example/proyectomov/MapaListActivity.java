package com.example.proyectomov;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomov.Clinica;
import com.example.proyectomov.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Vector;

public class MapaListActivity extends Activity {
    FirebaseAuth auth;
    Vector<Clinica> vClinics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_clinics);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        RecyclerView recyclerView = findViewById(R.id.rvClinics);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        vClinics = new Vector<>();

        //auth = FirebaseAuth.getInstance ();
        //iniciarSesion ();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference clinicas = database.getReference("Clinica");


        clinicas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {

                    Clinica c = item.getValue(Clinica.class);
                    vClinics.add(c);
                    Log.d("TAG", c.nombreC + " " + c.phoneC);
                }

                recyclerView.setAdapter(new ClinicAdapter(vClinics));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TYAM", databaseError.getMessage());
            }
        });
    }

    void iniciarSesion() {
        auth.signInAnonymously()
                .addOnFailureListener(e -> Log.e("TYAM", "Fail on anonymous auth", e));
    }
}

class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.MyViewHolder> {

    Vector<Clinica> data;

    public ClinicAdapter(Vector<Clinica> data) {
        this.data = data;

    }

    @NonNull
    @Override
    public ClinicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_clinics_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicAdapter.MyViewHolder holder, int position) {
        Clinica clinica = data.get (position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDir,tvPhone;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            tvName=itemView.findViewById(R.id.tvNombreA);
            tvPhone=itemView.findViewById(R.id.tvPhoneA);
            tvDir=itemView.findViewById(R.id.tvDirr);

        }
    }}

