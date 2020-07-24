package com.vako.projectbrain;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    User userClass;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    private String id;
    private  String email;
    private String userName;
    private String firstName = "first name";
    private String lastName = "last name";
    private  String location = "location";

    ListView listView;
    ArrayList<User> userList;
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void initialize() {

        listView = findViewById(R.id.mainList);
        userList = new ArrayList<User>();

        userListAdapter = new UserListAdapter(this, userList);
        listView.setAdapter(userListAdapter);

        getUsers();
    }

    // ************************************** Action Bar *******************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);

        MenuItem mainButton = menu.findItem(R.id.mainActtivity);
        mainButton.setVisible(false);

        MenuItem logoutBtn = menu.findItem(R.id.logout_icon);
        logoutBtn.setVisible(false);

        MenuItem editButton = menu.findItem(R.id.editUser);
        editButton.setVisible(false);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // *****************************
    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    private void getUsers() {

        final String userID = FirebaseAuth.getInstance().getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = new User(userName, firstName, lastName, location);

                    if (snapshot.child("userName").exists()) {
                        user.setUserName(snapshot.child("userName").getValue().toString());
                    }
                    if (snapshot.child("firstName").exists()) {
                        user.setFirstName(snapshot.child("firstName").getValue().toString());
                    }
                    if (snapshot.child("lastName").exists()) {
                        user.setLastName(snapshot.child("lastName").getValue().toString());
                    }
                    if (snapshot.child("location").exists()) {
                        user.setLocation(snapshot.child("location").getValue().toString());
                    }

                    userList.add(user);
                    userListAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers() {

    }
}