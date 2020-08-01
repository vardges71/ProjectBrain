package com.vako.projectbrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";

    FirebaseAuth mAuth;
    DatabaseReference myRef;

    EditText searchField;
    ImageButton searchBtn;

    private String userId;
    private String email;
    private String userName;
    private String firstName = "name";
    private String lastName = "surname";
    private String location = "location";
    private String ideaTitle = "";
    private String ideaContext = "";
    private String ideaCount = "";

    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserListAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialize();
    }

    public void initialize() {

        searchField = findViewById(R.id.searchField);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.searchList);
        sAdapter = new UserListAdapter(userList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(sAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                User clickedUser = userList.get(position);

                Intent intent = new Intent(SearchActivity.this, SingleUserActivity.class);

                intent.putExtra("clickedUserID", clickedUser.getUserID());
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

        MenuItem searchButton = menu.findItem(R.id.search_icon);
        searchButton.setVisible(true);

//        // Associate searchable configuration with the SearchView
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

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.searchBtn:
                searchUsers();
                break;
        }
    }

    // ********************************* Search Users **********************************************

    private void searchUsers() {

        final String userID = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference iRef = ref.child("ideas");

        final String query = searchField.getText().toString();

        if (query.isEmpty()) {

            Toast.makeText(SearchActivity.this, "please fill search field", Toast.LENGTH_LONG).show();

        } else {

            final User user = new User(userId, userName, firstName, lastName, location, ideaTitle, ideaContext, ideaCount);

            String firstLetterCapital = query.substring(0, 1).toUpperCase() + query.substring(1);
            Query eventSearchQuery = ref.orderByChild("userName").startAt(firstLetterCapital).endAt(firstLetterCapital + "uf8ff");
            Query ideaSearchQuery = iRef.orderByChild("ideaTitle").startAt(firstLetterCapital).endAt(firstLetterCapital + "uf8ff");

            eventSearchQuery.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        if (userSnapshot.exists()) {

                            if (userSnapshot.child("id").exists()) {
                                user.setUserID(userSnapshot.child("id").getValue().toString());
                            }
                            if (userSnapshot.child("userName").exists()) {
                                user.setUserName(userSnapshot.child("userName").getValue().toString());
                            }
                            if (userSnapshot.child("firstName").exists()) {
                                user.setFirstName(userSnapshot.child("firstName").getValue().toString());
                            }
                            if (userSnapshot.child("lastName").exists()) {
                                user.setLastName(userSnapshot.child("lastName").getValue().toString());
                            }
                            if (userSnapshot.child("location").exists()) {
                                user.setLocation(userSnapshot.child("location").getValue().toString());
                            }
                            if (userSnapshot.child("ideas").exists()) {

                                long ideaCount = userSnapshot.child("ideas").getChildrenCount();
                                user.setIdeaCount(String.valueOf(ideaCount));

                                for (DataSnapshot snap : userSnapshot.child("ideas").getChildren()) {

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

                            Log.d(TAG, userSnapshot.child("firstName").getValue().toString() + " " + userSnapshot.child("lastName").getValue().toString() + " from " + userSnapshot.child("location").getValue().toString());
                            userList.clear();
                            userList.add(user);
                            sAdapter.notifyDataSetChanged();

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
}