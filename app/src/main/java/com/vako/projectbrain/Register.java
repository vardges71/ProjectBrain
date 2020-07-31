package com.vako.projectbrain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText userNameField, userEmailField, passwordField, rePasswordField;
    Button registerButton, cancelButton;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {

        userNameField = findViewById(R.id.registerUserName);
        userEmailField = findViewById(R.id.registerEmail);
        passwordField = findViewById(R.id.regPasswField);
        rePasswordField = findViewById(R.id.reRegPasswField);

        registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.btnRegister:

                if (mAuth.getCurrentUser() != null) {

                    finish();
                } else {

                    getRegister(view);
                }
                break;
            case R.id.btnCancel:
                Intent backToLogIn = new Intent(this, LogIn.class);
                startActivity(backToLogIn);
        }
    }

    // **************************************** Register *******************************************

    public void getRegister(View view) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String userPassw;

        if ((passwordField.getText().toString().equals(rePasswordField.getText().toString())) &&
                ((userEmailField.getText().toString().trim().matches(emailPattern) &&
                        userEmailField.length() > 0)) && passwordField.length() > 6) {

            String checkedPassw = passwordField.getText().toString().trim();

            userPassw = checkedPassw;

            final String email = userEmailField.getText().toString().trim();
            String password = userPassw;

//          Register the User in FireBase

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        userSave();
                        Toast.makeText(getApplicationContext(), "Registration Success!!!", Toast.LENGTH_LONG).show();

                        Intent nextView = new Intent(Register.this, Profile.class);
                        startActivity(nextView);

                        userNameField.setText(""); userEmailField.setText(""); passwordField.setText(""); rePasswordField.setText("");

                    } else {

                        Toast.makeText(Register.this, "Error!!! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {

            Toast.makeText(getApplicationContext(), "Please enter valid credentials", Toast.LENGTH_LONG).show();
        }

    }

    private void userSave() {

        String userID = mAuth.getCurrentUser().getUid();

        String currentEmail = mAuth.getCurrentUser().getEmail();
        String userName = userNameField.getText().toString();

        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        myRef.child("id").setValue(userID);
        myRef.child("email").setValue(currentEmail);
        myRef.child("userName").setValue(userName);

        Toast.makeText(this, "Your credentials success added", Toast.LENGTH_LONG).show();
    }

}