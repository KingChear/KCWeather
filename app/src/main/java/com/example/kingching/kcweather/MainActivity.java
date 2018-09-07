package com.example.kingching.kcweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import util.FinalUtil;
import util.SharedPreferencesUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 从SharedPreferences中获取天气json数据，如果有的话，那么直接跳转到WeatherActivity显示此天气信息，
         * 不再重新选择省市区
         */
        String weatherJson = SharedPreferencesUtil.getString(FinalUtil.WEATHER_JSON);
        if (!TextUtils.isEmpty(weatherJson)) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
