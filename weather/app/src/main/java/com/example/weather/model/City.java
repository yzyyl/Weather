package com.example.weather.model;


import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    private  int city_id;
    private  String cityName;
    private int cityCode;
    private  int provinceId;

    public int getId() {
        return city_id;
    }

    public void setId(int id) {
        this.city_id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }



}
