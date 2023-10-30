package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUserTasks extends AppCompatActivity {

    private EditText editTextTaskTitle, editTextTaskDescription;
    private TextView textViewDueDate;
    private DatePicker datePicker;
    private Button btnUpdateTask;
    private FirebaseFirestore db;
    private String taskId,taskTitle,taskDesc,taskDueDate,taskOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_tasks);

        taskId = getIntent().getStringExtra("taskId");
        taskTitle = getIntent().getStringExtra("taskTitle");
        taskDesc = getIntent().getStringExtra("taskDesc");
        taskDueDate = getIntent().getStringExtra("taskDueDate");
        taskOfUser = getIntent().getStringExtra("taskOfUser");

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        datePicker = findViewById(R.id.datePicker);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);

        editTextTaskTitle.setText(taskTitle);
        editTextTaskDescription.setText(taskDesc);
        textViewDueDate.setText(taskDueDate);


        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle update button click

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(taskOfUser).child("Tasks");
                taskTitle = editTextTaskTitle.getText().toString();
                taskDesc = editTextTaskDescription.getText().toString();
                taskDueDate = textViewDueDate.getText().toString();
                UserTaskModel taskModel = new UserTaskModel(taskId,taskTitle,taskDesc,taskDueDate);
                ref.child(taskId).setValue(taskModel);
                Intent editIntent = new Intent(EditUserTasks.this, TaskDetails.class);
                editIntent.putExtra("taskId", taskId);
                editIntent.putExtra("taskTitle", taskTitle);
                editIntent.putExtra("taskDesc", taskDesc);
                editIntent.putExtra("taskDueDate", taskDueDate);
                editIntent.putExtra("taskOfUser", taskOfUser);
                startActivity(editIntent);
                finish();
                //updateTask();
            }
        });
    }


    /*private void updateTask() {
        String updatedTitle = editTextTaskTitle.getText().toString().trim();
        String updatedDescription = editTextTaskDescription.getText().toString().trim();

        // Get the selected date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Create a Calendar instance to set the due date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long dueDateInMillis = calendar.getTimeInMillis();

        // Update the task in Firestore
        DocumentReference taskRef = db.collection("Tasks").document(taskId);
        taskRef.update("title", updatedTitle, "description", updatedDescription, "dueDate", dueDateInMillis)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Task updated successfully
                        Toast.makeText(EditUserTasks.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity after updating
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(EditUserTasks.this, "Failed to update task", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/



}