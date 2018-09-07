package com.example.kingching.kcweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

// 所有的Activity类都继承自此BaseActivity
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 打印当前打开的Activity类名
        Log.d(TAG, getClass().getSimpleName());
    }
}
