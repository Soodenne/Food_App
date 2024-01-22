package com.example.food_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.food_app.Model.FoodDomain;
import com.example.food_app.Helper.ManagementCart;
import com.example.food_app.Model.UserDomain;
import com.example.food_app.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetailActivity extends AppCompatActivity {
    private Button addToCartBtn;
    private TextView plusBtn, minusBtn, title_txt, price_txt, description_txt, numberOrder_txt, star_txt, calory_txt, time_txt;
    private ImageView picFood, imageBack;
    private FoodDomain object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        managementCart = new ManagementCart(DetailActivity.this);
        initView();
        getBundle();
    }

    private void getBundle() {
        object = (FoodDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getPicUrl(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(picFood);

        title_txt.setText(object.getTitle());
        price_txt.setText("$" + object.getPrice());
        description_txt.setText(object.getDescription());
        numberOrder_txt.setText("" + numberOrder);
        calory_txt.setText(object.getEnergy() + " Cal");
        star_txt.setText(object.getScore() + "");
        time_txt.setText(object.getTime() + " min");
        addToCartBtn.setText("Add to cart - $" + Math.round(numberOrder * object.getPrice()));

        plusBtn.setOnClickListener(v -> {
            numberOrder = numberOrder + 1;
            numberOrder_txt.setText("" + numberOrder);
            addToCartBtn.setText("Add to cart - $" + Math.round(numberOrder * object.getPrice()));
        });
        minusBtn.setOnClickListener(v -> {
            numberOrder = numberOrder - 1;
            numberOrder_txt.setText("" + numberOrder);
            addToCartBtn.setText("Add to cart - $" + Math.round(numberOrder * object.getPrice()));
        });
        imageBack.setOnClickListener(v ->{
            onBackPressed();
        });
        addToCartBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);

            // Check if the record exists
            if (managementCart.isFoodExists(object)) {
                // If the record exists, update it
                managementCart.updateFood(object);
                Log.d("YourTag", "Record exists, updating");
            } else {
                // If the record does not exist, insert it
                managementCart.insertFood(object);
                Log.d("YourTag", "Record does not exist, inserting");
            }
        });
    }
    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        time_txt = findViewById(R.id.time_txt);
        title_txt = findViewById(R.id.title_txt);
        price_txt = findViewById(R.id.price_txt);
        description_txt = findViewById(R.id.description_txt);
        numberOrder_txt = findViewById(R.id.numberOrder_txt);
        plusBtn = findViewById(R.id.PlusCartBtn);
        minusBtn = findViewById(R.id.MinusCartBtn);
        picFood = findViewById(R.id.foodpic);
        star_txt = findViewById(R.id.star_txt);
        calory_txt = findViewById(R.id.cal_txt);
        imageBack = findViewById(R.id.imageBack);
    }

}