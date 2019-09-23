package com.example.dbms.Models;

public class Crop {
    private String crop_name;
    private String min_ph;
    private String max_ph;
    private String min_rainfall;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Crop(String name)
{
    this.crop_name = name;
}
    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public String getMin_ph() {
        return min_ph;
    }

    public void setMin_ph(String min_ph) {
        this.min_ph = min_ph;
    }

    public String getMax_ph() {
        return max_ph;
    }

    public void setMax_ph(String max_ph) {
        this.max_ph = max_ph;
    }

    public String getMin_rainfall() {
        return min_rainfall;
    }

    public void setMin_rainfall(String min_rainfall) {
        this.min_rainfall = min_rainfall;
    }

    public String getMax_rainfal() {
        return max_rainfal;
    }

    public void setMax_rainfal(String max_rainfal) {
        this.max_rainfal = max_rainfal;
    }

    private String max_rainfal;
}
