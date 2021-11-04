package com.example.garbagecollectingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private View view;
    private EditText txtname, txtphone, txtemail, txtaddress, txtdistrict;
    private Button btnsignout, btnedit;
    private FirebaseAuth authentication;
    private FirebaseUser user;
    private ProgressDialog pd;
    private DatabaseReference dbRef;
    private final String TAG = "profile Fragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnsignout = view.findViewById(R.id.btnsignout);
        txtname = view.findViewById(R.id.pro_name);
        txtphone = view.findViewById(R.id.pro_phone);
        txtemail = view.findViewById(R.id.pro_Email);
        txtaddress = view.findViewById(R.id.pro_address);
        txtdistrict = view.findViewById(R.id.pro_district);
        btnedit = view.findViewById(R.id.pro_btnedit);
        authentication = FirebaseAuth.getInstance();
        user = authentication.getCurrentUser();
        pd = new ProgressDialog(getContext());
        dbRef = FirebaseDatabase.getInstance().getReference();
        loadProfile(user);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String label = btnedit.getText().toString();
                if (label.equals("Edit Profile")) {
                    CTRL_MGT(true);
                    btnedit.setText("SAVE PROFILE");
                } else if (label.equals("SAVE PROFILE")) {
                    saveProfile(user);

                }

            }
        });
        return view;
    }

            private void saveProfile(FirebaseUser user) {
                String name = txtname.getText().toString();
                String email = txtemail.getText().toString();
                String phone = txtphone.getText().toString();
                String address = txtaddress.getText().toString();
                String district = txtdistrict.getText().toString();
                String uid = user.getUid();

                if (name.equals("") || email.equals("") || phone.equals("") || address.equals("")|| district.equals("")) {
                    Toast.makeText(getContext(), "fill the text field ", Toast.LENGTH_SHORT).show();
                } else {
                    User user1 = new User();
                    user1.setUserid(uid);
                    user1.setUsername(name);
                    user1.setUseremail(email);
                    user1.setUserphone(phone);
                    user1.setUseraddress(address);
                    user1.setUserdistrict(district);

                    dbRef.child("User").child(uid).setValue(user1, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                CTRL_MGT(false);
                                btnedit.setText("EDIT PROFILE");
                                Toast.makeText(getContext(), "successfully saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Errorr", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "record Update error" + error.getMessage());


                            }
                        }

                    });
                }


    }




    private void loadProfile(FirebaseUser user) {
        Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();
        pd.setMessage("Loading");
        pd.show();
        String uid1 = user.getUid();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.child("User").child(uid1).getValue(User.class);
                txtname.setText(user1.getUsername());
                txtphone.setText(user1.getUserphone());
                txtemail.setText(user1.getUseremail());
                txtaddress.setText(user1.getUseraddress());
                txtdistrict.setText(user1.getUserdistrict());
                CTRL_MGT(false);
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DB Error:" + error.getMessage().toString());

            }
        });
    }


    private void CTRL_MGT(boolean status) {
        txtname.setEnabled(status);
        txtphone.setEnabled(status);
        txtemail.setEnabled(status);
        txtaddress.setEnabled(status);
        txtdistrict.setEnabled(status);
    }
    }