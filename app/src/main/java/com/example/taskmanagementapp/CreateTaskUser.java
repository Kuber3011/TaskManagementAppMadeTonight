/*
package com.example.taskmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CreateTaskUser extends AppCompatActivity {

    EditText editTextTaskTitle, editTextTaskDescription;
    DatePicker datePicker;
    Button btnSaveTask;

    FirebaseFirestore db;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_user);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        datePicker = findViewById(R.id.datePicker);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String title = editTextTaskTitle.getText().toString().trim();
        String description = editTextTaskDescription.getText().toString().trim();

        // Get the selected date from the DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Note: Month in DatePicker is zero-based, so add 1 to the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Create a Map to represent the task
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("title", title);
        taskData.put("description", description);
        taskData.put("dueDate", calendar.getTime());
        taskData.put("userId", currentUser.getUid()); // Assuming you store the userId in the "Tasks" collection

        // Add the task to the "Tasks" collection
        db.collection("Tasks").add(taskData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // Task added successfully
                    // You can add a success message or navigate to another screen if needed
                    finish(); // Close the activity after saving the task
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure
                    // You can add an error message or log the error
                }
            });
    }
}
*/

package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;


public class CreateTaskUser extends AppCompatActivity {

    EditText editTextTaskTitle, editTextTaskDescription;
    DatePicker datePicker;
    Button btnSaveTask;

    DatabaseReference tasksRef;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_user);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        tasksRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Tasks");

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        datePicker = findViewById(R.id.datePicker);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String title = editTextTaskTitle.getText().toString().trim();
        String description = editTextTaskDescription.getText().toString().trim();

        // Get the selected date from the DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Note: Month in DatePicker is zero-based, so add 1 to the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Create a UserTaskModel to represent the task


        // Generate a unique key for the task in the "Tasks" collection
        String taskId = tasksRef.push().getKey();
        UserTaskModel task = new UserTaskModel(taskId, title, description, calendar.getTime().toString());

        // Add the task to the "Tasks" collection with the generated key
        tasksRef.child(taskId).setValue(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Task added successfully
                        // You can add a success message or navigate to another screen if needed

                        Toast.makeText(CreateTaskUser.this, "Task saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateTaskUser.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the activity after saving the task
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        // You can add an error message or log the error
                        Toast.makeText(CreateTaskUser.this, "Failed to save task", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
