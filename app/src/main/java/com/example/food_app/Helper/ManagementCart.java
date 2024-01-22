package com.example.food_app.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.Activity.CartActivity;
import com.example.food_app.Activity.ConnectionHelper;
import com.example.food_app.Activity.SharedData;
import com.example.food_app.Adapter.CartListAdapter;
import com.example.food_app.Model.Food;
import com.example.food_app.Model.FoodDomain;
import com.example.food_app.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagementCart {
    private static Context context;
    private static TinyDB tinyDB;
    private static CartListAdapter adapter;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    Connection connect;

    public boolean isFoodExists(FoodDomain object) {
        Connection connect;
        int userId = SharedData.getInstance().getUserId();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "SELECT COUNT(*) FROM [dbo].[Food_order] WHERE User_id = ? AND Food_id = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setInt(2, object.getFood_id());

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

    public static void updateFood(FoodDomain object) {
        Connection connect;
        int userId = SharedData.getInstance().getUserId();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "UPDATE [dbo].[Food_order] SET number_in_cart = ?, total_price = ? WHERE User_id = ? AND Food_id = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setInt(1, object.getNumberInCart());
                    preparedStatement.setInt(2, object.getNumberInCart() * object.getPrice());
                    preparedStatement.setInt(3, userId);
                    preparedStatement.setInt(4, object.getFood_id());

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

    // Your existing insertFood method remains unchanged
    public void insertFood(FoodDomain object) {
        Connection connect;
        int User_id = SharedData.getInstance().getUserId();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "INSERT INTO [dbo].[Food_order] (User_id, Food_id, number_in_cart, total_price) VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setInt(1, User_id);
                    preparedStatement.setInt(2, object.getFood_id());
                    preparedStatement.setInt(3, object.getNumberInCart());
                    preparedStatement.setInt(4, object.getNumberInCart() * object.getPrice());
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

//    public ArrayList<FoodDomain> getListCart() {
//        return tinyDB.getListObject("CartList");
//    }

    public void plusNumberFood(ArrayList<FoodDomain> listfood, int position, Runnable updateCallback) {
        // Logic tăng số lượng của món ăn được chọn
        listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart() + 1);

        // Logic cập nhật dữ liệu trong TinyDB
        tinyDB.putListObject("CartList", listfood);

        // Logic cập nhật dữ liệu trên SQL Server bất đồng bộ
        new UpdateOrderAsyncTask().execute(listfood, position);

        // Callback để thông báo hoàn tất việc cập nhật
        updateCallback.run();
    }

    public void minusNumberFood(ArrayList<FoodDomain> listFoodSelected, int position, Runnable updateCallback) {
        // Logic giảm số lượng của món ăn được chọn
        if (listFoodSelected.get(position).getNumberInCart() > 1) {
            listFoodSelected.get(position).setNumberInCart(listFoodSelected.get(position).getNumberInCart() - 1);

            // Logic cập nhật dữ liệu trong TinyDB
            tinyDB.putListObject("CartList", listFoodSelected);

            // Kiểm tra nếu totalEachItem lớn hơn 0 trước khi cập nhật vào cơ sở dữ liệu
            if (listFoodSelected.get(position).getNumberInCart() > 0) {
                // Logic cập nhật dữ liệu trên SQL Server bất đồng bộ
                new UpdateOrderAsyncTask().execute(listFoodSelected, position);
            } else {
                // Nếu totalEachItem là 0, xóa mục khỏi cả cơ sở dữ liệu và TinyDB
                new RemoveOrderAsyncTask().execute(listFoodSelected, position);
            }

            // Callback để thông báo hoàn tất việc cập nhật
            updateCallback.run();
        }
    }

    // Trong RemoveOrderAsyncTask
    private static class RemoveOrderAsyncTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            try {
                ArrayList<FoodDomain> listfood = (ArrayList<FoodDomain>) params[0];
                int position = (int) params[1];

                // Logic xóa dữ liệu trên SQL Server
                removeFood(listfood.get(position));

                // Logic xóa dữ liệu trên TinyDB
                listfood.remove(position);
                tinyDB.putListObject("CartList", listfood);

                // Thông báo cập nhật dữ liệu trong Adapter
                // Sử dụng runOnUiThread để thực hiện các hoạt động liên quan đến UI
                ((CartActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Gán adapter vào RecyclerView trước khi thông báo
                        RecyclerView recyclerView = ((CartActivity) context).findViewById(R.id.view6);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private static void removeFood(FoodDomain object) {
        Connection connect;
        int userId = SharedData.getInstance().getUserId();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                String query = "DELETE FROM [dbo].[Food_order] WHERE User_id = ? AND Food_id = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setInt(2, object.getFood_id());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Deletion successful
                        Toast.makeText(context, "Item removed from cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Deletion failed
                        Toast.makeText(context, "Deletion fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static class UpdateOrderAsyncTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            try {
                ArrayList<FoodDomain> listfood = (ArrayList<FoodDomain>) params[0];
                int position = (int) params[1];

                // Logic cập nhật dữ liệu trên SQL Server
                updateFood(listfood.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public float getTotalFee() {
        Food f = new Food();
        ArrayList<FoodDomain> listfood2 = f.GetFoodOrders();
        float fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberInCart());
        }
        return fee;
    }

    public ArrayList<FoodDomain> getListCart() {
        int User_id = SharedData.getInstance().getUserId();
        ArrayList<FoodDomain> cartList = new ArrayList<>();

        Connection connect = null;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            if (connect != null) {
                // Modified the SQL query to properly retrieve cart items for a specific user
                String query = "SELECT fo.User_id, fo.Food_id, f.title, f.picUrl, f.price, fo.number_in_cart, fo.total_price\n" +
                        "FROM Food_order fo\n" +
                        "JOIN Food f ON fo.Food_id = f.Food_id\n" +
                        "JOIN [User] u ON fo.User_id = u.User_id\n" +
                        "WHERE fo.User_id = ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                    preparedStatement.setInt(1, User_id);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int foodId = resultSet.getInt("Food_id");
                            String title = resultSet.getString("title");
                            String picUrl = resultSet.getString("picUrl");
                            int price = resultSet.getInt("price");
                            int numberInCart = resultSet.getInt("number_in_cart");
                            int totalPrice = resultSet.getInt("total_price");

                            // Create a FoodDomain object with retrieved data
                            FoodDomain foodDomain = new FoodDomain(foodId, User_id , title, picUrl, price, numberInCart, totalPrice);
                            foodDomain.setFood_id(foodId);
                            foodDomain.setTitle(title);
                            foodDomain.setPicUrl(picUrl);
                            foodDomain.setPrice(price);
                            foodDomain.setNumberInCart(numberInCart);
                            foodDomain.setTotal_price(totalPrice);
                            cartList.add(foodDomain);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cartList;
    }
}

