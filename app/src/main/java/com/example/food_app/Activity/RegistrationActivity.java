package com.example.food_app.Activity;

import static java.util.ResourceBundle.getBundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_app.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrationActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonRegister;
    Connection connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        getBundle();
    }

    public void checkRegister(){
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "INSERT INTO [dbo].[User] (Full_Name, Email, Password) VALUES ('" + name + "', '" + email + "', '" + password + "')";
                Statement statement = connect.createStatement();
                statement.executeUpdate(query);

                // Đăng ký thành công, thực hiện các hành động khác (ví dụ: chuyển hướng đến trang đăng nhập)
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình đăng ký
            } else {
                Toast.makeText(getApplicationContext(), "Không thể kết nối đến cơ sở dữ liệu.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getBundle() {
        buttonRegister.setOnClickListener(v -> {
            checkRegister();
        });
    }
    private void initView() {
        editTextName = findViewById(R.id.name_txt);
        editTextEmail = findViewById(R.id.email_txt);
        editTextPassword = findViewById(R.id.password_txt);
        buttonRegister = findViewById(R.id.register_btn);
    }
    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}