package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtRegister, txtError, gotologin;
    private EditText edtPersonName, edtPhone, edtAlternatePhone, edtEmail, edtConfirmPassword, edtPassword, edtLastDonated;
    private Button btnRegister;
    private Spinner spnArea, spnBloodGroup;
    private FirebaseFirestore db;
    DatePickerDialog.OnDateSetListener setListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*Toolbar toolbar = (findViewById(R.id.toolbarRegister));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Happy Donor");*/

        gotologin = (TextView) findViewById(R.id.gotologin);
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        txtRegister = (TextView) findViewById(R.id.txtRegisterRegister);
        txtRegister.setOnClickListener(this);

        edtPersonName = (EditText) findViewById(R.id.edtRegisterPersonName);
        edtPersonName.setOnClickListener(this);

        edtPhone = (EditText) findViewById(R.id.edtRegisterPhone);
        edtPhone.setOnClickListener(this);

        edtAlternatePhone = (EditText) findViewById(R.id.edtRegisterAlternatePhone);
        edtAlternatePhone.setOnClickListener(this);

        edtEmail = (EditText) findViewById(R.id.edtRegisterEmail);
        edtEmail.setOnClickListener(this);

        edtConfirmPassword = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        edtConfirmPassword.setOnClickListener(this);

        edtPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtPassword.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegisterRegister);
        btnRegister.setOnClickListener(this);

        edtLastDonated = (EditText) findViewById(R.id.edtRegisterLastDonated);
        edtLastDonated.setOnClickListener(this);

        spnArea = (Spinner) findViewById(R.id.spnRegisterArea);
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
                RegisterActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.area)
        );
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnArea.setAdapter(areaAdapter);

        spnBloodGroup = (Spinner) findViewById(R.id.spnRegisterBloodGroup);
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(
                RegisterActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.bloodgroup)
        );
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBloodGroup.setAdapter(bloodGroupAdapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtLastDonated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                edtLastDonated.setText(date);
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtRegisterRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.btnRegisterRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fullname = edtPersonName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pwd = edtPassword.getText().toString().trim();
        String confirmpwd = edtConfirmPassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String alternate = edtAlternatePhone.getText().toString().trim();
        String area = spnArea.getSelectedItem().toString().trim();
        String bloodGrp = spnBloodGroup.getSelectedItem().toString().trim();
        String lastDonated = edtLastDonated.getText().toString().trim();

        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if(alternate.isEmpty())
            alternate = "";

        if(lastDonated.isEmpty())
            lastDonated = "";

        if(!fullname.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !confirmpwd.isEmpty() && !phone.isEmpty() && !area.isEmpty() && !bloodGrp.isEmpty()) {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Email invalid!");
                edtEmail.requestFocus();
                return;
            }

            if(phone.length() != 10) {
                edtPhone.setError("Phone no. must be 10 digits");
                edtPhone.requestFocus();
                return;
            }

            if(!alternate.isEmpty()) {
                if(alternate.length() != 10) {
                    edtAlternatePhone.setError("Phone no. must be 10 digits");
                    edtAlternatePhone.requestFocus();
                    return;
                }
            }

            if(phone.equals(alternate)) {
                edtAlternatePhone.setError("Alternate phone number must be different");
                edtAlternatePhone.requestFocus();
                return;
            }

            if(pwd.length() < 8) {
                edtPassword.setError("Password must be greater than 8 characters!");
                edtPassword.requestFocus();
                return;
            }

            if(!confirmpwd.equals(pwd)) {
                edtConfirmPassword.setError("Confirm password again!");
                edtConfirmPassword.requestFocus();
                return;
            }
        }
        else {
            txtError = (TextView) findViewById(R.id.txtError);
            txtError.setText("Fields cannot be empty!");
            return;
        }

        String finalAlternate = alternate;
        String finalLastDonated = lastDonated;
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String newPass =  sha256(pwd).toString();
                            User user = new User(mAuth.getUid(),fullname, email, newPass, phone, finalAlternate, area, bloodGrp, finalLastDonated);
                            db = FirebaseFirestore.getInstance();
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "User added", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            //Toast.makeText(RegisterActivity.this, "User not added", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            CollectionReference addDonor = db.collection("AllDonors");
                            addDonor.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RegisterActivity.this, "You are successfully registered as a donor!", Toast.LENGTH_LONG).show();
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }, 5000); // 5 seconds
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(RegisterActivity.this,"Registration failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Email already exists!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}