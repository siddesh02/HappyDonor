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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private FirebaseDatabase reference1;
    private String UserId;
    DatePickerDialog.OnDateSetListener setListener;
    private TextView txtError;
    private EditText edtPersonName, edtPhone, edtAlternatePhone, edtEmail, edtConfirmPassword, edtPassword, edtLastDonated;
    private Button btnEdit;
    private Spinner spnArea, spnBloodGroup;
    User u, modal;
    String Id;
    private Object doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (findViewById(R.id.toolbarEditProfile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Profile");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        UserId = currentUser.getUid();

        edtPersonName = (EditText) findViewById(R.id.edtEditPersonName);
        edtPersonName.setOnClickListener(this);
        edtPhone = (EditText) findViewById(R.id.edtEditPhone);
        edtPhone.setOnClickListener(this);
        edtAlternatePhone = (EditText) findViewById(R.id.edtEditAlternatePhone);
        edtAlternatePhone.setOnClickListener(this);
        edtEmail = (EditText) findViewById(R.id.edtEditEmail);
        edtEmail.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtEditPassword);
        edtPassword.setOnClickListener(this);
        edtConfirmPassword = (EditText) findViewById(R.id.edtEditConfirmPassword);
        edtConfirmPassword.setOnClickListener(this);
        edtLastDonated = (EditText) findViewById(R.id.edtEditLastDonated);
        edtLastDonated.setOnClickListener(this);
        txtError = (TextView) findViewById(R.id.txtError1);
        btnEdit = (Button) findViewById(R.id.btnEditProfile);

        spnArea = (Spinner) findViewById(R.id.spnEditArea);
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
                EditProfileActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.area)
        );
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnArea.setAdapter(areaAdapter);

        spnBloodGroup = (Spinner) findViewById(R.id.spnEditBloodGroup);
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(
                EditProfileActivity.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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



        CollectionReference questionRef = db.collection("AllDonors");
        questionRef.whereEqualTo("id", currentUser.getUid()).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Id = d.getId();
                        modal = d.toObject(User.class);
                        edtPersonName.setText(modal.getFullname());
                        edtPhone.setText(modal.getPhone());
                        edtAlternatePhone.setText(modal.getAlternate());
                        edtLastDonated.setText(modal.getLastDonated());
                        edtEmail.setText(modal.getEmail());
                        switch (modal.getArea()) {
                            case "Margao": spnArea.setSelection(0);
                                break;

                            case "Vasco": spnArea.setSelection(1);
                                break;

                            case "Ponda": spnArea.setSelection(2);
                                break;

                            case "Panaji": spnArea.setSelection(3);
                                break;

                            case "Valpoi": spnArea.setSelection(4);
                                break;

                            case "Marcel": spnArea.setSelection(5);
                                break;

                            case "Sanquelim": spnArea.setSelection(6);
                                break;

                            case "Bicholim": spnArea.setSelection(7);
                                break;

                            case "Assonora": spnArea.setSelection(8);
                                break;

                            case "Mapusa": spnArea.setSelection(9);
                                break;
                        }
                        switch(modal.getBloodGrp()) {
                            case "A+": spnBloodGroup.setSelection(0);
                                break;

                            case "A-":spnBloodGroup.setSelection(1);
                                break;

                            case "B+": spnBloodGroup.setSelection(2);
                                break;

                            case "B-": spnBloodGroup.setSelection(3);
                                break;

                            case "AB+": spnBloodGroup.setSelection(4);
                                break;

                            case "AB-": spnBloodGroup.setSelection(5);
                                break;

                            case "O+": spnBloodGroup.setSelection(6);
                                break;

                            case "O-": spnBloodGroup.setSelection(7);
                                break;
                        }
                    }
                }
                else {
                    // if the snapshot is empty we are displaying a toast message.
                    Toast.makeText(EditProfileActivity.this, "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                }
            });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtPersonName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pwd = edtPassword.getText().toString().trim();
                String confirmPwd = edtConfirmPassword.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String alternate = edtAlternatePhone.getText().toString().trim();
                String area = spnArea.getSelectedItem().toString().trim();
                String bloodGrp = spnBloodGroup.getSelectedItem().toString().trim();
                String lastDonated = edtLastDonated.getText().toString().trim();

                if(!fullname.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !confirmPwd.isEmpty() && !phone.isEmpty() && !area.isEmpty() && !bloodGrp.isEmpty() && !lastDonated.isEmpty()) {
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
                        if(phone.equals(alternate)) {
                            edtAlternatePhone.setError("Alternate phone no must be different!");
                            edtAlternatePhone.requestFocus();
                            return;
                        }
                    }

                    if(pwd.length() < 8) {
                        edtPassword.setError("Password must be greater than 8 characters!");
                        edtPassword.requestFocus();
                        return;
                    }

                    if(!confirmPwd.equals(pwd)) {
                        edtConfirmPassword.setError("Confirm password again!");
                        edtConfirmPassword.requestFocus();
                        return;
                    }
                }
                else {
                    txtError = (TextView) findViewById(R.id.txtError1);
                    txtError.setText("Fields cannot be empty!");
                    return;
                }
                updateProfile(Id, fullname, email, pwd, phone, alternate, area, bloodGrp, lastDonated);
            }
        });
    }

    private void updateProfile(String id, String fullname, String email, String pwd, String phone, String alternate, String area, String bloodGrp, String lastDonated) {
        String newPass =  sha256(pwd).toString();
        User updatedDonor = new User(currentUser.getUid(), fullname, email, newPass, phone, alternate, area, bloodGrp, lastDonated);

        db.collection("AllDonors").document(id).set(updatedDonor)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(EditProfileActivity.this, ViewPostsActivity.class));
                        }
                    }, 3000); // 3 seconds
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Profile Update Failed!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {}
}