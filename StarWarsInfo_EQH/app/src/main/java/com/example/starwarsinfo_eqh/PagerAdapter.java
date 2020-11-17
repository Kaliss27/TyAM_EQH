package com.example.starwarsinfo_eqh;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import Fragments.PeopleF;
import Fragments.PlanetsF;
import Fragments.VehiclesF;

public class PagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> arrayList= new ArrayList<>();

    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment= new Fragment();
        switch (position){
            case 0: fragment = new PlanetsF(); break;
            case 1: fragment = new PeopleF(); break;
            case 2: fragment = new VehiclesF(); break;
        }
        //return arrayList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        //return arrayList.size();
        return 3;
    }

    public void addFragment(Fragment fragment){ arrayList.add(fragment);}
}