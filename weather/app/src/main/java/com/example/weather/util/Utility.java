package com.example.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.weather.model.BtWeatherDB;
import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response)
    {
        if(!TextUtils.isEmpty(response)) {
            try {
            JSONArray allProvinces = new JSONArray(response);
            for (int i = 0; i < allProvinces.length(); i++) {
                JSONObject provinceObject = allProvinces.getJSONObject(i);
                Province province = new Province();
                province.setProvinceName(provinceObject.getString("name"));
                province.setProvinceCode(provinceObject.getInt("id"));
                province.save();
            }
            return true;
        }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  false;
    }

//    public synchronized static boolean handleProvinceResponse(BtWeatherDB btWeatherDB, String response){
//        if (!(TextUtils.isEmpty(response))){
//            String[] allProvince = response.split(",");
//            if (allProvince != null && allProvince.length > 0){
//                for (String p : allProvince){
//                    String[] array = p.split("\\|");
//                    Province province = new Province();
//                    province.setProvinceCode(array[0]);
//                    province.setProvinceName(array[1]);
//                    // 将解析出来的数据存储到Province类
//                    btWeatherDB.saveProvince(province);
//                }
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceID)
    {
        if(!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceID);
                    city.save();
                }
                return  true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  false;
    }
//    public static boolean handleCitiesResponse(BtWeatherDB btWeatherDB,String response,int provinceId){
//        if (!TextUtils.isEmpty(response)){
//            String[] allCities = response.split(",");
//            if (allCities != null && allCities.length > 0){
//                for (String c: allCities){
//                    String[] array = c.split("\\|");
//                    City city = new City();
//                    city.setCityCode(array[0]);
//                    city.setCityName(array[1]);
//                    city.setProvinceId(provinceId);
//                    // 将解析出来的数据存储到City类
//                    btWeatherDB.saveCity(city);
//                }
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityID)
    {
        if(!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                   County county=new County();
                    county.setCountyName(countyObject.getString("name"));
//                    county.se(countyObject.getInt("weatherId"));

                    county.setCityId(cityID);
                    county.save();
                }
                return  true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  false;
    }
//    public static boolean handleCountiesResponse(BtWeatherDB btWeatherDB,String response,int CityId){
//        if (!TextUtils.isEmpty(response)){
//            String[] allCounties = response.split(",");
//            if (allCounties != null && allCounties.length > 0){
//                for (String c: allCounties){
//                    String[] array = c.split("\\|");
//                    County county = new County();
//                    county.setCountyCode(array[0]);
//                    county.setCountyName(array[1]);
//                    county.setCityId(CityId);
//                    btWeatherDB.saveCounty(county);
//                }
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有的天气信息存储到SharePreferences文件中
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2,
                                       String weatherDesp, String publishTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CANADA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}
