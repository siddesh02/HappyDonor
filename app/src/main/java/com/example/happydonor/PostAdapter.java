package com.example.happydonor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Posts> postArrayList;
    private Context context;
    private String currentUserEmail, currentUserName;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db;

    public PostAdapter(ArrayList<Posts> postArrayList, Context context) {
        this.postArrayList = postArrayList;
        this.context = context;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.each_post, parent, false));
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        if(currentUser != null) {
            currentUserEmail = currentUser.getEmail();
            currentUserName = currentUser.getDisplayName();
        }
        Posts posts = postArrayList.get(position);
        holder.patient.setText("Patient: " + posts.getPatient());
        holder.bloodgrp.setText("Blood Group: " + posts.getBloodGrp());
        holder.hospital.setText("Hospital: " + posts.getHospital());
        holder.lastDate.setText("Last Date: " + posts.getLastDate());
        holder.donorsGot.setText("Donors Got: " + posts.getDonorsGot());
        holder.donorsNeeded.setText("Donors Needed: " + posts.getDonorsNeeded());
        holder.posted.setText("Posted by " + posts.getPostedBy() + " on " + posts.getPostedOn());
        if (currentUserEmail.equals(posts.getEmail())) {
            holder.btnDonate.setVisibility(View.INVISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDeletePost.setVisibility(View.VISIBLE);
        }
        if (!currentUserEmail.equals(posts.getEmail())) {
            holder.btnDonate.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.INVISIBLE);
            holder.btnDeletePost.setVisibility(View.INVISIBLE);
        }
        holder.btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + posts.getContact().toString());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                context.startActivity(i);
            }
        });
        holder.btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("AllPosts").document(posts.getId()).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(context, "Post Deleted!", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        context.startActivity(new Intent(context, ViewPostsActivity.class));
                                    }
                                }, 3000); // 3 seconds
                            } else {
                                Toast.makeText(context, "Failed to delete post!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bloodgrp, patient, hospital, lastDate, donorsNeeded, donorsGot, posted;
        private final Button btnDonate, btnEdit, btnDeletePost;

        public ViewHolder(View itemView) {
            super(itemView);
            bloodgrp = itemView.findViewById(R.id.txtBloodGrp);
            patient = itemView.findViewById(R.id.txtPatient);
            hospital = itemView.findViewById(R.id.txtHospital);
            lastDate = itemView.findViewById(R.id.txtLastDate);
            donorsNeeded = itemView.findViewById(R.id.txtDonorsNeeded);
            donorsGot = itemView.findViewById(R.id.txtDonotsGot);
            posted = itemView.findViewById(R.id.txtPosted);
            btnDeletePost = itemView.findViewById(R.id.btnDeletePost);
            btnDonate = itemView.findViewById(R.id.btnDonate);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Posts posts = postArrayList.get(getAdapterPosition());
                    Intent i = new Intent(context, EditPostActivity.class);
                    i.putExtra("post", posts);
                    context.startActivity(i);
                }
            });
        }
    }
}

