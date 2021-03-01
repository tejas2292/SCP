package com.example.scp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scp.R;
import com.example.scp.Uploads.CaretakerUpload;
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

public class RegistrationCaretakerActivity extends AppCompatActivity {
    EditText mName, mPhoneNo, mClass_std, mPassword;
    LoadingDialog loadingDialog;
    DatabaseReference referenceData;
    String name, phoneNo, class_std, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_caretaker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registration Caretaker Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingDialog = new LoadingDialog(RegistrationCaretakerActivity.this);
        referenceData = FirebaseDatabase.getInstance().getReference().child("credentials");

        mName = findViewById(R.id.name);
        mPhoneNo = findViewById(R.id.roll_no);
        mClass_std = findViewById(R.id.class_std);
        mPassword = findViewById(R.id.password);
    }

    public void onRegisterCaretakerByAdmin(View view) {
        loadingDialog.startLoadingDialog();
        loadingDialog.setText("Creating Account..");

        name = mName.getText().toString();
        phoneNo = mPhoneNo.getText().toString();
        class_std = mClass_std.getText().toString();
        password = mPassword.getText().toString();

        if (name.equals("") || phoneNo.equals("") || class_std.equals("") || password.equals("")) {
            loadingDialog.dismissDialog();
            if (name.equals("")) {
                mName.setError("Name is required.");
            } else if (phoneNo.equals("")) {
                mPhoneNo.setError("Roll No is required.");
            } else if (class_std.equals("")) {
                mClass_std.setError("Class is required.");
            }else if (password.equals("")) {
                mPassword.setError("Password is required.");
            }
        }
        else {
            final CaretakerUpload caretakerUpload = new CaretakerUpload(class_std, name, password, phoneNo, phoneNo);

            referenceData.child("caretaker").child(phoneNo).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        loadingDialog.dismissDialog();
                        Toast.makeText(RegistrationCaretakerActivity.this, "This phone no is already registered.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        referenceData.child("caretaker").child(phoneNo).setValue(caretakerUpload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegistrationCaretakerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationCaretakerActivity.this, AdminMainActivity.class));
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
        Intent intent2 = new Intent(RegistrationCaretakerActivity.this, AdminMainActivity.class);
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