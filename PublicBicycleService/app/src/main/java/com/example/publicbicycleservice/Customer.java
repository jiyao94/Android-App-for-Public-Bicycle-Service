package com.example.publicbicycleservice;

/**
 * Created by jiyao on 2017/4/25.
 */

class Customer {
    private String name;
    private String email;
    private String phone;
    private String password;
    private double balance;

    Customer(String n, String e, String p, String passwd, Double b) {
        name = n;
        email = e;
        phone = p;
        password = passwd;
        balance = b;
    }
}
