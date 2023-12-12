package com.example.food_app.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.food_app.Activity.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class UserDomain implements Parcelable {
    int User_id;
    String Full_name, Email, Password, Image_user;

    public  UserDomain(){

    }

    public UserDomain(int User_id, String full_name) {
        this.User_id = User_id;
        this.Full_name = full_name;
    }
    public UserDomain( String email, String password) {
        Email = email;
        Password = password;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public String getFull_name() {
        return Full_name;
    }

    public void setFull_name(String full_name) {
        Full_name = full_name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage_user() {
        return Image_user;
    }

    public void setImage_user(String image_user) {
        Image_user = image_user;
    }

    protected UserDomain(Parcel in) {
        User_id = in.readInt();
        Full_name = in.readString();
        Email = in.readString();
        Password = in.readString();
    }


    public static final Creator<UserDomain> CREATOR = new Creator<UserDomain>() {
        @Override
        public UserDomain createFromParcel(Parcel in) {
            return new UserDomain(in);
        }

        @Override
        public UserDomain[] newArray(int size) {
            return new UserDomain[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(User_id);
        parcel.writeString(Full_name);
        parcel.writeString(Email);
        parcel.writeString(Password);
    }
}
