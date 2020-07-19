package com.vako.projectbrain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener, ValueEventListener, ChildEventListener {

    User userClass;

//    FirebaseAuth mAuth;

//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("users");
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            id = mAuth.getUid();
            email = mAuth.getCurrentUser().getEmail();
            emailField.setText("email:" + "\t" + email);

        }
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

        readUserData();
    }


    // ************************************* Menu part ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);

        MenuItem userButton = menu.findItem(R.id.userMenu);
        userButton.setVisible(false);

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

    private void userSave(View view) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        String currentEmail = mAuth.getCurrentUser().getEmail();
        String userName = userNameET.getText().toString();
        String firstname = firstNameET.getText().toString();
        String lastname = lastNameET.getText().toString();
        String location = locationET.getText().toString();

//        userClass = new User(currentEmail, userName, firstname, lastname, location);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        myRef.child(userID).child("email").setValue(currentEmail);
        myRef.child(userID).child("userName").setValue(userName);
        myRef.child(userID).child("firstName").setValue(firstname);
        myRef.child(userID).child("lastName").setValue(lastname);
        myRef.child(userID).child("location").setValue(location);

        userNameET.setEnabled(false);
        firstNameET.setEnabled(false);
        lastNameET.setEnabled(false);
        locationET.setEnabled(false);

        Toast.makeText(this, "Your credentials success added", Toast.LENGTH_LONG).show();
    }

    public void readUserData() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("userName").getValue().toString();
                String userFirstName = snapshot.child("firstName").getValue().toString();
                String userLastName = snapshot.child("lastName").getValue().toString();
                String userLocation = snapshot.child("location").getValue().toString();
                userNameET.setText("username:" + "\t" + userName);
                firstNameET.setText("first name:" + "\t" + userFirstName);
                lastNameET.setText("last name:" + "\t" + userLastName);
                locationET.setText("location :   " + "\t" + userLocation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteUser() {
//
//        String userID = mAuth.getCurrentUser().getUid();
//
//
//        myRef.child(userID).removeValue();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        userClass = snapshot.getValue(User.class);
        Toast.makeText(this, userClass.toString(), Toast.LENGTH_LONG).show();

        Log.d("\n\nFIREBASE", userClass.toString());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

//        String userID = mAuth.getCurrentUser().getUid();
//
//        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
//
//        String userName = snapshot.child("userName").getValue().toString();
//        String userFirstName = snapshot.child("firstName").getValue().toString();
//        String userLastName = snapshot.child("lastName").getValue().toString();
//        String userLocation = snapshot.child("location").getValue().toString();
//
//        userNameET.setText("username:" + "\t" + userName);
//        firstNameET.setText("first name:" + "\t" + userFirstName);
//        lastNameET.setText("last name:" + "\t" + userLastName);
//        locationET.setText("location :   " + "\t" + userLocation);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}