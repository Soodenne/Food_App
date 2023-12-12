package com.example.food_app.Activity;

public class SharedData {
    private static SharedData instance;
    private int userId;

    private SharedData() {
        // Khởi tạo constructor nếu cần
    }

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
