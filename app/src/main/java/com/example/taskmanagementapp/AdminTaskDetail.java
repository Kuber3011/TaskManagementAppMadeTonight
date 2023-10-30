package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminTaskDetail extends AppCompatActivity {

    TextView taskDescriptionText, taskPriorityText, taskDueDateText;
    Button editTaskButton, deleteTaskButton;

    FirebaseDatabase database;
    DatabaseReference taskReference;

    String taskKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_task_detail);

        taskDescriptionText = findViewById(R.id.taskDescriptionText);
        taskPriorityText = findViewById(R.id.taskPriorityText);
        taskDueDateText = findViewById(R.id.taskDueDateText);
        editTaskButton = findViewById(R.id.editTaskButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);

        // Get the task key from the intent
        Intent intent = getIntent();
        taskKey = intent.getStringExtra("taskKey");

        database = FirebaseDatabase.getInstance();
        taskReference = database.getReference("adminTasks").child(taskKey);

        // Load and display task details
        loadTaskDetails();

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click (open the edit task activity with the task key)
                Intent editIntent = new Intent(AdminTaskDetail.this, EditTaskAdmin.class);
                editIntent.putExtra("taskKey", taskKey);
                startActivity(editIntent);
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                deleteTask();
            }
        });
    }

    private void loadTaskDetails() {
        taskReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve and display task details
                    String taskDescription = dataSnapshot.child("taskDescription").getValue(String.class);
                    String taskPriority = dataSnapshot.child("priority").getValue(String.class);
                    long dueDate = dataSnapshot.child("dueDate").getValue(Long.class);

                    taskDescriptionText.setText(taskDescription);
                    taskPriorityText.setText(taskPriority);

                    // You can format the due date here, e.g., using SimpleDateFormat
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
                    String formattedDate = dateFormat.format(new Date(dueDate));
                    taskDueDateText.setText(formattedDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle an error in reading from the database
                Toast.makeText(AdminTaskDetail.this, "Failed to load task detail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTask() {
        // Delete the task from Firebase
        taskReference.removeValue();
        Toast.makeText(AdminTaskDetail.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close this activity after deleting
    }
}