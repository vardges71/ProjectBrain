package com.vako.projectbrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    User userClass;

    FirebaseAuth mAuth;
    DatabaseReference myRef;

    private static final String TAG = "ProfileActivity";

    TextView emailField;
    EditText userNameET, firstNameET, lastNameET, locationET;
    Button saveBtn, deleteBtn;

    private String id, email, userName, firstName, lastName, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            id = mAuth.getUid();
            email = mAuth.getCurrentUser().getEmail();
            emailField.setText("email: " + "\t" + email);
        }

        if (firstNameET == null && lastNameET == null && locationET == null ) {

            Toast.makeText(Profile.this, "Please enter your credentials", Toast.LENGTH_LONG).show();
        }

        loadUserData();
    }

    private void initialize() {

        emailField = findViewById(R.id.tvUserEmail);

        userNameET = findViewById(R.id.etUserName);
        userNameET.setEnabled(false);
        firstNameET = findViewById(R.id.etFirstName);
        firstNameET.setEnabled(false);
        lastNameET = findViewById(R.id.etLastName);
        lastNameET.setEnabled(false);
        locationET = findViewById(R.id.etLocation);
        locationET.setEnabled(false);

        saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(this);
        deleteBtn = findViewById(R.id.btnDelete);
        deleteBtn.setOnClickListener(this);

    }

    // ************************************* Menu part ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);

        MenuItem userButton = menu.findItem(R.id.userMenu);
        userButton.setVisible(false);

        MenuItem searchButton = menu.findItem(R.id.search_icon);
        searchButton.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mainActtivity:
                Intent toMain = new Intent(this, MainActivity.class);
                startActivity(toMain);
                return true;
            case R.id.userMenu:
                Intent toProfile = new Intent(this, Profile.class);
                startActivity(toProfile);
                return true;
            case R.id.logout_icon:
                logOut();
                return true;
            case R.id.search_icon:
                searchUsers();
                return true;
            case R.id.editUser:
                userNameET.setEnabled(true);
                firstNameET.setEnabled(true);
                lastNameET.setEnabled(true);
                locationET.setEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ********************************************************************************************

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    private void searchUsers() {


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.btnSave:
                userSave(view);
                break;
            case R.id.btnDelete:
                deleteUser();
                break;
        }
    }

// *************************************************************************************************

    private void userSave(final View view) {

        String userID = mAuth.getCurrentUser().getUid();

        String currentEmail = mAuth.getCurrentUser().getEmail();
        String userName = userNameET.getText().toString();
        String firstname = firstNameET.getText().toString();
        String lastname = lastNameET.getText().toString();
        String location = locationET.getText().toString();

        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        myRef.child("email").setValue(currentEmail);
        myRef.child("userName").setValue(userName);
        myRef.child("firstName").setValue(firstname);
        myRef.child("lastName").setValue(lastname);
        myRef.child("location").setValue(location);

        userNameET.setEnabled(false);
        firstNameET.setEnabled(false);
        lastNameET.setEnabled(false);
        locationET.setEnabled(false);

        Toast.makeText(this, "Your credentials success added", Toast.LENGTH_LONG).show();
    }



    public void loadUserData() {

        String userID = FirebaseAuth.getInstance().getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("userName").exists()) {
                    String userName = snapshot.child("userName").getValue().toString();
                    userNameET.setText(userName);
                }
                if(snapshot.child("firstName").exists()) {
                    String userFirstName = snapshot.child("firstName").getValue().toString();
                    firstNameET.setText(userFirstName);
                }
                if(snapshot.child("lastName").exists()) {
                    String userLastName = snapshot.child("lastName").getValue().toString();
                    lastNameET.setText(userLastName);
                }
                if(snapshot.child("location").exists()) {
                    String userLocation = snapshot.child("location").getValue().toString();
                    locationET.setText(userLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            Intent backToLogIn = new Intent(Profile.this, LogIn.class);
                            startActivity(backToLogIn);
                        }
                    }
                });

        myRef = FirebaseDatabase.getInstance().getReference();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child("users").child(userID).removeValue();

        Toast.makeText(Profile.this, "Account successfuly deleted", Toast.LENGTH_LONG).show();
    }
}