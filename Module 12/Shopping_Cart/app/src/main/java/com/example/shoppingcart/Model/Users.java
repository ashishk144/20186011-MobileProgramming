package com.example.shoppingcart.Model;

import java.util.ArrayList;

public class Users {

    private String Name,Number, Password;


    public Users() {

    }

    public Users(String name, String number, String password) {
        this.Name = name;
        this.Number = number;
        this.Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String toString() {
        return Name + Number + Password;
    }
}
