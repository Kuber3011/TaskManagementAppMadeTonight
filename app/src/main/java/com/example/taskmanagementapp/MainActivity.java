package com.example.taskmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button logout, btnAddTask, btnShowUserDetails, btnDeleteUser, btnAdminHome;
    ListView listViewUser;
    //FirebaseFirestore db;
    DatabaseReference tasksRef;
    String passedFromAdmin;

    boolean isadmin, adminCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.btnLogout);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnShowUserDetails = findViewById(R.id.btnShowUserDetails);
        listViewUser = findViewById(R.id.listViewUser);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnAdminHome = findViewById(R.id.btnAdminHome);
        //db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Intent intent = getIntent();
        passedFromAdmin = intent.getStringExtra("userId");
        //Log.e("passedFromAdmin", passedFromAdmin);
        String activeUser = intent.getStringExtra("activeUser");
        tasksRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Tasks");

        checkForAdmin();

        /*if(!(adminCheck)){
            btnDeleteUser.setVisibility(View.GONE);
            btnAddTask.setVisibility(View.VISIBLE);
            btnAdminHome.setVisibility(View.GONE);//View.VISIBLE
            if (currentUser != null) {
                loadUserTasks(currentUser.getUid());
            }
        }else{
            btnDeleteUser.setVisibility(View.VISIBLE);
            btnAddTask.setVisibility(View.GONE);
            btnAdminHome.setVisibility(View.VISIBLE);
            loadUserTasks(passedFromAdmin);
        }*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

        btnAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, admin.class);
                startActivity(intent1);
            }
        });


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateTaskUser.class));
            }
        });

        //change here
        btnShowUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reload the activity to refresh the task list
                loadUserTasks(currentUser.getUid());
            }
        });

        /*listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item position and retrieve the document ID from the adapter
                String taskId = (String) parent.getItemAtPosition(position);

                // Start a new activity to view task details
                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });*/

        listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) listViewUser.getAdapter();

                if (adapter != null) {
                    String taskTitle = adapter.getItem(position);

                    tasksRef.orderByChild("title").equalTo(taskTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Assuming each child key is the task ID
                                UserTaskModel taskModel = new UserTaskModel();
                                taskModel = dataSnapshot.getChildren().iterator().next().getValue(UserTaskModel.class);

                                // Start a new activity to view task details
                                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                                intent.putExtra("taskId", taskModel.getTask_id());
                                intent.putExtra("taskTitle", taskModel.getTitle());
                                intent.putExtra("taskDesc", taskModel.getDescription());
                                intent.putExtra("taskDueDate", taskModel.getDueDate().toString());
                                if(passedFromAdmin == null || passedFromAdmin.isEmpty()){
                                    intent.putExtra("taskOfUser", userId);
                                    intent.putExtra("activeUser", "user");
                                }else{
                                    intent.putExtra("taskOfUser", passedFromAdmin);
                                    intent.putExtra("activeUser", "admin");
                                }
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle failure
                        }
                    });
                }
            }
        });

    }

    /*private void loadUserTasks(String userId) {
        db.collection("Tasks")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> tasks = new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String taskTitle = document.getString("title");
                            // Add more details if needed
                            tasks.add(taskTitle);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, tasks);
                        listViewUser.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }*/

    private void loadUserTasks(String userId) {
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TaskData", "Data change event received"); // Log the event

                List<String> tasks = new ArrayList<>();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Log.d("TaskData", taskSnapshot.getValue().toString()); // Log the task data

                    UserTaskModel taskModel = taskSnapshot.getValue(UserTaskModel.class);

                    if (taskModel != null) {
                        String taskId = taskModel.getTask_id();
                        String taskTitle = taskModel.getTitle();
                        // Add more details if needed
                        tasks.add(taskTitle);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, tasks);
                listViewUser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TaskData", "Error: " + databaseError.getMessage()); // Log errors
            }
        });
    }

    public void checkForAdmin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String activeUserId = user.getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            // Assuming "admin" is a field in your user model indicating admin status
            ref.child(activeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);

                        isadmin = userModel.getAdmin();
                        // Now you can perform actions based on the isadmin value
                        handleAdminResult(isadmin,activeUserId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void handleAdminResult(boolean isadmin, String activeUserid) {
        // Do something with the isadmin value
        if (isadmin) {
            // User is an admin
            btnDeleteUser.setVisibility(View.VISIBLE);
            btnAddTask.setVisibility(View.GONE);
            btnAdminHome.setVisibility(View.VISIBLE);
            loadUserTasks(passedFromAdmin);
        } else {
            // User is not an admin
            btnDeleteUser.setVisibility(View.GONE);
            btnAddTask.setVisibility(View.VISIBLE);
            btnAdminHome.setVisibility(View.GONE);
            loadUserTasks(activeUserid);
        }
    }





























}