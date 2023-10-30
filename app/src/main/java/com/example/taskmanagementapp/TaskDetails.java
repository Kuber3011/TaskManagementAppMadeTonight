package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetails extends AppCompatActivity {
    private TextView textViewTaskTitle, textViewTaskDescription, textViewDueDate;
    private Button btnEditTask, btnDeleteTask;

    private String taskId, taskTitle, taskDesc, taskDueDate, taskOfUser, activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        taskTitle = intent.getStringExtra("taskTitle");
        taskDesc = intent.getStringExtra("taskDesc");
        taskDueDate = intent.getStringExtra("taskDueDate");
        taskOfUser = intent.getStringExtra("taskOfUser");
        activeUser = intent.getStringExtra("activeUser");

        Log.d("TaskDetails", "Received taskId: " + taskId);

        textViewTaskTitle = findViewById(R.id.textViewTaskTitle);
        textViewTaskDescription = findViewById(R.id.textViewTaskDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        btnEditTask = findViewById(R.id.btnEditTask);
        btnDeleteTask = findViewById(R.id.btnDeleteTask);

        textViewDueDate.setText(taskDueDate);
        textViewTaskTitle.setText(taskTitle);
        textViewTaskDescription.setText(taskDesc);

        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click (open EditTaskActivity)
                Intent editIntent = new Intent(TaskDetails.this, EditUserTasks.class);
                editIntent.putExtra("taskId", taskId);
                editIntent.putExtra("taskTitle", taskTitle);
                editIntent.putExtra("taskDesc", taskDesc);
                editIntent.putExtra("taskDueDate", taskDueDate);
                editIntent.putExtra("taskOfUser", taskOfUser);
                startActivity(editIntent);
            }
        });

        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click

                DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child("Users").child(taskOfUser).child("Tasks").child(taskId);
                taskRef.removeValue();
                Intent deleteIntent = new Intent(TaskDetails.this, MainActivity.class);
                intent.putExtra("activeUser", activeUser);
                //deleteIntent.putExtra("passedFromAdmin", taskOfUser);
                startActivity(deleteIntent);
            }
        });
    }

}