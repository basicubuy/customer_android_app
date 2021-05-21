///*
// * Copyright (c) 2021. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
// *
// */
//
//package com.ubuyng.app.ubuyapi.adapters.projects;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ubuyng.app.R;
//import com.ubuyng.app.ubuyapi.Models.ItemProject;
//
//import java.util.List;
//
//public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
//
//    List<ItemProject> projectList;
//    Context context;
//
//    public ProjectAdapter(Context context, List<ItemProject> itemProjects){
//        this.context = context;
//        projectList = itemProjects;
//    }
//    @NonNull
//    @Override
//    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.content_pro_feed, parent, false);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return projectList.size();
//    }
//
//    public class ProjectViewHolder extends RecyclerView.ViewHolder{
//        public ProjectViewHolder(@NonNull View itemView){
//            super(itemView);
//        }
//    }
//}
