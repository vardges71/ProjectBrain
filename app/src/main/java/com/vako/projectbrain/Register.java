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

    EditText userEmailField, passwordField, rePasswordField;
    Button registerButton, cancelButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {

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

                    Intent nextView = new Intent(Register.this, MainActivity.class);
                    startActivity(nextView);

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

        if ((passwordField.getText().toString().equals(rePasswordField.getText().toString())) && ((userEmailField.getText().toString().trim().matches(emailPattern) && userEmailField.length() > 0)) && passwordField.length() > 6) {

            String checkedPassw = passwordField.getText().toString().trim();

            userPassw = checkedPassw;

            final String email = userEmailField.getText().toString().trim();
            String password = userPassw;

//          Register the User in FireBase

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "Registration Success!!!", Toast.LENGTH_LONG).show();

                        Intent nextView = new Intent(Register.this, MainActivity.class);
                        startActivity(nextView);

                        userEmailField.setText(null); passwordField.setText(null); rePasswordField.setText(null);

                    } else {

                        Toast.makeText(Register.this, "Error!!! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {

            Toast.makeText(getApplicationContext(), "Please enter valid credentials", Toast.LENGTH_LONG).show();
        }
    }
}