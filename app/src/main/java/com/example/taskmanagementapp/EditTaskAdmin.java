package com.example.taskmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditTaskAdmin extends AppCompatActivity {

    EditText editTaskDescription;
    RadioGroup editRadioGroupPriority;
    DatePicker editDatePicker;
    Button editTaskButton;
    String taskKey;
    FirebaseDatabase database;
    DatabaseReference taskReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_admin);

        editTaskDescription = findViewById(R.id.editTaskDescription);
        editRadioGroupPriority = findViewById(R.id.editRadioGroupPriority);
        editDatePicker = findViewById(R.id.editDatePicker);
        editTaskButton = findViewById(R.id.editTaskButton);

        Intent intent = getIntent();
        taskKey = intent.getStringExtra("taskKey");

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        taskReference = database.getReference("adminTasks").child(taskKey); // Fix this line to reference the correct task using taskKey

        loadTaskDetails();

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                updateTaskDetails();
            }
        });
    }

    private void loadTaskDetails() {
        taskReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String taskDescription = dataSnapshot.child("taskDescription").getValue(String.class);
                    String taskPriority = dataSnapshot.child("priority").getValue(String.class); // Make sure the key matches the database
                    long dueDate = dataSnapshot.child("dueDate").getValue(Long.class);

                    editTaskDescription.setText(taskDescription);

                    if (taskPriority != null) {
                        if (taskPriority.equals("High")) {
                            editRadioGroupPriority.check(R.id.editHighPriority);
                        } else if (taskPriority.equals("Medium")) {
                            editRadioGroupPriority.check(R.id.editMediumPriority);
                        } else if (taskPriority.equals("Low")) {
                            editRadioGroupPriority.check(R.id.editLowPriority);
                        }
                    }

                    // You need to set the due date in the DatePicker
                    // Assuming your dueDate is a timestamp in milliseconds

                    if (dueDate > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dueDate);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        // Set the DatePicker components accordingly
                        editDatePicker.init(year, month, day, null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle an error in reading from the database
                Toast.makeText(EditTaskAdmin.this, "Failed to load task detail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTaskDetails() {
        // Handle updating task details in Firebase
        // You need to retrieve the values from the fields
        String updatedTaskDescription = editTaskDescription.getText().toString();

        // You also need to get the selected priority from the RadioGroup
        RadioButton selectedPriority = findViewById(editRadioGroupPriority.getCheckedRadioButtonId());
        String updatedTaskPriority = selectedPriority.getText().toString();

        // You also need to get the selected date from the DatePicker
        int year = editDatePicker.getYear();
        int month = editDatePicker.getMonth();
        int day = editDatePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long updatedDueDate = calendar.getTimeInMillis();

        // Update the task in the database using taskKey and the new values
        taskReference.child("taskDescription").setValue(updatedTaskDescription);
        taskReference.child("priority").setValue(updatedTaskPriority);
        taskReference.child("dueDate").setValue(updatedDueDate);

        Toast.makeText(EditTaskAdmin.this, "Task updated successfully", Toast.LENGTH_SHORT).show();

    }
}
