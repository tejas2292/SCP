package com.example.scp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scp.Admin.AdminMainActivity;
import com.example.scp.Caretaker.CaretakerMainActivity;
import com.example.scp.Patient.PatientMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button mBtnLogIn;
    EditText mUsername, mPassword;
    DatabaseReference referenceData;
    LoadingDialog loadingDialog;
    String username, password, passwordDB, loginIDDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogIn = findViewById(R.id.btnLogin);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        loadingDialog = new LoadingDialog(LoginActivity.this);

        referenceData = FirebaseDatabase.getInstance().getReference().child("credentials");

        checkSession();

        mBtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Logging In..");
                username = mUsername.getText().toString().trim();
                password = mPassword.getText().toString().trim();

                if (username.equals("") || password.equals("")) {
                    loadingDialog.dismissDialog();
                    if(username.equals("")){
                        mUsername.setError("Enter Username");
                    }
                    else if(password.equals("")){
                        mPassword.setError("Enter Password");
                    }
                } else {
                    referenceData.child("admin").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                passwordDB = dataSnapshot.child("password").getValue().toString();
                               // loginIDDB = dataSnapshot.child("loginId").getValue().toString();
                                if (password.equals(passwordDB)) {
                                    new UserCurrent(LoginActivity.this).setUsername(username);
                                    new UserCurrent(LoginActivity.this).setPass(passwordDB);
                                    loginByDesignation("1");
                                } else {
                                    loadingDialog.dismissDialog();
                                    mPassword.setError("Password is incorrect.");
                                }
                            }
                            else {
                                referenceData.child("caretaker").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            passwordDB = dataSnapshot.child("password").getValue().toString();
                                         //   loginIDDB = dataSnapshot.child("loginId").getValue().toString();
                                            if (password.equals(passwordDB)) {
                                                new UserCurrent(LoginActivity.this).setUsername(username);
                                                new UserCurrent(LoginActivity.this).setPass(passwordDB);
                                                loginByDesignation("2");
                                            } else {
                                                loadingDialog.dismissDialog();
                                                mPassword.setError("Password is incorrect.");
                                            }
                                        } else {
                                            referenceData.child("patient").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        passwordDB = dataSnapshot.child("password").getValue().toString();
                                                        //   loginIDDB = dataSnapshot.child("loginId").getValue().toString();
                                                        if (password.equals(passwordDB)) {
                                                            new UserCurrent(LoginActivity.this).setUsername(username);
                                                            new UserCurrent(LoginActivity.this).setPass(passwordDB);
                                                            loginByDesignation("3");
                                                        } else {
                                                            loadingDialog.dismissDialog();
                                                            mPassword.setError("Password is incorrect.");
                                                        }
                                                    } else {
                                                        loadingDialog.dismissDialog();
                                                        Toast.makeText(LoginActivity.this, "account not present"
                                                                , Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage()
                                                            , Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage()
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void checkSession() {
        loadingDialog.startLoadingDialog();
        if (new UserCurrent(LoginActivity.this).getPass() != "") {
            String p = new UserCurrent(LoginActivity.this).getLoginid();
            loginByDesignation(p);
        } else {
            loadingDialog.dismissDialog();
        }
    }

    private void loginByDesignation(String id) {
        Intent intent = null;
        if (id.equals("1")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, AdminMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (id.equals("2")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, CaretakerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if (id.equals("3")) {
            new UserCurrent(LoginActivity.this).setLoginid(id);
            loadingDialog.dismissDialog();
            intent = new Intent(LoginActivity.this, PatientMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            loadingDialog.dismissDialog();
            new UserCurrent(LoginActivity.this).removeUser();
            Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Are you sure want to exit from app?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        //here exit app alert close............................................
    }

}