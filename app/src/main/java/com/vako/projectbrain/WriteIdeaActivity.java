package com.vako.projectbrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class WriteIdeaActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference myRef;
    FirebaseAuth mAuth;

    private static final String TAG = "WrireIdeaActivity";
    EditText titleET, contextET, contentET;
    Button saveBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_idea);

        initialize();

        mAuth = FirebaseAuth.getInstance();
    }

    private void initialize() {

        titleET = findViewById(R.id.ideasTitle);
        titleET.setEnabled(false);
        contextET = findViewById(R.id.ideasContext);
        contextET.setEnabled(false);
        contentET = findViewById(R.id.ideasContent);
        contentET.setEnabled(false);

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
        userButton.setVisible(true);

        MenuItem ideaButton = menu.findItem(R.id.goToIdea);
        ideaButton.setVisible(false);

        MenuItem searchButton = menu.findItem(R.id.search_icon);
        searchButton.setVisible(true);

        MenuItem editButton = menu.findItem(R.id.editUser);
        editButton.setVisible(true);

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
                logOut();
                return true;
            case R.id.editUser:
                titleET.setEnabled(true);
                contextET.setEnabled(true);
                contentET.setEnabled(true);
                return true;
            case R.id.search_icon:
                Intent toSearch = new Intent(this, SearchActivity.class);
                startActivity(toSearch);
                return true;
        }
        return false;
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    // ********************************************************************************************

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.btnSave:
                ideaSave(v);
                break;
            case R.id.btnDelete:

                break;
        }
    }

    public void ideaSave(View v) {

        String userID = mAuth.getCurrentUser().getUid();

        String uniqueID = UUID.randomUUID().toString();
        Date currentTime = Calendar.getInstance().getTime();

        String ideaId = uniqueID;
        String currentDate = currentTime.toString();
        String ideaTitle = titleET.getText().toString();
        String ideaContext = contextET.getText().toString();
        String ideaContent = contentET.getText().toString();

        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("ideas").child(ideaId);

        myRef.child("modifiedDate").setValue(currentDate);
        myRef.child("ideaTitle").setValue(ideaTitle);
        myRef.child("ideaContext").setValue(ideaContext);
        myRef.child("ideaContent").setValue(ideaContent);

        titleET.setEnabled(false);
        contextET.setEnabled(false);
        contentET.setEnabled(false);
        contentET.setMovementMethod(new ScrollingMovementMethod());

        Intent toMain = new Intent(WriteIdeaActivity.this, MainActivity.class);
        startActivity(toMain);
    }

    public void getIdea() {


    }
}