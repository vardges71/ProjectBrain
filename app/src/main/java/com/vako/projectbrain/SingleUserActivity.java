package com.vako.projectbrain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SingleUserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference myRef;

    ListView listView;
    ArrayList<Idea> ideasList;
    IdeasListAdapter ideasListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);

        initialize();
    }

    public void initialize() {

        listView = findViewById(R.id.ideasList);
        ideasList = new ArrayList<Idea>();

        ideasListAdapter = new IdeasListAdapter(this, ideasList);
        listView.setAdapter(ideasListAdapter);
    }
}