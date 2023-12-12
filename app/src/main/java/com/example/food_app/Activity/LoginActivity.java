package com.example.food_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_app.Model.UserDomain;
import com.example.food_app.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Connection connect;
    private Button login_btn;
    EditText inputEmail, inputPassword;
    Bundle dataBundle;
    SharedPreferences sharedPreferences;
    TextView Full_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        getBundle();

    }
    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
    private List<UserDomain> userList = new ArrayList<>();
    public List<UserDomain> getUserList() {
        return userList;
    }
    public void checkLogin(){
        String userInputEmail  = inputEmail.getText().toString();
        String userInputPassword  = inputPassword.getText().toString();
//        List<UserDomain> userList = new ArrayList<>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "SELECT * FROM [dbo].[User] WHERE Email = '" + userInputEmail + "' AND Password = '" + userInputPassword + "'";

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                if (rs.next()){
                    int User_id = rs.getInt(1);
                    String fullName = rs.getString(2);
                    UserDomain user = new UserDomain(User_id, fullName);
                    // Set User_id vào SharedData
                    SharedData.getInstance().setUserId(User_id);
                    userList.add(user);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putParcelableArrayListExtra("userList", (ArrayList<? extends Parcelable>) userList);
                    startActivity(intent);
                    finish();
                    }else{
                        String error = "Email or Password not correct!";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initView(){
        login_btn = (Button) findViewById(R.id.login_btn);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
    }
    private void getBundle(){
        login_btn.setOnClickListener(v -> {
            checkLogin();
        });
    }

}