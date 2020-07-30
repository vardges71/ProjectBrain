package com.vako.projectbrain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<User> userList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userNameTV;
        public TextView name;
        public TextView location;
        public TextView lastPostedLbl;

        public ViewHolder(@NonNull View customView) {
            super(customView);

            userNameTV = (TextView) customView.findViewById(R.id.userNameTV);
            lastPostedLbl = (TextView) customView.findViewById(R.id.lastPostedLbl);
            name = (TextView) customView.findViewById(R.id.name);
            location = (TextView) customView.findViewById(R.id.location);
        }
    }

    public UserListAdapter(List<User> myDataset) {
        userList = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_cell, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);
        holder.userNameTV.setText(user.getUserName());
        holder.lastPostedLbl.setText("Last posted");
        holder.name.setText(user.getFirstName() + " " + user.getLastName() + " |");
        holder.location.setText(" " + user.getLocation());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}