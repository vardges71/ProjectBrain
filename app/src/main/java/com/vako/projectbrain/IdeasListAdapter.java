package com.vako.projectbrain;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IdeasListAdapter extends RecyclerView.Adapter<IdeasListAdapter.ViewHolder> {

    private List<User> ideaList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ideaTitle;
        public TextView context;

        public ViewHolder(@NonNull View customView) {
            super(customView);

            ideaTitle = (TextView) customView.findViewById(R.id.ideasTitle);
            context = (TextView) customView.findViewById(R.id.ideasContext);
            context.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public IdeasListAdapter(List<User> myDataset) {
        ideaList = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View ideaView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ideas_list_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(ideaView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User idea = ideaList.get(position);
        holder.ideaTitle.setText(idea.getIdeaTitle());
        holder.context.setText(idea.getIdeaContext());
    }

    @Override
    public int getItemCount() {
        return ideaList.size();
    }
}
