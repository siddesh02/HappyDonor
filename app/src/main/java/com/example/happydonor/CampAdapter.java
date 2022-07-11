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

import java.text.BreakIterator;
import java.util.ArrayList;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.ViewHolder> {
    private ArrayList<Camps> campsArrayList;
    private Context context;
    private String currentUserEmail, currentUserName;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db;

    public CampAdapter(ArrayList<Camps> campsArrayList, Context context) {
        this.campsArrayList = campsArrayList;
        this.context = context;
    }

    @Override
    public CampAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CampAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.each_camp, parent, false));
    }

    @Override
    public void onBindViewHolder(CampAdapter.ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        if(currentUser != null) {
            currentUserEmail = currentUser.getEmail();
            currentUserName = currentUser.getDisplayName();
        }
        Camps camps = campsArrayList.get(position);
        holder.orgBy.setText("Organiser: " + camps.getOrgBy());
        holder.cmpDate.setText("Date: " + camps.getCmpDate());
        holder.cmpTime.setText("Time: " + camps.getCmpTime());
        holder.venue.setText("Venue: " + camps.getVenue());
        holder.regLink.setText("Link: " + camps.getRegLink());
        holder.contact.setText("Contact: " + camps.getContact());
        holder.posted.setText("Posted by " + camps.getAddedBy() + " on " + camps.getAddedOn());

        if (currentUserEmail.equals(camps.getAddedBy())) {
            holder.editCamp.setVisibility(View.VISIBLE);
            holder.delCamp.setVisibility(View.VISIBLE);
            holder.callCamp.setVisibility(View.INVISIBLE);
        }
        if (!currentUserEmail.equals(camps.getAddedBy())) {
            holder.editCamp.setVisibility(View.INVISIBLE);
            holder.delCamp.setVisibility(View.INVISIBLE);
            holder.callCamp.setVisibility(View.VISIBLE);
        }

        holder.callCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + camps.getContact().toString());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                context.startActivity(i);
            }
        });

        holder.delCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("AllCamps").document(camps.getId()).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(context, "Camp Deleted!", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            context.startActivity(new Intent(context, ViewCampsActivity.class));
                                        }
                                    }, 3000); // 3 seconds
                                } else {
                                    Toast.makeText(context, "Failed to delete camp!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return campsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView orgBy, cmpDate, cmpTime, venue, regLink, contact, posted;
        private final Button editCamp, delCamp, callCamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orgBy = itemView.findViewById((R.id.txtCampOrgBy));
            cmpDate = itemView.findViewById((R.id.txtCampDate));
            cmpTime = itemView.findViewById((R.id.txtCampTime));
            venue = itemView.findViewById((R.id.txtCampVenue));
            contact = itemView.findViewById((R.id.txtCampContact));
            regLink = itemView.findViewById(R.id.txtCampRegLink);
            posted = itemView.findViewById(R.id.txtCampPosted);
            editCamp = itemView.findViewById(R.id.btnCampEdit);
            delCamp = itemView.findViewById(R.id.btnCampDelete);
            callCamp = itemView.findViewById(R.id.btnCampCall);

            editCamp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Camps camps = campsArrayList.get(getAdapterPosition());
                    Intent i = new Intent(context, EditCampActivity.class);
                    i.putExtra("camp", camps);
                    context.startActivity(i);
                }
            });
        }
    }
}
