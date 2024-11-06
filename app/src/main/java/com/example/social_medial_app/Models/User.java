package com.example.social_medial_app.Models;

public class User {

   public String Image="";
   public String Name="";
    public String Email="";
    public String Password="";


    public User()
    {}

    public User(String image, String name, String email, String password) {
        Image = image;
        Name = name;
        Email = email;
        Password = password;
    }

    public User(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }

    public User(String email, String password) {
        Email = email;
        Password = password;
    }
}
