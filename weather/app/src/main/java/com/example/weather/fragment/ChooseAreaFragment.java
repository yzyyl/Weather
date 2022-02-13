package com.example.weather.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;
import com.example.weather.adapter.PlaceAdapter;
import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.Utility;


import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVERL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backbutton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //区列表
    private List<County> countyList;
    //选中的省份
    private Province selectedProvinces;
    //选中的市
    private City selectdeCity;
    //当前选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View view=inflater.inflate(R.layout.activity_choose_area,container,false);
        titleText=view.findViewById(R.id.title_text);
      backbutton=view.findViewById(R.id.back_button);
      listView=view.findViewById(R.id.list_view);
      adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
      listView.setAdapter(adapter);
      return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE)
                {
                    selectedProvinces=provinceList.get(position);
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY)
                {
                    selectdeCity=cityList.get(position);
                    queryCounties();
                }

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVERL_COUNTY)
                {
                 queryCities();
                }
                else if(currentLevel==LEVEL_CITY)
                {
                  queryProvinces();
                }
            }
        });
       queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces()
    {
        titleText.setText("中国");
        backbutton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器查询
     */
    private void queryCities(){
        titleText.setText(selectedProvinces.getProvinceName());
        backbutton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid=?",String.valueOf(selectedProvinces.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
//            queryFromServer(selectedProvince.getProvinceCode(),"city");
            int provinceCode=selectedProvinces.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器查询
     */
    private void queryCounties(){
        titleText.setText(selectdeCity.getCityName());
        backbutton.setVisibility(View.VISIBLE);
        countyList =LitePal.where("cityid=?",String.valueOf(selectdeCity.getId())).find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

            currentLevel = LEVERL_COUNTY;
        } else {
        int provinceCode=selectedProvinces.getProvinceCode();
        int cityCode=selectdeCity.getCityCode();
        String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
        queryFromServer(address,"county");
        }
    }
    /**
     * 根据传入的代号和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address ,final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
             String responseText=response.body().string();
             boolean result=false;
             if("province".equals(type))
             {
                 result= Utility.handleProvinceResponse(responseText);
             }else  if("city".equals(type))
             {
                 result=Utility.handleCityResponse(responseText,selectedProvinces.getId());
             }else  if("county".equals(type))
             {
                 result=Utility.handleCityResponse(responseText,selectdeCity.getId());
             }
             if(result)
             {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         closeProgressDialog();
                         if("province".equals(type))
                         {
                             queryProvinces();
                         }else  if("city".equals(type))
                         {
                             queryCities();
                         }else  if("county".equals(type))
                         {
                             queryCounties();
                         }
                     }
                 });
             }
            }
        });


    }
    private void showProgressDialog()
    {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}