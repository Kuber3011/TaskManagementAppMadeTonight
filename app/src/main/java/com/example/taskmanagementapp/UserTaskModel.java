package com.example.taskmanagementapp;

import com.google.firebase.database.DatabaseReference;

public class UserTaskModel {
    private String task_id;
    private String title;
    private String description;
    private String dueDate;

    public UserTaskModel() {
    }

    public UserTaskModel(String task_id, String title, String description, String dueDate) {
        this.title = title;
        this.task_id = task_id;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
