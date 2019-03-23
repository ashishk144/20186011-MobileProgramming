package com.example.grievanceapp;


import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grievanceapp.Model.Users;
import com.example.grievanceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText inputNum, inputPW;
    private Button logInButton;
    private ProgressDialog loadingBar;
    private final String dbName = "Users";
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = (Button) findViewById(R.id.login_btn);

        inputNum = (EditText) findViewById(R.id.login_phone_number_input);
        inputPW = (EditText) findViewById(R.id.login_password_input);
        rememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        loadingBar = new ProgressDialog(this);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String number = inputNum.getText().toString();
        String password = inputPW.getText().toString();

        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait, while we are logging you.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            accessProvider(number, password);
        }
    }


    private void accessProvider(final String number, final String password) {

        if (rememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, number);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("No:74", "Inside AccessProvider");
                if (dataSnapshot.child(dbName).child(number).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(number).getValue(Users.class);
                    Log.d("Users", usersData.toString());
                    if (usersData.getNumber().equals(number)) {
                        System.out.println(usersData.getPassword() + "In LA");
                        if (usersData.getPassword().equals(password)) {
                            boolean val = dataSnapshot.child("Users").child(number).child("isAdmin").getValue(boolean.class);
                            usersData.setisAdmin(val);
                            Prevalent.currentUser = usersData;
                            Toast.makeText(LoginActivity.this, "You have successfully logged in", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "You have provided wrong password", Toast.LENGTH_LONG).show();
                        }

                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Your not registered with us.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
