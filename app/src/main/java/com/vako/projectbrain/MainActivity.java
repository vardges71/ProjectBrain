package com.vako.projectbrain;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    private String userId;
    private String email;
    private String userName;
    private String firstName = "name";
    private String lastName = "surname";
    private String location = "location";

    private String ideaID = "";
    private String ideaTitle = "";
    private String ideaContext = "";
    private String ideaCount = "";

    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.mainList);
        mAdapter = new UserListAdapter(userList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                User clickedUser = userList.get(position);

                Intent intent = new Intent(MainActivity.this, SingleUserActivity.class);

                intent.putExtra("clickedUserID", clickedUser.getUserID());
                intent.putExtra("userId", clickedUser.getUserID());
                intent.putExtra("userName", clickedUser.getUserName());
                intent.putExtra("userFirstName", clickedUser.getFirstName());
                intent.putExtra("userLastName", clickedUser.getLastName());
                intent.putExtra("userLocation", clickedUser.getLocation());
                intent.putExtra("ideaCount", clickedUser.getIdeaCount());
                intent.putExtra("ideaTitle", clickedUser.getIdeaTitle());
                intent.putExtra("ideaContext", clickedUser.getIdeaContext());
                intent.putExtra("ideaContent", clickedUser.getIdeaContent());
                intent.putExtra("ideaModDate", clickedUser.getCreatedDate());

                startActivity(intent);

//                Log.d(TAG, "USER CRED " + clickedUser.getIdea());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getUsers();
        searchUsers();
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
            case R.id.goToIdea:
                Intent toWriteIdea = new Intent(this, WriteIdeaActivity.class);
                startActivity(toWriteIdea);
                return true;
            case R.id.search_icon:
                Intent toSearch = new Intent(this, SearchActivity.class);
                startActivity(toSearch);
                return true;
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

    private void getUsers() {

//        final String userID = FirebaseAuth.getInstance().getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = new User(userId, userName, firstName, lastName, location, ideaID, ideaTitle, ideaContext, ideaCount);

                    if (snapshot.child("id").exists()) {
                        user.setUserID(snapshot.child("id").getValue().toString());
                    }
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
                    if (snapshot.child("ideas").exists()) {

                        long ideaCount = snapshot.child("ideas").getChildrenCount();
                        user.setIdeaCount(String.valueOf(ideaCount));

                        for (DataSnapshot snap : snapshot.child("ideas").getChildren()) {

                            if (snap.child("ideaTitle").exists()) {
                                user.setIdeaTitle(snap.child("ideaTitle").getValue().toString());
                            }
                            if (snap.child("ideaContext").exists()) {
                                user.setIdeaContext(snap.child("ideaContext").getValue().toString());
                            }
                            if (snap.child("ideaContent").exists()) {
                                user.setIdeaContent(snap.child("ideaContent").getValue().toString());
                            }
                            if (snap.child("modifiedDate").exists()) {
                                user.setCreatedDate(snap.child("modifiedDate").getValue().toString());
                            }
                        }
                    }

                    userList.add(user);
                    mAdapter.notifyDataSetChanged();

//                    Log.d(TAG, "IDEA TITLE: " + user.getIdeaTitle());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // ********************************* Search Users **********************************************

    private void searchUsers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//
        String query = "a";
        String firstLetterCapital = query.substring(0, 1).toUpperCase() + query.substring(1);
        Query eventSearchQuery = ref.orderByChild("userName").startAt(firstLetterCapital).endAt(firstLetterCapital + "uf8ff");
//        equalTo("Bibi")
        eventSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    System.out.println(userSnapshot.getKey());
                    System.out.println(userSnapshot.child("userName").getValue(String.class));
                    if (userSnapshot.child("firstName").exists() && userSnapshot.child("lastName").exists()) {
                        Log.d(TAG, userSnapshot.child("firstName").getValue().toString() + " " + userSnapshot.child("lastName").getValue().toString() + " from " + userSnapshot.child("location").getValue().toString());
                    } else {
                        Log.d("ATTENTION", "User credentials is not exist");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}