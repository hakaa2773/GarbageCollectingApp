package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity  {
    private EditText txtemail, txtpassword;
    private Button btnlogin,btncancel;
    private FirebaseAuth authentication;
    private DatabaseReference db_ref;
    String d_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        btnlogin =findViewById(R.id.btnlogin);
        btncancel =findViewById(R.id.btncacel);
        authentication = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference().child("Driver");

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int_reg = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(int_reg);
                finish();

            }
        });




        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email = txtemail.getText().toString();
                password = txtpassword.getText().toString();

                if (email.isEmpty()|| password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "enter the password and email", Toast.LENGTH_LONG).show();
                }
                else {
                    validateuser(email,password);
                }
            }


        });
    }

    private void validateuser(String email, String password) {
       authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
               d_id = authentication.getCurrentUser().getUid();
               if(email.equals("admin@gmail.com")){
                   startActivity(new Intent(LoginActivity.this,AdminHomeActivity  .class));
               }
               else if (d_id != null) {
                   db_ref.orderByChild("driverid").equalTo(d_id).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()) {
                               String role = snapshot.child(d_id).child("role").getValue().toString();
                               if (role.equals("driver")){
                                   startActivity(new Intent(LoginActivity.this,DriverActivity.class));
                               }
                           }

                       }


                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(LoginActivity.this, "invalid username or password please try again", Toast.LENGTH_SHORT).show();


                       }
                   });

               }


           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(LoginActivity.this, "invalid username or password please try again", Toast.LENGTH_SHORT).show();

           }
       });
    }


}