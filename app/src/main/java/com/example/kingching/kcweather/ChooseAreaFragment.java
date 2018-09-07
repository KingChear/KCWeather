package com.example.kingching.kcweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.City;
import database.County;
import database.HandleData;
import database.Province;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FinalUtil;
import util.HttpUtil;
import util.ToastUtil;

public class ChooseAreaFragment extends Fragment {

    private static final int PROVINCE_LEVEL = 0;  // 省
    private static final int CITY_LEVEL = 1;  //市
    private static final int COUNTY_LEVEL = 2;  // 县

    private TextView mAreaTitleTv;
    private Button mAreaBackBtn;
    private ListView mAreaLv;

    private ArrayAdapter mAreaAdapter;  // 区域ListView的adapter
    private List<String> mAreaNameList = new ArrayList<>();  // 用于存放ListView中的数据，为区域名称

    private List<Province> mProvinceList;  // 省列表
    private List<City> mCityList;  // 市列表
    private List<County> mCountyList;  // 县列表

    private Province mSelectedProvince;  // 选中的省
    private City mSelectedCity;  // 选中的市
    private County mSelectedCounty;  // 选中的县

    private int mCurrentLevel;  // 当前的级别
    private ProgressDialog mProgressDialog;  // 用于显示网络加载的进度框

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area_fragment, container, false);

        mAreaTitleTv = view.findViewById(R.id.area_title_tv);
        mAreaBackBtn = view.findViewById(R.id.area_back_btn);
        mAreaLv = view.findViewById(R.id.area_lv);

        // 设置显示区域的ListView，布局使用系统布局，数据List采用mAreaNameList来显示区域名称
        mAreaAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, mAreaNameList);
        mAreaLv.setAdapter(mAreaAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAreaLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentLevel == PROVINCE_LEVEL) {
                    // 如果当前级别为省级，则选择具体的省份之后，查询出的应为该省的市列表，此时记录被选中的省份
                    mSelectedProvince = mProvinceList.get(position);
                    queryCities();
                } else if (mCurrentLevel == CITY_LEVEL) {
                    // 如果当前级别为市级，则选择具体的市之后，查询出的应为该市的县列表，此时记录被选中的市
                    mSelectedCity = mCityList.get(position);
                    queryCounties();
                } else if (mCurrentLevel == COUNTY_LEVEL) {
                    // 如果当前级别为县级，先根据县列表取得选中县的0天气id
                    String weatherId = mCountyList.get(position).getWeatherId();  // 得到选中县的weatherId
                    if (getActivity() instanceof MainActivity) {
                        // 如果此Fragment是MainActivity中的，则将weatherId放入Intent中，并打开WeatherActivity
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);  // 将此weatherId传给WeatherActivity
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        // 如果此Fragment是WeatherActivity中的，则做如下三件事
                        // 1、关闭DrawerLayout
                        // 2、设置下拉进度框为显示状态
                        // 3、调用requestWeather()方法来获取天气信息
                        // 得到WeatherActivity的对象
                        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
                        weatherActivity.mDataRefreshSrl.setRefreshing(true);
                        weatherActivity.mAreaDl.closeDrawers();
                        weatherActivity.requestWeather(weatherId);
                    }

                }
            }
        });

        mAreaBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLevel == COUNTY_LEVEL) {  // 如果当前是县级，则点击回退键了之后，应当查询市级数据
                    queryCities();
                } else if (mCurrentLevel == CITY_LEVEL) {  // 如果当前是市级，则点击回退键了之后，应当查询省级数据
                    queryProvinces();
                }
            }
        });

        queryProvinces();  // 一开始显示省份列表
    }

    // 用于显示省级数据
    private void queryProvinces() {
        // 当查询出省列表时，顶部标题应为“中国”，并且没有回退键
        mAreaTitleTv.setText("中国");
        mAreaBackBtn.setVisibility(View.GONE);
        // 通过LitePal数据库加载List数据
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size() > 0) {  // 如果从数据库中查出数据，则将数据显示到ListView中
            mAreaNameList.clear();
            for (Province province : mProvinceList) {
                mAreaNameList.add(province.getProvinceName());
            }
            mAreaAdapter.notifyDataSetChanged();  // 此时ListView的内容已经改变，需要进行刷新
            mAreaLv.setSelection(0);
            mCurrentLevel = PROVINCE_LEVEL;  // 设置当前级别
        } else {  // 如果数据库中没有数据，则去服务器端获取数据
            String url = FinalUtil.PROVINCE_INTERFACE;
            queryFromServer(url, "province");
        }
    }

    // 用于显示市级数据
    private void queryCities() {
        // 当查询出市列表时，顶部标题应为该市对应的省名，并且显示回退键
        mAreaTitleTv.setText(mSelectedProvince.getProvinceName());
        mAreaBackBtn.setVisibility(View.VISIBLE);
        // 通过LitePal数据库加载List数据
        mCityList = DataSupport.where("provinceid=?", String.valueOf(mSelectedProvince.getId()))
                .find(City.class);
        if (mCityList.size() > 0) {  // 如果从数据库中查出数据，则将数据显示到ListView中
            mAreaNameList.clear();
            for (City city : mCityList) {
                mAreaNameList.add(city.getCityName());
            }
            mAreaAdapter.notifyDataSetChanged();  // 此时ListView的内容已经改变，需要进行刷新
            mAreaLv.setSelection(0);
            mCurrentLevel = CITY_LEVEL;  // 设置当前级别
        } else {  // 如果数据库中没有数据，则去服务器端获取数据
            String url = FinalUtil.PROVINCE_INTERFACE + "/" + mSelectedProvince.getProvinceCode();
            queryFromServer(url, "city");
        }
    }

    // 用于显示县级数据
    private void queryCounties() {
        // 当查询出县列表时，顶部标题应为该县对应的市名，并且显示回退键
        mAreaTitleTv.setText(mSelectedCity.getCityName());
        mAreaBackBtn.setVisibility(View.VISIBLE);
        mCountyList = DataSupport.where("cityid=?", String.valueOf(mSelectedCity.getId()))
                .find(County.class);
        if (mCountyList.size() > 0) {  // 如果从数据库中查出数据，则将数据显示到ListView中
            mAreaNameList.clear();
            for (County county : mCountyList) {
                mAreaNameList.add(county.getCountyName());
            }
            mAreaAdapter.notifyDataSetChanged();  // 此时ListView的内容已经改变，需要进行刷新
            mAreaLv.setSelection(0);
            mCurrentLevel = COUNTY_LEVEL;  // 设置当前级别
        } else {  // 如果数据库中没有数据，则去服务器端获取数据
            String url = FinalUtil.PROVINCE_INTERFACE + "/" + mSelectedProvince.getProvinceCode() + "/" +
                    mSelectedCity.getCityCode();
            queryFromServer(url, "county");
        }
    }

    // 从服务器端获取数据
    private void queryFromServer(String url, final String type) {
        showDialog();  // 当从服务器获取数据时，应显示加载进度框
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show("数据加载失败");
                        closeDialog();  // 数据加载完毕后，关闭加载进度框
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseContent = response.body().string();  // 获取服务器返回的数据
                if (!TextUtils.isEmpty(responseContent)) {
                    boolean handleResult = false;  // 用于记录数据处理的结果
                    if (type.equals("province")) {
                        handleResult = HandleData.handleProvinceResponse(responseContent);
                    } else if (type.equals("city")) {
                        handleResult = HandleData.handleCityResponse(responseContent, mSelectedProvince.getId());
                    } else if (type.equals("county")) {
                        handleResult = HandleData.handleCountyResponse(responseContent, mSelectedCity.getId());
                    }
                    if (handleResult) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 从服务器获取数据完毕后，再次调用query**方法来将数据显示在ListView上
                                if (type.equals("province")) {
                                    queryProvinces();
                                } else if (type.equals("city")) {
                                    queryCities();
                                } else if (type.equals("county")) {
                                    queryCounties();
                                }
                                closeDialog();  // 数据加载完毕后，关闭加载进度框
                            }
                        });
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("数据加载失败");
                        }
                    });
                }
            }
        });
    }

    private void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
