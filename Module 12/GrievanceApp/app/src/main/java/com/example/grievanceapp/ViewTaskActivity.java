package com.example.grievanceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ViewTaskActivity extends AppCompatActivity {
    private EditText eComplaint;
    private EditText eStatus;
    private Button eDelete;
    private Button eUpdate;
    private int ind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        eComplaint = (EditText) findViewById(R.id.view_task_description);
        eStatus = (EditText) findViewById(R.id.view_task_status);
        eDelete = (Button) findViewById(R.id.view_task_delete);
        eUpdate = (Button) findViewById(R.id.update_status);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("complaints");
        ind = id;
        if(Prevalent.currentUser.isAdmin()) {
            eComplaint.setEnabled(false);
            eDelete.setVisibility(View.INVISIBLE);
        } else {
            eStatus.setEnabled(false);
            eUpdate.setText("Update\nComplaint");
        }
        HashMap<String, String> hm = (HashMap<String, String>) Prevalent.currentUser.getComp(id);
        for(String key : hm.keySet()) {
            eComplaint.setText(key);
            eStatus.setText(hm.get(key));
        }
    }

    public void delete(View view) {
        Prevalent.currentUser.deleteComp(ind);
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentUser.getNumber());
        if(Prevalent.currentUser.getComps() == null) {
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    db.child("comps").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewTaskActivity.this, "Complaint has been removed in here", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ViewTaskActivity.this, "Complaint has not been removed due to network error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            db.child("comps").setValue(Prevalent.currentUser.getComps())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewTaskActivity.this, "Complaint has been removed", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ViewTaskActivity.this, "Complaint has not been removed due to network error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    public void update(View view) {
        if (Prevalent.currentUser.isAdmin()) {
            final String updated_status = eStatus.getText().toString();
            if (updated_status != null) {
                String str = Prevalent.adminList.get(ind);
                final String[] path = str.split("\\$");
                System.out.print(path.toString());
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rootRef.child(path[0]).child("comps").child(path[1]).child(path[2])
                                .setValue(updated_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ViewTaskActivity.this, "Status has been updated", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ViewTaskActivity.this, HomeActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(ViewTaskActivity.this, "Status has not been updated due to network error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                Toast.makeText(this, "Please update status", Toast.LENGTH_SHORT).show();
            }
        } else {
            String status = eStatus.getText().toString();
            if(status.equals("New Complaint")) {
                String newcomp = eComplaint.getText().toString();
                if(newcomp != null && !newcomp.equals("")) {
                    Prevalent.currentUser.deleteComp(ind);
                    final HashMap<String, String> hm = new HashMap<>();
                    hm.put(newcomp, "New Complaint");
                    Prevalent.currentUser.addComp(hm);
                    List<Object> obj = Prevalent.currentUser.getComps();
                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getNumber());
                    db.child("comps").setValue(obj)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ViewTaskActivity.this, "Complaint has been updated", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ViewTaskActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ViewTaskActivity.this, "Complaint has not been updated due to network error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(this, "Complaint cant be empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cannot edit as the complaint is being pursued", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
