package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditCampActivity extends AppCompatActivity {
    private EditText orgBy, campDate, campTime, venue, contact, regLink;
    private Button post;
    private FirebaseFirestore db;
    private ArrayList<Posts> postArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_camp);

        Toolbar toolbar = (findViewById(R.id.toolbarEditCamp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Camp");

        Camps camps = (Camps) getIntent().getSerializableExtra("camp");
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        orgBy = (EditText) findViewById(R.id.edtEditCampOrgBy);
        campDate = (EditText) findViewById(R.id.edtEditCampDate);
        campTime = (EditText) findViewById(R.id.edtEditCampTime);
        venue = (EditText) findViewById(R.id.edtEditCampVanue);
        contact = (EditText) findViewById(R.id.edtEditCampContact);
        regLink = (EditText) findViewById(R.id.edtEditCampLink);
        post = (Button) findViewById(R.id.btnEditCamp);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        campDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCampActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        campDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        orgBy.setText(camps.getOrgBy());
        campDate.setText(camps.getCmpDate());
        campTime.setText(camps.getCmpTime());
        venue.setText(camps.getVenue());
        contact.setText(camps.getContact());
        regLink.setText(camps.getRegLink());

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgBy1, cmpDate1, cmpTime1, venue1, contact1, regLink1, addedBy1, addedOn1;
                orgBy1 = orgBy.getText().toString().trim();
                cmpDate1 = campDate.getText().toString().trim();
                cmpTime1 = campTime.getText().toString().trim();
                venue1 = venue.getText().toString().trim();
                contact1 = contact.getText().toString().trim();
                regLink1 = regLink.getText().toString().trim();
                addedBy1 = camps.getAddedBy();
                addedOn1 = camps.getAddedOn();
                updateCamp(camps, orgBy1, cmpDate1, cmpTime1, venue1, contact1, regLink1, addedBy1, addedOn1);
            }
        });
    }

    private void updateCamp(Camps camps, String orgBy, String cmpDate, String cmpTime, String venue, String contact, String regLink, String addedBy, String addedOn) {
        Camps updatedCamp = new Camps(orgBy, cmpDate, cmpTime, venue, contact, regLink, addedBy, addedOn);

        db.collection("AllCamps").document(camps.getId()).set(updatedCamp).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditCampActivity.this, "Camp has been updated..", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(EditCampActivity.this, ViewCampsActivity.class));
                        }
                    }, 3000); // 2 seconds
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditCampActivity.this, "Fail to update the camp..", Toast.LENGTH_SHORT).show();
                }
            });
    }
}