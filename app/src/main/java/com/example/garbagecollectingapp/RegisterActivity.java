package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity  {
    private EditText txtname, txtphone, txtemail, txtpassword;
    private Button btnregister;
    private TextView tvregister;
    private String role = "user";
    FirebaseAuth authentication;
    DatabaseReference db_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtname = findViewById(R.id.txtName);
        txtphone = findViewById(R.id.txtphone);
        txtemail= findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        btnregister = findViewById(R.id.btnregister);
        tvregister =findViewById(R.id.tvregister);
        authentication = FirebaseAuth.getInstance();


        db_ref = FirebaseDatabase.getInstance().getReference();
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,phone,email,password;
                name = txtname.getText().toString();
                phone = txtphone.getText().toString();
                email = txtemail.getText().toString();
                password = txtpassword.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "please fill all the text fields", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(email, password, name, phone);
                }
            }
        });

    }

    private void createUser(String email, String password, String name, String phone) {
        authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("RegisterActivity", "successfully registered");
                    String userid =authentication.getCurrentUser().getUid();
                    addUser(userid,name,phone,email,role);
                }else {
                    Toast.makeText(RegisterActivity.this, "registration failure", Toast.LENGTH_SHORT).show();
                    Exception e = task.getException();
                    Log.e("RegisterActivity", e.getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Registration faill try again", Toast.LENGTH_SHORT).show();
                Log.e("RegisterActivity", e.getMessage());
            }
        });
    }

    private void addUser(String userid, String name, String phone, String email, String role) {
        db_ref = FirebaseDatabase.getInstance().getReference();
        User user = new User(userid, name,phone,email,role);
        db_ref.child("User").child(userid).setValue(user);
        Toast.makeText(this, "added to the database", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }


}