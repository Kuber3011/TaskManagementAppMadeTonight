package com.example.taskmanagementapp;

public class AdminTaskModel {
    private String taskDescription;
    private String priority;
    private long dueDate;

    public AdminTaskModel() {
        // Default constructor required for Firebase
    }

    public AdminTaskModel(String taskDescription, String priority, long dueDate) {
        this.taskDescription = taskDescription;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
}
