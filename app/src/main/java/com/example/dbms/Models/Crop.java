package com.example.dbms.Models;

public class Crop {
    private String crop_name;
    private String url;

    public Crop(String s, String s1) {
        crop_name = s;
        url = s1;
    }

    String getUrl() {
        return url;
    }


    public Crop(String name)
{
    this.crop_name = name;
}
    String getCrop_name() {
        return crop_name;
    }

}
