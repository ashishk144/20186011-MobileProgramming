package com.example.grievanceapp.Model;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String Name, Number, Password;
    private boolean isAdmin;
    private List<Object> comps;



    public Users() {
        this.Name = "";
        this.Number = "";
        this.Password = "";

        this.comps = new ArrayList<>();
        System.out.println(isAdmin + "In 3");

    }
    public Users(String name, String number, String password, boolean isAdmin) {
        this.Name = name;
        this.Number = number;
        this.Password = password;
        System.out.println(isAdmin + "In 4");
        this.isAdmin = isAdmin;
        this.comps = new ArrayList<>();
    }
    public Users(String name, String number, String password, boolean isAdmin, List<Object> comps) {
        Name = name;
        Number = number;
        Password = password;
        this.isAdmin = isAdmin;
        this.comps = comps;
        System.out.println(isAdmin + "In 5");

    }

    public Object getComp(int index) {
        return this.comps.get(index);
    }

    public List<Object> getComps() {

        return this.comps;
    }

    public void addComp(Object compl) {
        this.comps.add(compl);
    }

    public void deleteComp(int position) {
        this.comps.remove(position);
    }



    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setisAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNumber() {
        return this.Number;
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
        return Name + Number + Password + isAdmin + comps.toString();
    }
}
