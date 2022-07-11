package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String lastDonated;
    LocalDate date, newDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (findViewById(R.id.toolbarDashboard));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Status");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        TextView myCamps = (TextView) findViewById(R.id.txtYourCamps);
        myCamps.setOnClickListener(this);
        TextView myPosts = (TextView) findViewById(R.id.txtYourPosts);
        myPosts.setOnClickListener(this);
        TextView eligible = (TextView) findViewById(R.id.txtEligibility);
        TextView previousDonation = (TextView) findViewById(R.id.txtYourLastDonatedDate);

        CollectionReference questionRef = db.collection("AllDonors");
        questionRef.whereEqualTo("id", currentUser.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            User modal = d.toObject(User.class);
                            lastDonated = modal.getLastDonated();
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("d-M-yyyy");
                            if(lastDonated.equals("")) {
                                newDate = LocalDate.now();
                            }
                            else {
                                date = LocalDate.parse(lastDonated, df);
                                newDate = date.plusMonths(3);
                                if(newDate.isBefore(LocalDate.now())) {
                                    newDate = LocalDate.now();
                                }
                            }
                            //LocalDate futureDate = LocalDate.now().plusMonths(3);
                            eligible.setText("Your are eligible for donation from : \n\n" + newDate);
                            previousDonation.setText(lastDonated);
                        }
                    }
                    else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(DashboardActivity.this, "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DashboardActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtYourCamps:
                startActivity(new Intent(DashboardActivity.this, MyCampsActivity.class));
                break;

            case R.id.txtYourPosts:
                startActivity(new Intent(DashboardActivity.this, MyPostsActivity.class));
                break;

        }
    }
}