package com.example.happydonor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class A2Fragment extends Fragment {

    ListView listView;
    Button btnDonorCall;
    ArrayList<FragmentModel> fragmentModalArrayList;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a2, container, false);
        listView = v.findViewById(R.id.listViewA2);
        fragmentModalArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        loadDataInListview();
        return v;
    }

    private void loadDataInListview() {
        CollectionReference questionRef = db.collection("AllDonors");
        questionRef.whereEqualTo("bloodGrp", "A-").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        FragmentModel modal = d.toObject(FragmentModel.class);
                        fragmentModalArrayList.add(modal);
                    }
                    FragmentAdapter adapter1 = new FragmentAdapter(A2Fragment.this, fragmentModalArrayList);
                    listView.setAdapter(adapter1);
                }
                else {
                    // if the snapshot is empty we are displaying a toast message.
                    Toast.makeText(getActivity(), "No users found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                }
            });
    }
}