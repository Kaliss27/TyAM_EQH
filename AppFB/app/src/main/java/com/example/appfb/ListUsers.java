package com.example.appfb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Vector;

public class ListUsers extends Activity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      

    }
    void iniciarSesion () {
        auth.signInAnonymously ()
                .addOnFailureListener(e -> Log.e ("TYAM", "Fail on anonymous auth", e));
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
}
class Users {
    public String name;
    public String lastn;
    public String address;
    public String photo;
    public String phone;
    public int age;
}
class MyAdater extends RecyclerView.Adapter<MyAdater.MyViewHolder> {
    Vector<Users> data;

    public MyAdater (Vector<Users> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdater.MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users user = data.get (position);

        holder.tvName.setText (user.name);
        holder.tvAddress.setText (user.address);
        holder.tvAge.setText (user.age);
        holder.tvPhone.setText (user.phone);
       // holder.tvCompany.setText (user.company);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvLastN, tvAddress, tvAge , tvPhone , tvPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            tvName = itemView.findViewById (R.id.textName);
            //tvLastN = itemView.findViewById (R.id.tex);
            tvAddress = itemView.findViewById (R.id.textDirection);
            tvAge = itemView.findViewById (R.id.textAge);
            tvPhone = itemView.findViewById (R.id.textPhone);
            //tvPhoto = itemView.findViewById (R.id.);
        }
    }
}