package com.vako.projectbrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SingleUserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference myRef;

    private List<Idea> ideaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IdeasListAdapter lAdapter;

    TextView singleUserName, singleUserFullName, singleUserLocation, singleUserIdeasNumberResult;

    private String id;
    private String email;
    private String userName;
    private String firstName = "name";
    private String lastName = "surname";
    private String location = "location";
    private String idea = "bla, bla-bla";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);

        initialize();
    }

    public void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.ideasList);
        lAdapter = new IdeasListAdapter(ideaList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        singleUserName = findViewById(R.id.singleUserName);
        singleUserFullName = findViewById(R.id.singleUserFullName);
        singleUserLocation = findViewById(R.id.singleUserLocation);
        singleUserIdeasNumberResult = findViewById(R.id.singleUserIdeasNumberResult);

        recyclerView.setAdapter(lAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                Idea idea = ideaList.get(position);
                Intent intent = new Intent(SingleUserActivity.this, SingleUserActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getUser();
    }

    // ************************************** Action Bar *******************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);

        MenuItem mainButton = menu.findItem(R.id.mainActtivity);
        mainButton.setVisible(true);

        MenuItem logoutBtn = menu.findItem(R.id.logout_icon);
        logoutBtn.setVisible(false);

        MenuItem editButton = menu.findItem(R.id.editUser);
        editButton.setVisible(false);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search_icon).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

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
            case R.id.goToIdea:
                Intent toWriteIdea = new Intent(this, WriteIdeaActivity.class);
                startActivity(toWriteIdea);
                return true;
            case R.id.logout_icon:
                logOut();
                return true;
//            case R.id.search_icon:
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    // *********************************************************************************************

    private void getUser() {


        Bundle extras = getIntent().getExtras();
        singleUserName.setText(extras.getString("userName"));
        singleUserFullName.setText(extras.getString("userFirstName") + " " + extras.getString("userLastName"));
        singleUserLocation.setText(extras.getString("userLocation"));

//        final String userID = FirebaseAuth.getInstance().getUid();
//
//        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
//
////        final User user = new User(userName, firstName, lastName, location);
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.child("userName").exists()) {
//                        user.setUserName(dataSnapshot.child("userName").getValue().toString());
//                        singleUserName.setText(user.getUserName());
//                        System.out.println("USER DETAIS: " + user.getUserName());
//                    }
//                    if (dataSnapshot.child("firstName").exists()) {
//                        user.setFirstName(dataSnapshot.child("firstName").getValue().toString());
//                    }
//                    if (dataSnapshot.child("lastName").exists()) {
//                        user.setLastName(dataSnapshot.child("lastName").getValue().toString());
//                    }
//                    if (dataSnapshot.child("location").exists()) {
//                        user.setLocation(dataSnapshot.child("location").getValue().toString());
//                        singleUserLocation.setText(user.getLocation());
//                    }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        Log.d("ATENNTION", "USER DETAIS: " + singleUserFullName);
    }
}