package com.example.starwarsinfo_eqh;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import Fragments.PeopleF;
import Fragments.PlanetsF;
import Fragments.VehiclesF;

public class MainActivity extends AppCompatActivity {
    PagerAdapter pagerAdapter;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);

        pagerAdapter= new PagerAdapter(getSupportFragmentManager(),getLifecycle());
/*
        pagerAdapter.addFragment(new PlanetsF());
        pagerAdapter.addFragment(new PeopleF());
        pagerAdapter.addFragment(new VehiclesF());*/

        viewPager2.setAdapter(pagerAdapter);

        TabLayout tabLayout=findViewById(R.id.tab_layout);

        final String[] tabT= new String[]{
                getString(R.string.text_label_1),
                getString(R.string.text_label_2),
                getString(R.string.text_label_3)
                };

        new TabLayoutMediator(tabLayout,viewPager2,(tab, position) -> tab.setText(tabT[position])).attach();
    }

}
