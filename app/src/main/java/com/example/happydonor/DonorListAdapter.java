package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DonorListAdapter extends FragmentStateAdapter {

    private String[] titles = new String[] { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    public DonorListAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int i) {
        switch (i) {
            case 0:
                return new A1Fragment();

            case 1:
                return new A2Fragment();

            case 2:
                return new B1Fragment();

            case 3:
                return new B2Fragment();

            case 4:
                return new AB1Fragment();

            case 5:
                return new AB2Fragment();

            case 6:
                return new O1Fragment();

            case 7:
                return new O2Fragment();
        }
        return new A1Fragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
