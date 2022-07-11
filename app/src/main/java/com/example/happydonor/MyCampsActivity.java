package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyCampsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<Camps> campsArrayList;
    private CampAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camps);

        Toolbar toolbar = (findViewById(R.id.toolbarMyCamps));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("My Camps");

        recyclerView = findViewById(R.id.recMyCamps);
        campsArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CampAdapter(campsArrayList, this);
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        loadDataInListview();
    }

    private void loadDataInListview() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference questionRef = db.collection("AllCamps");
        questionRef.whereEqualTo("addedBy", currentUser.getEmail()).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            Camps c = d.toObject(Camps.class);
                            c.setId(d.getId());
                            campsArrayList.add(c);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(MyCampsActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyCampsActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onClick(View v) {

    }
}