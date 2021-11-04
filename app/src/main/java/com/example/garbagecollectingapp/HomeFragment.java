package com.example.garbagecollectingapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //private View view;
    private TextView txttype;
    private ImageView imageView;
    private Spinner spi_day;
    private ArrayList<String> list_type;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private DatabaseReference dbref,dbref1;
    private String imageUri = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        txttype = view.findViewById(R.id.item_type);
        imageView = view.findViewById(R.id.item_image);
        spi_day = view.findViewById(R.id.item_day);
        list_type = new ArrayList<>();
        list_type.add(0, "Select A Day");
        list_type.add(1, "Monday");
        list_type.add(2, "Tuesday");
        list_type.add(3, "Wednesday");
        list_type.add(4, "Thursday");
        list_type.add(5, "Friday");
        list_type.add(6, "Saturday");
        list_type.add(7, "Sunday");

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list_type);
        spi_day.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spi_day.setOnItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());
        dbref = FirebaseDatabase.getInstance().getReference().child("Collect");
        dbref1 = FirebaseDatabase.getInstance().getReference().child("Collecting");

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItemPosition()!=0){
            String day = adapterView.getSelectedItem().toString();
            dbref.child("Day").child(day).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue()!=null){
                        txttype.setText(snapshot.getValue().toString());


                      dbref1.child("Day").child(day).addValueEventListener(new ValueEventListener() {
                           @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                               //Collecting collecting = new Collecting();
                               imageUri = snapshot.getValue(String.class);
                               //Toast.makeText(getContext(),imageUri.toString(), Toast.LENGTH_SHORT).show();
                               //System.out.println(imageUri);
                               Picasso.get().load(imageUri).into(imageView);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                       });
                    }
                    else {
                        txttype.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}