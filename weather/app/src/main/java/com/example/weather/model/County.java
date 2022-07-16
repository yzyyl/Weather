package com.example.weather.model;


import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    private  int county_id;
    private  String countyName;
    private int weatherId;
    private  int cityId;

    public int getId() {
        return county_id;
    }

    public void setId(int id) {
        this.county_id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyCode() {
        return weatherId;
    }

    public void setCountyCode(int countyCode) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


}
