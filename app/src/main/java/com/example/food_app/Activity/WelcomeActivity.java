package com.example.food_app.Activity;

import static android.app.PendingIntent.getActivity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.food_app.Model.FoodDomain;
import com.example.food_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WelcomeActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 3000;
    GoogleSignInClient mGoogleSignInClient;
    Button Sign_in_Google;
    Connection connect;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        initView();
        Sign_in_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void register(View view) {
        startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class));
    }

    public void login(View view) {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }
    public void loginSuccess() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
    }
    private void initView() {
        Sign_in_Google = findViewById(R.id.Sign_in_Google);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String Full_name = acct.getDisplayName();
                String Email = acct.getEmail();
                saveEmail(this, Full_name, Email);
            }

            loginSuccess();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
    public void saveEmail(Context context, String Full_name, String Email) {
        ConnectionHelper connectionHelper = new ConnectionHelper();
        connect = connectionHelper.connectionclass();

        // Kiểm tra xem User_id đã tồn tại trong cơ sở dữ liệu chưa
        if (isEmailExists(context, Email)) {
            // Nếu đã tồn tại, thực hiện cập nhật
            updateEmail(context, Full_name, Email);
        } else {
            // Nếu chưa tồn tại, thực hiện chèn mới
            insertEmail(context, Full_name, Email);
        }
    }
    public boolean isEmailExists(Context context, String Email) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "SELECT COUNT(*) FROM [dbo].[User] WHERE Email = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setString(1, Email);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            return count > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public void updateEmail(Context context,String Full_name, String Email) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "UPDATE [dbo].[User] SET Full_name = ? WHERE Email = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setString(1, Full_name);
                    preparedStatement.setString(2, Email);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Update successful
                        Toast.makeText(context, "Cart successfully updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Update failed
                        Toast.makeText(context, "Update fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertEmail(Context context, String Full_name, String Email) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "INSERT INTO [dbo].[User] (Full_name, Email) VALUES (?, ?)";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setString(1, Full_name);
                    preparedStatement.setString(2, Email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Insert successful
                        Toast.makeText(context, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insert failed
                        // You might want to handle this case
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}