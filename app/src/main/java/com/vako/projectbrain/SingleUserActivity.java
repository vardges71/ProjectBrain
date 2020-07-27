package com.vako.projectbrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SingleUserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference myRef;

    private List<Idea> ideaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IdeasListAdapter lAdapter;

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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_icon).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
}