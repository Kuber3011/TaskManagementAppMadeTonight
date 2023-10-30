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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CreateTaskAdmin extends AppCompatActivity {

    EditText taskDescription;
    RadioGroup radioGroupPriority;
    DatePicker datePicker;
    Button btnTaskAssign;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_admin);

        taskDescription = findViewById(R.id.task_description);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        datePicker = findViewById(R.id.datePicker);
        btnTaskAssign = findViewById(R.id.btn_task_assign);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("adminTasks");

        btnTaskAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskDesc = taskDescription.getText().toString().trim();

                // Get the selected priority from the RadioGroup
                int selectedPriorityId = radioGroupPriority.getCheckedRadioButtonId();
                RadioButton selectedPriorityRadioButton = findViewById(selectedPriorityId);
                String selectedPriority = selectedPriorityRadioButton.getText().toString();

                // Get the due date from the DatePicker
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                long dueDate = new Date(year - 1900, month, day).getTime();

                if (taskDesc.isEmpty() || selectedPriority.isEmpty()) {
                    Toast.makeText(CreateTaskAdmin.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an AdminTaskModel with the input data
                    AdminTaskModel model = new AdminTaskModel(taskDesc, selectedPriority, dueDate);

                    // Generate a unique task ID and store the task in Firebase
                    String taskId = reference.push().getKey();
                    reference.child(taskId).setValue(model);

                    Toast.makeText(CreateTaskAdmin.this, "Task successfully created", Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        });

    }
}