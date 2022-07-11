package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPostsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Posts> postArrayList;
    private PostAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        Toolbar toolbar = (findViewById(R.id.toolbarViewPosts));
        setSupportActionBar(toolbar);
        setTitle("Emergencies");

        recyclerView = findViewById(R.id.recViewPosts);
        postArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postArrayList, this);
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        loadDataInListview();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItem:
                startActivity(new Intent(ViewPostsActivity.this, AddPostActivity.class));
                break;

            case R.id.itmDashboard:
                startActivity(new Intent(ViewPostsActivity.this, DashboardActivity.class));
                return true;

            case R.id.itmShowDonors:
                startActivity(new Intent(ViewPostsActivity.this, ViewDonorsActivity.class));
                return true;

            case R.id.itmCamps:
                startActivity(new Intent(ViewPostsActivity.this, ViewCampsActivity.class));
                return true;

            case R.id.itmProfile:
                startActivity(new Intent(ViewPostsActivity.this, EditProfileActivity.class));
                return true;

            case R.id.itmLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ViewPostsActivity.this, LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataInListview() {
        db.collection("AllPosts").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            Posts p = d.toObject(Posts.class);
                            p.setId(d.getId());
                            postArrayList.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(ViewPostsActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewPostsActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onClick(View v) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_btn, menu);
        inflater.inflate(R.menu.options, menu);
        return true;
    }
}