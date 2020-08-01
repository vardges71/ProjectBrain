package com.vako.projectbrain;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SingleIdeaActivity extends AppCompatActivity {

    private static final String TAG = "SingleIdeaActivity";

    TextView titleTV, authorName, contextTV, contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_idea);

        initialize();
    }

    private void initialize() {

        titleTV = findViewById(R.id.ideasTitle);
        contextTV = findViewById(R.id.ideasContext);
        contentTV = findViewById(R.id.ideasContent);
//        authorName = findViewById(R.id.authorName);

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
//        authorName.setText(extras.getString("firstName"));
        contextTV.setText(extras.getString("ideaContext"));
        contentTV.setText(extras.getString("ideaContent"));
    }
}