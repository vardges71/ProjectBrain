package com.vako.projectbrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IdeasListAdapter extends RecyclerView.Adapter<IdeasListAdapter.ViewHolder> {

    private List<Idea> ideaList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ideaTitle;
        public EditText context;

        public ViewHolder(@NonNull View customView) {
            super(customView);

            ideaTitle = (TextView) customView.findViewById(R.id.ideasTitle);
            context = (EditText) customView.findViewById(R.id.ideasContext);
        }
    }

    public IdeasListAdapter(List<Idea> myDataset) {
        ideaList = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ideas_list_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Idea idea = ideaList.get(position);
        holder.ideaTitle.setText(idea.getTitle());
        holder.context.setText(idea.getContext());
    }

    @Override
    public int getItemCount() {
        return ideaList.size();
    }
}
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View customView = convertView;
//
//        if (customView == null){
//            customView = inflater.inflate(R.layout.ideas_list_cell, parent,false); }
//
//        Idea idea = getItem(position);
//        TextView ideaTitleTV = (TextView) customView.findViewById(R.id.ideasTitle);
//        TextView ideaContextTV = (TextView) customView.findViewById(R.id.ideasContext);
//
//        ideaTitleTV.setText(idea.getTitle());
//        ideaContextTV.setText(idea.getContext());
//
//        return customView;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//}
