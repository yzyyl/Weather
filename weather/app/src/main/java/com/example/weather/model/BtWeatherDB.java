package com.example.weather.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.weather.db.BtWeatherOpenHelper;

public class BtWeatherDB {
    /**
     * 数据库名
     */
    public  static final String DB_NAME="Bt_weather";
    /**
     * 数据库版本
     */
    public static final int VERSION=1;
    private static BtWeatherDB btWeatherDB;
    private SQLiteDatabase db;
    /**
     * 将构造方法私有化
     */
    private BtWeatherDB(Context context)
    {
        BtWeatherOpenHelper dbHelper=new BtWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();

    }
    /**
     * 获取BtWeatherDB实例
     */
    public synchronized static BtWeatherDB getInstance(Context context)
    {
        if(btWeatherDB==null)
        {
            btWeatherDB=new BtWeatherDB(context);
        }
        return btWeatherDB;
    }

}
