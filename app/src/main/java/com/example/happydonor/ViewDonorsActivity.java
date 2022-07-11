package com.example.happydonor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ViewDonorsActivity extends AppCompatActivity {

    private String[] titles = new String[] { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donors);

        Toolbar toolbar = (findViewById(R.id.toolbarDonor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Donors");

        ViewPager2 viewPager2 = (ViewPager2) findViewById(R.id.donorsView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.donorstab);
        DonorListAdapter donorListAdapter = new DonorListAdapter(this);

        viewPager2.setAdapter(donorListAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();
    }
}