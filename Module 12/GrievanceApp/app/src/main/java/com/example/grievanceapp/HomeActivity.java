package com.example.grievanceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.grievanceapp.Model.Complaint;
import com.example.grievanceapp.Model.Users;
import com.example.grievanceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private List<Object> mComplaints = new ArrayList<>();
    private List<Object> mStatus = new ArrayList<>();
    private List<String> mString = new ArrayList<>();

    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(Prevalent.currentUser.isAdmin()) {
            Log.d("In Home", "Admin");
            loadComplaints();
            fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            fab.hide();
        } else {
            loadComplaintsUser();
        }
    }

    private void loadComplaintsUser() {
        for(Object o : Prevalent.currentUser.getComps()) {
            HashMap<String, String> hash= (HashMap) o;
            for(String key: hash.keySet()) {
                Log.d("KEYS ra reyy", key);
                mComplaints.add(key);
                mStatus.add(hash.get(key));
            }
        }
        initRecyclerView();
    }


    private void loadComplaints() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    for(DataSnapshot dsk: ds.child("comps").getChildren()) {
                        for(DataSnapshot dskk: dsk.getChildren()) {
                            String str = ds.getKey() + "$" + dsk.getKey() + "$" + dskk.getKey();
//                            String[] str1 = str.split("\\$");
//                            Log.d("Paths", str1[0]);
                            mString.add(str);
                            Prevalent.adminList = mString;
                            Prevalent.currentUser.addComp(dsk.getValue());
                            mComplaints.add(dskk.getKey());
                            mStatus.add(dskk.getValue());
                        }
                    }

                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_products);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mComplaints, mStatus, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void logOut(View view) {
        Prevalent.currentUser = new Users();
        Paper.book().write(Prevalent.UserPhoneKey, "UserPhone");
        Paper.book().write(Prevalent.UserPasswordKey, "UserPassword");
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void newComplaint(View view) {
        Intent intent = new Intent(HomeActivity.this, AddComplaintActivity.class);
        startActivity(intent);
    }
}
