package com.example.publicbicycleservice;

/**
 * Created by jiyao on 2017/4/26.
 */

class BicycleBook {
    private String email;
    private String code;
    private boolean locked;

    BicycleBook(String e, String c, boolean l) {
        email = e;
        code = c;
        locked = l;
    }
}

class BicycleTime {
    private String email;
    private String code;
    private int hours;

    BicycleTime(String e, String c, int h) {
        email = e;
        code = c;
        hours = h;
    }
}