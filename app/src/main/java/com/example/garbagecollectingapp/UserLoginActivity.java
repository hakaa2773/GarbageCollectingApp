package com.example.garbagecollectingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UserLoginActivity extends AppCompatActivity {
    private EditText txtemail, txtpassword;
    private TextView tvlink;
    private Button btnlogin,btncancel;
    private FirebaseAuth authentication;
    private DatabaseReference db_ref;
    String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        txtemail = findViewById(R.id.txtemailu);
        txtpassword = findViewById(R.id.txtpasswordu);
        tvlink =findViewById(R.id.tvlinku);
        btnlogin =findViewById(R.id.btnloginu);
        btncancel =findViewById(R.id.btncacel);
        authentication = FirebaseAuth.getInstance();
        tvlink = findViewById(R.id.tvlinku);
        db_ref = FirebaseDatabase.getInstance().getReference().child("User");

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int_reg = new Intent(UserLoginActivity.this,StartActivity.class);
                startActivity(int_reg);
                finish();

            }
        });

        tvlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int_reg = new Intent(UserLoginActivity.this,RegisterActivity.class);
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

                if (email.isEmpty()|| password.isEmpty()){
                    Toast.makeText(UserLoginActivity.this,"enter the password and email",Toast.LENGTH_LONG).show();

                }
                else{
                    validateUser(email,password);
                }

            }

            private void validateUser(String email, String password) {
                authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        u_id = authentication.getCurrentUser().getUid();
                        if (u_id != null) {
                            db_ref.orderByChild("userid").equalTo(u_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String role = snapshot.child(u_id).child("role").getValue().toString();
                                        if (role.equals("user")){
                                            startActivity(new Intent(UserLoginActivity.this,MainActivity.class));
                                        }
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(UserLoginActivity.this, "invalid username or password please try again", Toast.LENGTH_SHORT).show();


                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserLoginActivity.this, "invalid username or password please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}