package com.example.happydonor;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText lastDate, patient, donorsGot, donorsNeeded, hospital, contact, postedBy;
    private TextView showLastDate, txtError;
    private Button post, reset;
    private Spinner bloodGrp;
    private FirebaseFirestore db;
    private ArrayList<Posts> postArrayList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = (findViewById(R.id.toolbarPostPost));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("New Post");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        db = FirebaseFirestore.getInstance();

        patient = (EditText) findViewById(R.id.edtPostPostPatient);
        patient.setOnClickListener(this);

        contact = (EditText) findViewById(R.id.edtPostPostContact);
        contact.setOnClickListener(this);

        postedBy = (EditText) findViewById(R.id.edtPostPostPostedBy);
        postedBy.setOnClickListener(this);

        donorsGot = (EditText) findViewById(R.id.edtPostPostDonorsGot);
        donorsGot.setOnClickListener(this);

        donorsNeeded = (EditText) findViewById(R.id.edtPostPostDonorsNeeded);
        donorsNeeded.setOnClickListener(this);

        hospital = (EditText) findViewById(R.id.edtPostPostHospital);
        hospital.setOnClickListener(this);

        bloodGrp = (Spinner) findViewById(R.id.spnPostPostBlood);
        ArrayAdapter<String> bgAdapter = new ArrayAdapter<String>(AddPostActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bloodgroup));
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrp.setAdapter(bgAdapter);

        lastDate = (EditText) findViewById(R.id.edtPostPostLastDate);
        showLastDate = (TextView) findViewById(R.id.txtPostPostShowDate);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPostActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        post = (Button)findViewById(R.id.btnPostPost);
        String phone = contact.getText().toString().trim();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if(!bloodGrp.getSelectedItem().toString().isEmpty() &&
                    !patient.getText().toString().isEmpty() &&
                    !lastDate.getText().toString().isEmpty() &&
                    !hospital.getText().toString().isEmpty() &&
                    !contact.getText().toString().isEmpty() &&
                    !donorsNeeded.getText().toString().isEmpty() &&
                    !donorsGot.getText().toString().isEmpty() &&
                    !postedBy.getText().toString().isEmpty())
                {
                    if(contact.getText().toString().trim().length() != 10) {
                        contact.setError("Phone no. must be 10 digits");
                        contact.requestFocus();
                        return;
                    }
                    if(Integer.parseInt(donorsNeeded.getText().toString()) < Integer.parseInt(donorsGot.getText().toString())) {
                        donorsGot.setError("Donors got cannot be greater than donors needed!!!");
                        donorsGot.requestFocus();
                        return;
                    }
                }
                else {
                    txtError = (TextView) findViewById(R.id.txtPostError);
                    txtError.setText("Fields cannot be empty!");
                    return;
                }
                Map<String, Object> posts = new HashMap<>();
                posts.put("bloodGrp", bloodGrp.getSelectedItem().toString().trim());
                posts.put("patient", patient.getText().toString().trim());
                posts.put("lastDate", lastDate.getText().toString().trim());
                posts.put("hospital", hospital.getText().toString().trim());
                posts.put("donorsNeeded", Integer.parseInt(donorsNeeded.getText().toString()));
                posts.put("donorsGot", Integer.parseInt(donorsGot.getText().toString()));
                posts.put("postedBy", postedBy.getText().toString().trim());
                assert currentUser != null;
                posts.put("email", currentUser.getEmail());
                posts.put("contact", contact.getText().toString().trim());
                posts.put("postedOn", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                db.collection("AllPosts").add(posts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //String id = documentReference.getId();
                        //db.collection("AllPosts").document(id).update("Id", id);
                        Toast.makeText(AddPostActivity.this, "Post Added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPostActivity.this, ViewPostsActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPostActivity.this, "Post not added!", Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });

        reset = (Button) findViewById(R.id.btnPostPostResetPost);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPostActivity.this, AddPostActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}