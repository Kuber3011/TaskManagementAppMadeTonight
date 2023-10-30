package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity {

    //Button assignTaskButton;
    ListView userListView;

    FirebaseDatabase database;
    DatabaseReference userReference;
    ArrayAdapter<String> userAdapter;
    List<String> userIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //addTaskButton = findViewById(R.id.addTaskButton);
        //assignTaskButton = findViewById(R.id.assignTaskButton);
        userListView = findViewById(R.id.userListView);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("Users");

        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        userListView.setAdapter(userAdapter);

        userIds = new ArrayList<>();

        loadUsers();

        // Handle item click in the ListView
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected task key
                String selectedUserKey = userIds.get(position);

                // Start the TaskDetails activity with the selected task key
                Intent intent = new Intent(admin.this, MainActivity.class);
                intent.putExtra("userId", selectedUserKey);
                startActivity(intent);
            }
        });


    }

/*    private void loadUsers() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAdapter.clear();
                userIds.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = taskSnapshot.getValue(UserModel.class);
                    if (user != null) {
                        userAdapter.add(user.getUser_email());
                        userIds.add(user.getUser_id());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle an error in reading from the database
            }
        });
    }*/

    private void loadUsers() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAdapter.clear();
                userIds.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = taskSnapshot.getValue(UserModel.class);
                    if (user != null && user.getUser_email() != null) {
                        userAdapter.add(user.getUser_email());
                        userIds.add(user.getUser_id());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle an error in reading from the database
                Log.e("DatabaseError", "Error: " + databaseError.getMessage());
            }
        });
    }



    public void logoutAdmin(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

    }
