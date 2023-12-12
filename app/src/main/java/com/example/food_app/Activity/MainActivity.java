package com.example.food_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_app.Adapter.FoodListAdapter;
import com.example.food_app.Model.FoodDomain;
import com.example.food_app.Model.Food;
import com.example.food_app.Model.UserDomain;
import com.example.food_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterFoodList;
    private RecyclerView recyclerViewFood;
    Connection connect;
    Context context;
    TextView Full_name;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Full_name =findViewById(R.id.Full_name);
        Intent intent = getIntent();
        List<UserDomain> userList = intent.getParcelableArrayListExtra("userList");
        if (userList != null && !userList.isEmpty()) {
            // Lấy thông tin người dùng từ danh sách (ví dụ: lấy thông tin người dùng đầu tiên)
            UserDomain user = userList.get(0);

            // Hiển thị thông tin trên giao diện
            Full_name.setText(user.getFull_name());
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        EditText searchInput = findViewById(R.id.search_input);
        ImageView searchBtn = findViewById(R.id.search_btn);
        ImageView logout_btn = findViewById(R.id.logout_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy nội dung từ EditText
                String searchText = searchInput.getText().toString();

                // Kiểm tra xem searchText có giá trị hay không
                if (!searchText.isEmpty()) {
                    if(searchText.equals("Pizza")) {
                        // Nếu có giá trị, chuyển trang tới layout khác
                        Intent intent = new Intent(MainActivity.this, PizzaActivity.class);
                        startActivity(intent);
                    } else if (searchText.equals("Burger")) {
                        Intent intent = new Intent(MainActivity.this, BurgerActivity.class);
                        startActivity(intent);
                    }else if (searchText.equals("Chicken")) {
                        Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                        startActivity(intent);
                    }else if (searchText.equals("Drinks")) {
                        Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                onBackPressed();
            }
        });

        initRecycleView();
        bottomNavigation();
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void bottomNavigation() {
        LinearLayout home_btn = findViewById(R.id.home_btn);
        LinearLayout cart_btn = findViewById(R.id.cart_btn);
        ConstraintLayout pizza_btn = findViewById(R.id.pizza_btn);
        ConstraintLayout burger_btn = findViewById(R.id.burger_btn);
        ConstraintLayout chicken_btn = findViewById(R.id.chicken_btn);
        ConstraintLayout drink_btn = findViewById(R.id.drink_btn);
        home_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cart_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        pizza_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, PizzaActivity.class)));
        burger_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, BurgerActivity.class)));
        chicken_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, ChickenActivity.class)));
        drink_btn.setOnClickListener(V -> startActivity(new Intent(MainActivity.this, DrinkActivity.class)));
    }
    private void initRecycleView() {
        Food f = new Food();
        ArrayList<FoodDomain> items = f.GetAllFoods();

        recyclerViewFood = findViewById(R.id.view1);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterFoodList = new FoodListAdapter(items);
        recyclerViewFood.setAdapter(adapterFoodList);
    }
}