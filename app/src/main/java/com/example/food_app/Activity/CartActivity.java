package com.example.food_app.Activity;

import static androidx.core.location.LocationManagerCompat.requestLocationUpdates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_app.Adapter.CartListAdapter;
import com.example.food_app.Adapter.FoodListAdapter;
import com.example.food_app.Helper.ChangeNumberItemsListener;
import com.example.food_app.Helper.ManagementCart;
import com.example.food_app.Helper.TinyDB;
import com.example.food_app.Model.Food;
import com.example.food_app.Model.FoodDomain;
import com.example.food_app.Model.UserDomain;
import com.example.food_app.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterCart;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView total_txt, tax_txt, delivery_txt, subtotal_txt, empty_txt;
    private float tax;
    private ScrollView scrollView;
    private ImageView back_btn, address_btn;
    private Button order_btn;
    private RecyclerView recyclerViewFood;
    Context context;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private double longitude, latitude;
    Connection connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        managementCart = new ManagementCart(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        initView();
        initRecycleView();
        calculateCart();
        setVariable();
        address_btn = findViewById(R.id.address_btn);

        address_btn.setOnClickListener(view -> {
            // Khi người dùng nhấn vào nút lấy vị trí
            checkLocationPermission();
            saveLocation(SharedData.getInstance().getUserId(), latitude, longitude);
        });
    }

    //Information
    // Hàm kiểm tra và yêu cầu quyền truy cập vị trí
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu quyền
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            // Nếu đã có quyền, lấy vị trí
            requestLocationUpdates();
        }
    }
    private void requestLocationUpdates() {
        try {
            // Lấy vị trí hiện tại
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000, // Độ nhạy (milliseconds)
                    10, // Khoảng cách (meters)
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            // Xử lý khi vị trí thay đổi
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            // Sử dụng Handler để trì hoãn hiển thị Toast
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                            });
                        }


                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(@NonNull String provider) {
                        }

                        @Override
                        public void onProviderDisabled(@NonNull String provider) {
                        }
                    }
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, lấy vị trí
                requestLocationUpdates();
            } else {
                // Người dùng từ chối cấp quyền, xử lý tương ứng
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveLocation(int User_id, double latitude, double longitude) {
        User_id = SharedData.getInstance().getUserId();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();

            // Kiểm tra xem User_id đã tồn tại trong cơ sở dữ liệu chưa
            if (isUserLocationExists(User_id, connect)) {
                // Nếu đã tồn tại, thực hiện cập nhật
                updateLocation(User_id, latitude, longitude, connect);
            } else {
                // Nếu chưa tồn tại, thực hiện chèn mới
                insertLocation(User_id, latitude, longitude, connect);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra xem User_id đã tồn tại trong bảng User_location hay chưa
    private boolean isUserLocationExists(int User_id, Connection connect) throws SQLException {
        String checkUserQuery = "SELECT COUNT(*) FROM [dbo].[User_location] WHERE User_id = ?";
        try (PreparedStatement checkUserStatement = connect.prepareStatement(checkUserQuery)) {
            checkUserStatement.setInt(1, User_id);
            try (ResultSet resultSet = checkUserStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    // Cập nhật dữ liệu nếu User_id đã tồn tại
    private void updateLocation(int User_id, double latitude, double longitude, Connection connect) throws SQLException {
        String updateQuery = "UPDATE [dbo].[User_location] SET latitude = ?, longitude = ? WHERE User_id = ?";
        try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
            updateStatement.setDouble(1, latitude);
            updateStatement.setDouble(2, longitude);
            updateStatement.setInt(3, User_id);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật thành công
                Toast.makeText(this, "User location updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật thất bại
                Toast.makeText(this, "Failed to update user location!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Chèn dữ liệu nếu User_id chưa tồn tại
    private void insertLocation(int User_id, double latitude, double longitude, Connection connect) throws SQLException {
        String insertQuery = "INSERT INTO [dbo].[User_location] (User_id, latitude, longitude) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connect.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, User_id);
            insertStatement.setDouble(2, latitude);
            insertStatement.setDouble(3, longitude);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Chèn mới thành công
                Toast.makeText(this, "User location added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Chèn mới thất bại
                Toast.makeText(this, "Failed to add user location!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Order Summary
    private float calculatedSubtotal, calculatedTax, calculatedDelivery, calculatedTotal;

    private void setVariable() {
        back_btn.setOnClickListener(V -> finish());
        order_btn.setOnClickListener(V -> insertOrderSummary(
                SharedData.getInstance().getUserId(),
                calculatedSubtotal,
                calculatedTax,
                calculatedDelivery,
                calculatedTotal
        ));
    }
    private void insertOrderSummary(int User_id, float subtotal, float tax, float delivery, float total) {
        User_id = SharedData.getInstance().getUserId();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.connectionclass();

            if (connect != null) {
                // Check if the User_id already exists in the Order_summary table
                if (isUserExists(User_id, connect)) {
                    // Update existing order details
                    updateOrderSummary(User_id, subtotal, tax, delivery, total, connect);
                } else {
                    // Insert new order details
                    insertNewOrderSummary(User_id, subtotal, tax, delivery, total, connect);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserExists(int User_id, Connection connect) throws SQLException {
        String checkUserQuery = "SELECT COUNT(*) FROM [dbo].[Order_summary] WHERE User_id = ?";
        try (PreparedStatement checkUserStatement = connect.prepareStatement(checkUserQuery)) {
            checkUserStatement.setInt(1, User_id);
            try (ResultSet resultSet = checkUserStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private void updateOrderSummary(int User_id, float subtotal, float tax, float delivery, float total, Connection connect) throws SQLException {
        String updateQuery = "UPDATE [dbo].[Order_summary] SET subtotal = ?, tax = ?, delivery = ?, total = ? WHERE User_id = ?";
        try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
            updateStatement.setFloat(1, subtotal);
            updateStatement.setFloat(2, tax);
            updateStatement.setFloat(3, delivery);
            updateStatement.setFloat(4, total);
            updateStatement.setInt(5, User_id);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Update successful
                Toast.makeText(this, "Order details updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Update failed
                Toast.makeText(this, "Failed to update order details!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertNewOrderSummary(int User_id, float subtotal, float tax, float delivery, float total, Connection connect) throws SQLException {
        String insertQuery = "INSERT INTO [dbo].[Order_summary] (User_id, subtotal, tax, delivery, total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connect.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, User_id);
            insertStatement.setFloat(2, subtotal);
            insertStatement.setFloat(3, tax);
            insertStatement.setFloat(4, delivery);
            insertStatement.setFloat(5, total);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Insert successful
                Toast.makeText(this, "Order details inserted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Insert failed
                Toast.makeText(this, "Failed to insert order details!", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void initRecycleView() {
        Food f = new Food();
        ArrayList<FoodDomain> items = f.GetFoodOrders();

        recyclerViewFood = findViewById(R.id.view6);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterCart = new CartListAdapter(items, this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCart();
            }
        });
        recyclerViewFood.setAdapter(adapterCart);

        if(managementCart.getListCart().isEmpty()){
            empty_txt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            empty_txt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
    private void calculateCart(){
        float percentTax = 0.02F;
        float delivery = 10;
        tax = (float) (Math.round(managementCart.getTotalFee() * percentTax*100.0)/100.0);

        float total = Math.round((managementCart.getTotalFee() + tax + delivery)*100.0)/100;
        float totalItem = (float) (Math.round(managementCart.getTotalFee() * 100.0)/100.0);

        calculatedSubtotal = totalItem;
        calculatedTax = tax;
        calculatedDelivery = delivery;
        calculatedTotal = total;

        subtotal_txt.setText("$"+totalItem);
        tax_txt.setText("$"+tax);
        delivery_txt.setText("$"+delivery);
        total_txt.setText("$"+total);
    }
    private void initView() {
        subtotal_txt = findViewById(R.id.subtotal_txt);
        tax_txt = findViewById(R.id.tax_txt);
        delivery_txt = findViewById(R.id.delivery_txt);
        total_txt = findViewById(R.id.total_txt);
        recyclerViewList = findViewById(R.id.view6);
        scrollView = findViewById(R.id.scrollView3);
        back_btn = findViewById(R.id.back_btn);
        empty_txt = findViewById(R.id.empty_txt);
        order_btn = findViewById(R.id.order_btn);
    }
}