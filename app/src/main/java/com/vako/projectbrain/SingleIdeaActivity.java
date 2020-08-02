package com.vako.projectbrain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

public class SingleIdeaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SingleIdeaActivity";
    FirebaseAuth mAuth;
    DatabaseReference reference;

    TextView titleTV, authorName, contextTV, contentTV;

    private int buttonState = 1;
    ImageButton followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_idea);

        checkIfFavorite();
        initialize();
    }

    private void initialize() {

        titleTV = findViewById(R.id.ideasTitle);
        contextTV = findViewById(R.id.ideasContext);
        contentTV = findViewById(R.id.ideasContent);
        authorName = findViewById(R.id.authorName);

        followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(this);

        contextTV.setMovementMethod(new ScrollingMovementMethod());
        contentTV.setMovementMethod(new ScrollingMovementMethod());

        getIdea();
    }

    // ************************************* Menu part ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);

        MenuItem userButton = menu.findItem(R.id.userMenu);
        userButton.setVisible(true);

        MenuItem ideaButton = menu.findItem(R.id.goToIdea);
        ideaButton.setVisible(true);

        MenuItem searchButton = menu.findItem(R.id.search_icon);
        searchButton.setVisible(true);

        MenuItem editButton = menu.findItem(R.id.editUser);
        editButton.setVisible(false);

        MenuItem logoutBtn = menu.findItem(R.id.logout_icon);
        logoutBtn.setVisible(false);

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
//                logOut();
                return true;
            case R.id.editUser:
//                titleET.setEnabled(true);
//                contextET.setEnabled(true);
//                contentET.setEnabled(true);
                return true;
            case R.id.search_icon:
                Intent toSearch = new Intent(this, SearchActivity.class);
                startActivity(toSearch);
                return true;
        }
        return false;
    }

    // ********************************************************************************************

    public void getIdea() {

        final Bundle extras = getIntent().getExtras();
        titleTV.setText(extras.getString("ideaTitle"));
        authorName.setText(extras.getString("userFullName"));
        contextTV.setText(extras.getString("ideaContext"));
        contentTV.setText(extras.getString("ideaContent"));
    }

    @Override
    public void onClick(View view) {

        if(buttonState % 2 == 0) {
            unfollowIdea();
        } else {
            followIdea();
        }
        buttonState++;
    }

    private void followIdea() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Bundle extras = getIntent().getExtras();
        String clickedIdeaID = extras.getString("ideaID");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favoritIdeas").child(clickedIdeaID);

        reference.child("favoriteID").setValue(clickedIdeaID);

        followButton.setImageResource(R.drawable.ic_star_filled);
    }

    private void unfollowIdea() {

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Bundle extras = getIntent().getExtras();

        final String clickedIdeaID = extras.getString("ideaID");

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("Are you sure to Unfollow Idea?")
                .setMessage("The idea will be deleted from your favorites")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favoritIdeas").child(clickedIdeaID);
                        reference.removeValue();
                        followButton.setImageResource(R.drawable.ic_star);
                        Toast.makeText(getApplicationContext(),"The idea unfollow", Toast.LENGTH_LONG).show();
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

        Query ref;
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        Bundle extras = getIntent().getExtras();
        final String clickedIdeaID = extras.getString("ideaID");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("favoritIdeas");

        reference.orderByChild("favoriteID").equalTo(clickedIdeaID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    followButton.setImageResource(R.drawable.ic_star_filled);
                    Log.d("THE USER HAVE FAVORITES", snapshot.child("favoritIdeas").getKey());
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