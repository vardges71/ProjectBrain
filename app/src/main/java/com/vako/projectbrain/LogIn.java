package com.vako.projectbrain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText emailField, passwordField;
    Button loginButton, registerButton;
    TextView forgotButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initialize();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            Intent i = new Intent(LogIn.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {

            // User is signed out
            Log.d("TAG", "onAuthStateChanged: Signed_Out");
        }
    }

    private void initialize() {

        emailField = findViewById(R.id.emailET);
        passwordField = findViewById(R.id.passwordET);
        loginButton = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerBtn);
        forgotButton = findViewById(R.id.forgotBtn);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgotButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.loginBtn:
                getLogIn(view);
                break;
            case R.id.registerBtn:
                Intent intent = new Intent(LogIn.this, Register.class);
                startActivity(intent);
                break;
            case R.id.forgotBtn:
                forgotPassword(view);
                break;
        }
    }

    private void getLogIn(View view) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String password = passwordField.getText().toString().trim();

        if ((emailField.getText().toString().trim().matches(emailPattern) && emailField.length() > 0) && passwordField.length() > 6) {

            final String email = emailField.getText().toString().trim();

//          Authenticate the User in FireBase

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "LogIn is Success!!!", Toast.LENGTH_LONG).show();

                        Intent nextView = new Intent(LogIn.this, MainActivity.class);
                        startActivity(nextView);

                        emailField.setText(null);
                        passwordField.setText(null);
                    } else {

                        Toast.makeText(LogIn.this, "Error!!! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            });
        } else {

            Toast.makeText(LogIn.this, "Enter correct e-mail or password.", Toast.LENGTH_LONG).show();
        }
    }

    private void forgotPassword(View view) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ((emailField.getText().toString().trim().matches(emailPattern) && emailField.length() > 0)) {
            mAuth.sendPasswordResetEmail(emailField.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(LogIn.this,
                                        "Password reset email has been sent to you", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(LogIn.this, "Error!!! " +
                                        task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {

            Toast.makeText(getApplicationContext(), "Please enter valid e-mail", Toast.LENGTH_LONG).show();
        }
    }
}