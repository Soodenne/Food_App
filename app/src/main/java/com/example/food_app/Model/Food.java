package com.example.food_app.Model;

import com.example.food_app.Activity.ConnectionHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Food {
    Connection connect;
    public ArrayList<FoodDomain> GetAllFoods(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title,description,imgUrl, price, time,energy,score;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select * from [dbo].[Food] where [score] = '5'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    int Food_id = rs.getInt(1);
                   title = rs.getString(2);
                   description = rs.getString(3);
                   imgUrl = rs.getString(4);
                   price = rs.getString(5);
                   time = rs.getString(6);
                   energy = rs.getString(7);
                   score = rs.getString(8);

                   FoodDomain f = new FoodDomain(Food_id, title,description,imgUrl,Integer.parseInt(price),Integer.parseInt(time),Integer.parseInt(energy),Double.parseDouble(score));
                   listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<FoodDomain> GetAllPizzas(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title,description,imgUrl, price, time,energy,score;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select * from [dbo].[Food] where [category] = 'pizza'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    int Food_id = rs.getInt(1);
                    title = rs.getString(2);
                    description = rs.getString(3);
                    imgUrl = rs.getString(4);
                    price = rs.getString(5);
                    time = rs.getString(6);
                    energy = rs.getString(7);
                    score = rs.getString(8);

                    FoodDomain f = new FoodDomain(Food_id, title,description,imgUrl,Integer.parseInt(price),Integer.parseInt(time),Integer.parseInt(energy),Double.parseDouble(score));                    listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<FoodDomain> GetAllBurgers(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title,description,imgUrl, price, time,energy,score;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select * from [dbo].[Food] where [category] = 'burger'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    int Food_id = rs.getInt(1);
                    title = rs.getString(2);
                    description = rs.getString(3);
                    imgUrl = rs.getString(4);
                    price = rs.getString(5);
                    time = rs.getString(6);
                    energy = rs.getString(7);
                    score = rs.getString(8);

                    FoodDomain f = new FoodDomain(Food_id, title,description,imgUrl,Integer.parseInt(price),Integer.parseInt(time),Integer.parseInt(energy),Double.parseDouble(score));                    listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<FoodDomain> GetAllChickens(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title,description,imgUrl, price, time,energy,score;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select * from [dbo].[Food] where [category] = 'chicken'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    int Food_id = rs.getInt(1);
                    title = rs.getString(2);
                    description = rs.getString(3);
                    imgUrl = rs.getString(4);
                    price = rs.getString(5);
                    time = rs.getString(6);
                    energy = rs.getString(7);
                    score = rs.getString(8);

                    FoodDomain f = new FoodDomain(Food_id, title,description,imgUrl,Integer.parseInt(price),Integer.parseInt(time),Integer.parseInt(energy),Double.parseDouble(score));                    listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<FoodDomain> GetAllDrinks(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title,description,imgUrl, price, time,energy,score;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select * from [dbo].[Food] where [category] = 'drink'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    int Food_id = rs.getInt(1);
                    title = rs.getString(2);
                    description = rs.getString(3);
                    imgUrl = rs.getString(4);
                    price = rs.getString(5);
                    time = rs.getString(6);
                    energy = rs.getString(7);
                    score = rs.getString(8);

                    FoodDomain f = new FoodDomain(Food_id, title,description,imgUrl,Integer.parseInt(price),Integer.parseInt(time),Integer.parseInt(energy),Double.parseDouble(score));                    listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<FoodDomain> GetFoodOrders(){
        ArrayList<FoodDomain> listFood = new ArrayList<>();
        String title, picUrl;
        int User_id, Food_id, number_in_cart, total_price, price;
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!= null){
                String query = "Select fo.User_id, fo.Food_id, f.title, f.picUrl, f.price, fo.number_in_cart, fo.total_price\n" +
                        "from Food_order fo join Food f on fo.Food_id = f.Food_id join [User] u on fo.User_id = u.User_id";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    User_id = rs.getInt(1);
                    Food_id = rs.getInt(2);
                    title = rs.getString(3);
                    picUrl = rs.getString(4);
                    price = rs.getInt(5);
                    number_in_cart = rs.getInt(6);
                    total_price = rs.getInt(7);

                    FoodDomain f = new FoodDomain(User_id, Food_id,title, picUrl, price, number_in_cart, total_price);
                    listFood.add(f);
                }
                return listFood;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
