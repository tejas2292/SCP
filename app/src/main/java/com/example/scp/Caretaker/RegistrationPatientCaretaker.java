package com.example.scp.Caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scp.Admin.AdminMainActivity;
import com.example.scp.Admin.RegistrationPatientAdmin;
import com.example.scp.R;
import com.example.scp.Uploads.PatientUpload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationPatientCaretaker extends AppCompatActivity {
    EditText mName, mRoll_no, mClass_std, mPassword;
    LoadingDialog loadingDialog;
    DatabaseReference referenceData;
    String name, roll_no, class_std, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_patient_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registration Patient Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingDialog = new LoadingDialog(RegistrationPatientCaretaker.this);
        referenceData = FirebaseDatabase.getInstance().getReference().child("credentials");

        mName = findViewById(R.id.name);
        mRoll_no = findViewById(R.id.roll_no);
        mClass_std = findViewById(R.id.class_std);
        mPassword = findViewById(R.id.password);
    }

    public void onRegisterPatientByAdmin(View view) {
        loadingDialog.startLoadingDialog();
        loadingDialog.setText("Creating Account..");

        name = mName.getText().toString();
        roll_no = mRoll_no.getText().toString();
        class_std = mClass_std.getText().toString();
        password = mPassword.getText().toString();

        if (name.equals("") || roll_no.equals("") || class_std.equals("") || password.equals("")) {
            loadingDialog.dismissDialog();
            if (name.equals("")) {
                mName.setError("Name is required.");
            } else if (roll_no.equals("")) {
                mRoll_no.setError("Roll No is required.");
            } else if (class_std.equals("")) {
                mClass_std.setError("Class is required.");
            }else if (password.equals("")) {
                mPassword.setError("Password is required.");
            }
        }
        else {
            final PatientUpload patientUpload = new PatientUpload(class_std, name, password, roll_no, roll_no);

            referenceData.child("patient").child(roll_no).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        loadingDialog.dismissDialog();
                        Toast.makeText(RegistrationPatientCaretaker.this, "This roll no is already registered.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        referenceData.child("patient").child(roll_no).setValue(patientUpload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegistrationPatientCaretaker.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationPatientCaretaker.this, CaretakerMainActivity.class));
                                finish();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(RegistrationPatientCaretaker.this, CaretakerMainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}