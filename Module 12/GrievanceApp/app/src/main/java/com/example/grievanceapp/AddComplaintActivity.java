package com.example.grievanceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grievanceapp.Model.Complaint;
import com.example.grievanceapp.Model.Users;
import com.example.grievanceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddComplaintActivity extends AppCompatActivity {
    private EditText mET;
    private List<Object> mList = new ArrayList<>();

    private static Button mCreate;
    static String comp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);
        mET = (EditText) findViewById(R.id.add_complaint);
        mCreate = (Button) findViewById(R.id.add_complaint_btn);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comp = mET.getText().toString();
                if (comp != null) {
                    final HashMap<String, String> hm = new HashMap<>();
                    hm.put(comp, "New Complaint");
                    Prevalent.currentUser.addComp(hm);
//                    if(Prevalent.currentUser.getComps() != null) {
//                        mList = Prevalent.currentUser.getComps();
//                    }
                    mList = Prevalent.currentUser.getComps();
                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getNumber());

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("comps").exists()) {
                                HashMap<String, Object> hm1 = new HashMap<>();
                                hm1.put("comps", mList);
                                db.updateChildren(hm1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddComplaintActivity.this, "Complaint has been filed", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AddComplaintActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(AddComplaintActivity.this, "Complaint has not been filed due to network error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
//                                for (DataSnapshot dsk : dataSnapshot.child("comp").child("0").getChildren()) {
//                                    HashMap<String, String> hm2 = new HashMap<>();
//                                    hm2.put(dsk.getKey().toString(), dsk.getValue().toString());
//                                    mList.add(hm2);
//                                }


//                                db.updateChildren(hm1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(AddComplaintActivity.this, "Complaint has been filed in here", Toast.LENGTH_LONG).show();
//                                        } else {
//                                            Toast.makeText(AddComplaintActivity.this, "Complaint has not been filed due to network error", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
                                Log.d("SVale", mList.toString());
                                db.child("comps").setValue(mList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddComplaintActivity.this, "Complaint has been filed in here", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AddComplaintActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(AddComplaintActivity.this, "Complaint has not been filed due to network error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    Toast.makeText(AddComplaintActivity.this, "No complaint entered", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
