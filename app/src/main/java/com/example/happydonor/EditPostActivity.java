package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditPostActivity extends AppCompatActivity {
    private EditText lastDate, patient, donorsGot, donorsNeeded, hospital, contact, postedBy, email, postedOn;
    private Spinner bloodGrp;
    private TextView txtError;
    private String date;
    private String name;
    private int got;
    private int needed;
    private String hosp;
    private String phone;
    private String blood;
    private String email1;
    private String postBy;
    private String postOn;
    private FirebaseFirestore db;
    private ArrayList<Posts> postArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Toolbar toolbar = (findViewById(R.id.toolbarEditPost));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Post");

        Posts posts = (Posts) getIntent().getSerializableExtra("post");
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        lastDate = (EditText) findViewById(R.id.edtEditPostLastDate);
        patient = (EditText) findViewById(R.id.edtEditPostPatient);
        donorsGot = (EditText) findViewById(R.id.edtEditPostDonorsGot);
        donorsNeeded = (EditText) findViewById(R.id.edtEditPostDonorsNeeded);
        hospital = (EditText) findViewById(R.id.edtEditPostHospital);
        contact = (EditText) findViewById(R.id.edtEditPostContact);
        postedBy = (EditText) findViewById(R.id.edtEditPostPostedBy);
        Button editPost = (Button) findViewById(R.id.btnEditPost);

        bloodGrp = (Spinner) findViewById(R.id.spnEditPostBlood);
        ArrayAdapter<String> bgAdapter = new ArrayAdapter<String>(EditPostActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bloodgroup));
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrp.setAdapter(bgAdapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditPostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        lastDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        lastDate.setText(posts.getLastDate());
        patient.setText(posts.getPatient());
        donorsGot.setText(String.valueOf(posts.getDonorsGot()));
        donorsNeeded.setText(String.valueOf(posts.getDonorsNeeded()));
        hospital.setText(posts.getHospital());
        contact.setText(posts.getContact());
        postedBy.setText(posts.getPostedBy());

        switch(posts.getBloodGrp()) {
            case "A+":
                bloodGrp.setSelection(0);
                break;

            case "A-":
                bloodGrp.setSelection(1);
                break;

            case "B+":
                bloodGrp.setSelection(2);
                break;

            case "B-":
                bloodGrp.setSelection(3);
                break;

            case "AB+":
                bloodGrp.setSelection(4);
                break;

            case "AB-":
                bloodGrp.setSelection(5);
                break;

            case "O+":
                bloodGrp.setSelection(6);
                break;

            case "O-":
                bloodGrp.setSelection(7);
                break;
        }

        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = patient.getText().toString().trim();
                date = lastDate.getText().toString().trim();
                got = Integer.parseInt(donorsGot.getText().toString().trim());
                needed = Integer.parseInt(donorsNeeded.getText().toString().trim());
                hosp = hospital.getText().toString().trim();
                phone = contact.getText().toString().trim();
                blood = bloodGrp.getSelectedItem().toString();
                postBy = postedBy.getText().toString().trim();
                email1 = posts.getEmail();
                postOn = posts.getPostedOn();

                if (!blood.isEmpty() && !name.isEmpty() && !date.isEmpty() && !hosp.isEmpty() && !phone.isEmpty() && !postBy.isEmpty() &&
                    !donorsNeeded.getText().toString().isEmpty() &&
                    !donorsGot.getText().toString().isEmpty())
                {
                    if(phone.length() != 10) {
                        contact.setError("Phone no. must be 10 digits");
                        contact.requestFocus();
                        return;
                    }
                    if(needed < got) {
                        donorsGot.setError("Donors got cannot be greater than donors needed!!!");
                        donorsGot.requestFocus();
                        return;
                    }
                }
                else {
                    txtError = (TextView) findViewById(R.id.txtEditPostError);
                    txtError.setText("Fields cannot be empty!");
                    return;
                }
                updatePost(posts, blood, name, hosp, postBy, postOn, date, email1, needed, got, phone);
            }
        });
    }

    private void updatePost(Posts posts, String blood, String name, String hosp, String postedBy, String postedOn, String date, String email, int needed, int got, String phone) {
        Posts updatedPost = new Posts(blood, name, hosp, postedBy, postedOn, date, email, needed, got, phone);

        db.collection("AllPosts").document(posts.getId()).set(updatedPost).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditPostActivity.this, "Post has been updated..", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(EditPostActivity.this, ViewPostsActivity.class));
                        }
                    }, 3000); // 2 seconds
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditPostActivity.this, "Fail to update the post..", Toast.LENGTH_SHORT).show();
                }
            });
    }

}