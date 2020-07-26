package com.vako.projectbrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IdeasListAdapter extends ArrayAdapter<Idea> implements View.OnClickListener {

    public IdeasListAdapter(@NonNull Context context, ArrayList<Idea> title) {
        super(context, R.layout.ideas_list_cell, title);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = convertView;

        if (customView == null){
            customView = inflater.inflate(R.layout.ideas_list_cell, parent,false); }

        Idea idea = getItem(position);
        TextView ideaTitleTV = (TextView) customView.findViewById(R.id.ideasTitle);
        TextView ideaContextTV = (TextView) customView.findViewById(R.id.ideasContext);

        ideaTitleTV.setText(idea.getTitle());
        ideaContextTV.setText(idea.getContext());

        return customView;
    }

    @Override
    public void onClick(View v) {

    }
}
