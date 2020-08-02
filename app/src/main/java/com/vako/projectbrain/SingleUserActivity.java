package com.vako.projectbrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class SingleUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SingleUserActivity";
    FirebaseAuth mAuth;
    DatabaseReference reference;

    private List<User> ideaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IdeasListAdapter lAdapter;

    private int buttonState = 1;

    TextView singleUserName, singleUserFullName, singleUserLocation, singleUserIdeasNumberResult;
    ImageButton followButton;

    String userID;
    private String email;
    private String userName;
    private String firstName = "name";
    private String lastName = "surname";
    private String location = "location";

    private String ideaID = "";
    private String ideaTitle = "";
    private String ideaContext = "";
    private String ideaCount = "";
    private String modifiedDate;


    private User idea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);

        checkIfFavorite();
        initialize();
    }

    public void initialize() {

        recyclerView = findViewById(R.id.ideasList);
        lAdapter = new IdeasListAdapter(ideaList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        singleUserName = findViewById(R.id.singleUserName);
        singleUserFullName = findViewById(R.id.singleUserFullName);
        singleUserLocation = findViewById(R.id.singleUserLocation);
        singleUserIdeasNumberResult = findViewById(R.id.singleUserIdeasNumberResult);

        followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(this);

        recyclerView.setAdapter(lAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                final Bundle extras = getIntent().getExtras();
                String userFullName = (extras.getString("userFirstName")) + " " + (extras.getString("userLastName"));
                String clickedUserID = extras.getString("clickedUserID");

                User clickedIdea = ideaList.get(position);
                Intent intent = new Intent(SingleUserActivity.this, SingleIdeaActivity.class);

                intent.putExtra("clickedUserID", clickedUserID);
                intent.putExtra("userFullName", userFullName);
                intent.putExtra("ideaCount", clickedIdea.getIdeaCount());
                intent.putExtra("ideaID", clickedIdea.getIdeaID());
                intent.putExtra("ideaTitle", clickedIdea.getIdeaTitle());
                intent.putExtra("ideaContext", clickedIdea.getIdeaContext());
                intent.putExtra("ideaContent", clickedIdea.getIdeaContent());
                intent.putExtra("ideaModDate", clickedIdea.getCreatedDate());
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
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mainButton = menu.findItem(R.id.mainActtivity);
        mainButton.setVisible(true);

        MenuItem logoutBtn = menu.findItem(R.id.logout_icon);
        logoutBtn.setVisible(false);

        MenuItem editButton = menu.findItem(R.id.editUser);
        editButton.setVisible(false);

        MenuItem searchButton = menu.findItem(R.id.search_icon);
        searchButton.setVisible(true);

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

    private void getUser() {

        Query myRef;

        final Bundle extras = getIntent().getExtras();
        singleUserName.setText(extras.getString("userName"));
        singleUserFullName.setText(extras.getString("userFirstName") + " " + extras.getString("userLastName"));
        singleUserLocation.setText(extras.getString("userLocation"));
        singleUserIdeasNumberResult.setText(extras.getString("ideaCount"));

        final String clickedUserID = extras.getString("clickedUserID");

        myRef = FirebaseDatabase.getInstance().getReference().child("users").orderByKey().equalTo(clickedUserID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("ideas").exists()) {

                        for (DataSnapshot snap : snapshot.child("ideas").getChildren()) {

                            User idea = new User(userID, userName, firstName, lastName, location, ideaID, ideaTitle, ideaContext, ideaCount);

                            if (snap.child("ideaID").exists()) {
                                idea.setIdeaID(snap.child("ideaID").getValue().toString());
                            }
                            if (snap.child("ideaTitle").exists()) {
                                idea.setIdeaTitle(snap.child("ideaTitle").getValue().toString());
                            }
                            if (snap.child("ideaContext").exists()) {
                                idea.setIdeaContext(snap.child("ideaContext").getValue().toString());
                            }
                            if (snap.child("ideaContent").exists()) {
                                idea.setIdeaContent(snap.child("ideaContent").getValue().toString());
                            }
                            if (snap.child("modifiedDate").exists()) {
                                idea.setCreatedDate(snap.child("modifiedDate").getValue().toString());
                            }
                            ideaList.add(idea);
                            lAdapter.notifyDataSetChanged();
                        }
                    }
                }

                Log.d("ATTENTION", "CURRENT USER ID: " + clickedUserID + " AND USERNAME: " + extras.getString("userName"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("ATENNTION", "USER DETAIS: " + singleUserFullName);
    }

    @Override
    public void onClick(View view) {

        if(buttonState % 2 == 0) {
            unfollowUser();
        } else {
            followUser();
        }
        buttonState++;
    }

    private void followUser() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Bundle extras = getIntent().getExtras();
        String username = extras.getString("userName");
        String clickedUserID = extras.getString("clickedUserID");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favorites").child(clickedUserID);
        reference.child("username").setValue(username);
        reference.child("favoriteID").setValue(clickedUserID);

        followButton.setImageResource(R.drawable.ic_star_filled);
    }

    private void unfollowUser() {

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Bundle extras = getIntent().getExtras();
        String username = extras.getString("userName");
        final String clickedUserID = extras.getString("clickedUserID");

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("Are you sure to Unfollow User?")
                .setMessage("The user will be deleted from your favorites")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favorites").child(clickedUserID);
                        reference.removeValue();
                        followButton.setImageResource(R.drawable.ic_star);
                        Toast.makeText(getApplicationContext(),"The user unfollow",Toast.LENGTH_LONG).show();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void checkIfFavorite() {

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        Bundle extras = getIntent().getExtras();
        final String clickedUserID = extras.getString("clickedUserID");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favorites");

        reference.orderByChild("favoriteID").equalTo(clickedUserID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    followButton.setImageResource(R.drawable.ic_star_filled);
                    Log.d("THE USER HAVE FAVORITES", snapshot.child("favorites").getKey());
                } else {

                    Log.d("THE USER HAVE NOT", "DE HIMA INCH ANENQ VOR QEZ CHEN HAVANEL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}