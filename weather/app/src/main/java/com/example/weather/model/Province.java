package com.example.weather.model;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private int province_id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return province_id;
    }

    public void setId(int id) {
        this.province_id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }


}
