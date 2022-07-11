package com.example.happydonor;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FragmentAdapter extends ArrayAdapter<FragmentModel> {
    TextView fullname, bloodGrp, area, alternate, email, lastDonated;
    Button btnDonorCall;

    public FragmentAdapter(@NonNull A1Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull A2Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull B1Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull B2Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull AB1Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull AB2Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull O1Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public FragmentAdapter(@NonNull O2Fragment context, ArrayList<FragmentModel> fragmentModalArrayList) {
        super(context.getActivity(), 0, fragmentModalArrayList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.each_donor, parent, false);
        }

        FragmentModel model = getItem(position);

        fullname = listItemView.findViewById(R.id.txtDonorName);
        bloodGrp = listItemView.findViewById(R.id.txtDonorBloodGrp);
        area = listItemView.findViewById(R.id.txtDonorArea);
        alternate = listItemView.findViewById(R.id.txtDonorAlternatePhone);
        email = listItemView.findViewById(R.id.txtDonorEmail);
        lastDonated = listItemView.findViewById(R.id.txtDonorLastDonated);
        btnDonorCall = listItemView.findViewById(R.id.btnDonorCall);

        btnDonorCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                getContext().startActivity(intent);
            }
        });

        fullname.setText("Name: " + model.getFullname());
        bloodGrp.setText("Blood Group: "+ model.getBloodGrp());
        area.setText("Area: " + model.getArea());
        alternate.setText("Alternate No.: " + model.getAlternate());
        email.setText("Email: " + model.getEmail());
        lastDonated.setText("Last Donated: " + model.getLastDonated());

        return listItemView;
    }
}
