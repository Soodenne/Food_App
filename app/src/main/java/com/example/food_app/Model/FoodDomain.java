package com.example.food_app.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Food")
public class FoodDomain implements Serializable {
    public int User_id ,Food_id;
    private String title;
    private String description;
    private String picUrl;
    private int price;
    private int time;
    private int energy;
    private double score;
    private int numberInCart, total_price;

    public FoodDomain(int user_id, int food_id, String title, String picUrl, int price, int numberInCart, int total_price) {
        User_id = user_id;
        Food_id = food_id;
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.numberInCart = numberInCart;
        this.total_price = total_price;
    }

    public FoodDomain(int Food_id, String title, String description, String picUrl, int price, int time, int energy, double score) {
        this.Food_id = Food_id;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.time = time;
        this.energy = energy;
        this.score = score;
    }

    public FoodDomain(String title, String description, String picUrl, int price, int time, int energy, double score) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.time = time;
        this.energy = energy;
        this.score = score;
    }

    public FoodDomain(String title, String picUrl, int time, double score) {
        this.title = title;
        this.picUrl = picUrl;
        this.time = time;
        this.score = score;
    }

    public int getFood_id() {
        return Food_id;
    }

    public void setFood_id(int food_id) {
        Food_id = food_id;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
