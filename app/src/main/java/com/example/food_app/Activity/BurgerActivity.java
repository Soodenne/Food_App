package com.example.food_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.food_app.Adapter.FoodListAdapter;
import com.example.food_app.Model.Food;
import com.example.food_app.Model.FoodDomain;
import com.example.food_app.R;

import java.util.ArrayList;

public class BurgerActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterFoodList;
    private RecyclerView recyclerViewFood;
    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);
        initRecycleView();
        imageBack = findViewById(R.id.back_btn);
        imageBack.setOnClickListener(v ->{
            onBackPressed();
        });
    }
    private void initRecycleView() {
        Food f = new Food();
        ArrayList<FoodDomain> items = f.GetAllBurgers();

        recyclerViewFood = findViewById(R.id.burger_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewFood.setLayoutManager(layoutManager);

        adapterFoodList = new FoodListAdapter(items);
        recyclerViewFood.setAdapter(adapterFoodList);
    }
}