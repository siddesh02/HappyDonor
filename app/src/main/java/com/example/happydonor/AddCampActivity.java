package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCampActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText orgBy, campDate, campTime, venue, contact, regLink;
    private TextView txtError;
    private Button post;
    private FirebaseFirestore db;
    private ArrayList<Posts> postArrayList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user = mAuth.getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camp);

        Toolbar toolbar = (findViewById(R.id.toolbarAddCamp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("New Camp");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        db = FirebaseFirestore.getInstance();

        orgBy = (EditText) findViewById(R.id.edtAddCampOrgBy);
        orgBy.setOnClickListener(this);
        campDate = (EditText) findViewById(R.id.edtAddCampDate);
        campDate.setOnClickListener(this);
        campTime = (EditText) findViewById(R.id.edtAddCampTime);
        campTime.setOnClickListener(this);
        venue = (EditText) findViewById(R.id.edtAddCampVenue);
        venue.setOnClickListener(this);
        contact = (EditText) findViewById(R.id.edtAddCampContact);
        contact.setOnClickListener(this);
        regLink = (EditText) findViewById(R.id.edtAddCampLink);
        regLink.setOnClickListener(this);
        post = (Button) findViewById(R.id.btnAddCamp);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        campDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCampActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (!campDate.getText().toString().isEmpty() &&
                    !campTime.getText().toString().isEmpty() &&
                    !contact.getText().toString().isEmpty() &&
                    !orgBy.getText().toString().isEmpty() &&
                    !venue.getText().toString().isEmpty() )
                {
                    Map<String, Object> camps = new HashMap<>();
                    camps.put("addedBy", currentUser.getEmail());
                    camps.put("addedOn", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                    camps.put("cmpDate", campDate.getText().toString().trim());
                    camps.put("cmpTime", campTime.getText().toString().trim());
                    camps.put("contact", contact.getText().toString().trim());
                    camps.put("orgBy", orgBy.getText().toString().trim());
                    camps.put("regLink", regLink.getText().toString().trim());
                    camps.put("venue", venue.getText().toString().trim());

                    db.collection("AllCamps").add(camps).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //String id = documentReference.getId();
                            //db.collection("AllCamps").document(id).update("Id", id);
                            Toast.makeText(AddCampActivity.this, "Camp Added!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddCampActivity.this, ViewCampsActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCampActivity.this, "Camp not added!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    txtError = (TextView) findViewById(R.id.txtCampError);
                    txtError.setText("Fields cannot be empty!");
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}