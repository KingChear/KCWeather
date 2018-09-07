package com.example.kingching.kcweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import database.HandleData;
import gson.Forecast;
import gson.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FinalUtil;
import util.HttpUtil;
import util.SharedPreferencesUtil;
import util.ToastUtil;

public class WeatherActivity extends BaseActivity implements View.OnClickListener{

    private TextView mCountyTitleTv;
    private TextView mUpdateTimeTv;
    private TextView mDegreeTv;
    private TextView mWeatherInfoTv;
    private LinearLayout mForecastItemLl;
    private TextView mAQITv;
    private TextView mPM25;
    private TextView mComfortTv;
    private TextView mSportTv;
    private TextView mCarWashTv;

    private TextView mForecastDateTv;
    private TextView mForecastInfoTv;
    private TextView mForecastMaxTv;
    private TextView mForecastMinTv;

    public SwipeRefreshLayout mDataRefreshSrl;  // 下拉刷新控件
    public DrawerLayout mAreaDl;  // 侧滑区域选择栏
    private Button mOpenDrawerLayoutBtn;
    private ImageView mBingPicImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        findViewById();
        setOnClickListener();

        // 先判断SharedPreferences中是否有天气的json数据信息，如果有，则直接用此天气json数据显示。
        // 如果没有天气的json数据信息，则从MainActivity获取的Intent来得到weatherId，通过此weatherId来向服务器请求数据
        String weatherJson = SharedPreferencesUtil.getString(FinalUtil.WEATHER_JSON);
        if (!TextUtils.isEmpty(weatherJson)) {
            Weather weather = HandleData.handleWeatherResponse(weatherJson);
            showWeatherInfo(weather);
        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            requestWeather(weatherId);
        }

        // 对于必应每日一图的加载，如果SharedPreferences中有必应每日一图的URL缓存，则从SharedPreferences中取出
        // 如果没有缓存，则去服务器上获取
        // 通过Glide进行加载
        String bingPicUrl = SharedPreferencesUtil.getString(FinalUtil.BING_PIC_JSON);
        if (!TextUtils.isEmpty(bingPicUrl)) {
            Glide.with(this).load(bingPicUrl).into(mBingPicImg);
        } else {
            loadBingPic();
        }

