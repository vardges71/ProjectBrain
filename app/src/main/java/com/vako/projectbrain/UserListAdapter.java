package com.vako.projectbrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> implements View.OnClickListener {

    public UserListAdapter(@NonNull Context context, ArrayList<User> names) {
        super(context, R.layout.main_list_cell, names);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = convertView;

        if (customView == null){
            customView = inflater.inflate(R.layout.main_list_cell, parent,false); }

        User user = getItem(position);
        TextView userNameTV = (TextView) customView.findViewById(R.id.userNameTV);
        TextView lastPostedLbl = (TextView) customView.findViewById(R.id.lastPostedLbl);
        EditText postContainerET = (EditText) customView.findViewById(R.id.postContainer);
        TextView name = (TextView) customView.findViewById(R.id.name);
        TextView location = (TextView) customView.findViewById(R.id.location);
        Button followBtn = (Button) customView.findViewById(R.id.followBtn);


        userNameTV.setText(user.getUserName());
        lastPostedLbl.setText("Last posted");
//        postContainerET.setText(String.valueOf(user.getResultTRecovered()));
        name.setText(user.getFirstName() + " " + user.getLastName() + " |");
        location.setText(" " + user.getLocation());
        followBtn.setOnClickListener(this);

        return customView;
    }

    @Override
    public void onClick(View view) {

    }
}
