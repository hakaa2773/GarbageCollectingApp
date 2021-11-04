package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverRegisterActivity extends AppCompatActivity {
    private EditText txtname, txtphone, txtemail,txttrucknumber, txtpassword;
    private String role = "driver";
    private Button btnregister,btncancel;
    FirebaseAuth authentication;
    DatabaseReference db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        txtname = findViewById(R.id.txtName);
        txtphone = findViewById(R.id.txtphone);
        txtemail= findViewById(R.id.txtemail);
        txttrucknumber = findViewById(R.id.txttrucknumber);
        txtpassword = findViewById(R.id.txtpassword);
        btnregister = findViewById(R.id.btndriverregister);
        btncancel = findViewById(R.id.btncaceld);
        authentication = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverRegisterActivity.this,AdminHomeActivity.class));
                finish();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, phone, email, truck_number, password;
                name = txtname.getText().toString();
                phone = txtphone.getText().toString();
                email = txtemail.getText().toString();
                truck_number = txttrucknumber.getText().toString();
                password = txtpassword.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || truck_number.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "please fill all the text fields", Toast.LENGTH_SHORT).show();
                } else {
                    createDriver(name, phone, email, truck_number, password);
                }
            }
        });
    }

            private void createDriver(String name, String phone, String email, String truck_number, String password) {
                authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i("DriverRegisterActivity","Successfully Register");
                            String driverid = authentication.getCurrentUser().getUid();
                            addDriver(driverid,name,phone,email,truck_number,role);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverRegisterActivity.this, "Registration fail try again", Toast.LENGTH_SHORT).show();
                        Log.e("DriverRegisterActivity", e.getMessage());
                    }
                });
            }

            private void addDriver(String driverid, String name, String phone, String email, String truck_number, String role) {
                    db_ref = FirebaseDatabase.getInstance().getReference();
                    Driver driver = new Driver(driverid,name,phone,email,truck_number,role);
                    db_ref.child("Driver").child(driverid).setValue(driver);
                    Toast.makeText(this, "added to the database", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RegisterActivity.this, MainActivity.class));

            }
    }