        // 设置SwipeRefreshLayout对象的下拉刷新响应事件
        mDataRefreshSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * 下拉刷新的逻辑处理：下拉刷新操作，必定是刷新当前天气id的数据信息，那么此天气id对应的天气json
                 * 数据一定是已经存在了SharedPreferences缓存当中的，所以这里只需要重新从SharedPreferences当中
                 * 获取天气json数据，根据天气json数据获取天气id，再调用requestWeather()方法将此天气id传入即可
                 */
                String weatherJson = SharedPreferencesUtil.getString(FinalUtil.WEATHER_JSON);
                Weather weather = HandleData.handleWeatherResponse(weatherJson);
                String weatherId = weather.basic.weatherId;
                requestWeather(weatherId);
            }
        });

    }

    private void findViewById() {
        mCountyTitleTv = findViewById(R.id.county_title_tv);
        mUpdateTimeTv = findViewById(R.id.update_time_tv);
        mDegreeTv = findViewById(R.id.degree_tv);
        mWeatherInfoTv = findViewById(R.id.weather_info_tv);
        mForecastItemLl = findViewById(R.id.forecast_item_ll);
        mAQITv = findViewById(R.id.aqi_tv);
        mPM25 = findViewById(R.id.pm25_tv);
        mComfortTv = findViewById(R.id.comfort_tv);
        mSportTv = findViewById(R.id.sport_tv);
        mCarWashTv = findViewById(R.id.car_wash_tv);

        mOpenDrawerLayoutBtn = findViewById(R.id.open_drawer_layout_btn);

        mDataRefreshSrl = findViewById(R.id.data_refresh_srl);
        mAreaDl = findViewById(R.id.area_dl);
        mBingPicImg = findViewById(R.id.bing_pic_img);
    }

    private void setOnClickListener() {
        mOpenDrawerLayoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.open_drawer_layout_btn:
                mAreaDl.openDrawer(GravityCompat.START);  // 打开侧滑区域选择栏
                break;
            default:
                break;
        }
    }

    /**
     * 根据天气id来获取天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        // 构造获取天气json数据的URL
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=5e30983c19184cbeb0944fc585685a63";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show("数据加载失败");
                        mDataRefreshSrl.setRefreshing(false);  // 数据加载完毕后，取消下拉刷新的进度框
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 得到天气json数据
                final String responseContent = response.body().string();
                if (!TextUtils.isEmpty(responseContent)) {
                    final Weather weather = HandleData.handleWeatherResponse(responseContent);  // 进行json数据处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 此处对weather做一个非空判断以及weather的status状态
                            if (weather != null && "ok".equals(weather.status)) {
                                // 将天气的json信息存储到SharedPreferences当中
                                SharedPreferencesUtil.putString(FinalUtil.WEATHER_JSON, responseContent);
                                showWeatherInfo(weather);  // 显示天气的各个信息
                                mDataRefreshSrl.setRefreshing(false);  // 数据加载完毕后，取消下拉刷新的进度框
                            } else {
                                ToastUtil.show("数据加载失败");
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("数据加载失败");
                            mDataRefreshSrl.setRefreshing(false);  // 数据加载完毕后，取消下拉刷新的进度框
                        }
                    });
                }
            }
        });

        // 每次从服务器上请求天气信息时，同时也请求必应每日一图
        loadBingPic();
    }

    /**
     * 在控件上显示天气的各个信息
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;  // 县名称
        mCountyTitleTv.setText(cityName);
        String updateTime = weather.basic.update.updateTime;  // 更新时间
        mUpdateTimeTv.setText(updateTime);
        String degree = weather.now.degree;  // 温度
        mDegreeTv.setText(degree + "度");
        String weatherInfo = weather.now.cond.info;  // 天气情况
        mWeatherInfoTv.setText(weatherInfo);

        // 将子布局里面的内容先清空
        mForecastItemLl.removeAllViews();
        // 循环加载forecastList中的内容
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastItemLl, false);

            mForecastDateTv = view.findViewById(R.id.forecast_date_tv);
            mForecastInfoTv = view.findViewById(R.id.forecast_info_tv);
            mForecastMaxTv = view.findViewById(R.id.forecast_max_tv);
            mForecastMinTv = view.findViewById(R.id.forecast_min_tv);

            String date = forecast.date;  // 未来的日期
            mForecastDateTv.setText(date);
            String info = forecast.cond.info;  // 未来的天气情况
            mForecastInfoTv.setText(info);
            String max = forecast.degree.max;  // 未来的最高温
            mForecastMaxTv.setText(max);
            String min = forecast.degree.min;  // 未来的最低温
            mForecastMinTv.setText(min);

            mForecastItemLl.addView(view);  // 将当前布局加载进父布局中
        }

        String aqi = weather.aqi.city.aqi;  // 空气质量
        if (!TextUtils.isEmpty(aqi)) {  // 有些城市的空气质量为空，所以此处需要进行判断
            mAQITv.setText(aqi);
            String pm25 = weather.aqi.city.pm25;
            mPM25.setText(pm25);
        }

        String comfort = weather.suggestion.comf.info;  // 舒适情况
        mComfortTv.setText("舒适度：" + comfort);

        String sport = weather.suggestion.sport.info;  // 运动建议
        mSportTv.setText("运动指数：" + sport);

        String carWash = weather.suggestion.cw.info;  // 洗车建议
        mCarWashTv.setText("洗车指数：" + carWash);

    }

    /**
     * 从服务器中加载必应每日一图
     */
    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(FinalUtil.BING_PIC_INTERFACE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show("数据加载失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 获取服务器返回的内容
                final String responseContent = response.body().string();
                if (!TextUtils.isEmpty(responseContent)) {
                    // 将此必应每日一图的URL存入SharedPreferences当中
                    SharedPreferencesUtil.putString(FinalUtil.BING_PIC_JSON, responseContent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 将此必应每日一图的URL通过Glide进行加载
                            Glide.with(WeatherActivity.this).load(responseContent).into(mBingPicImg);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("数据加载失败");
                        }
                    });
                }
            }
        });
    }
}
