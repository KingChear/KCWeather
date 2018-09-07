package application;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class KCApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        LitePal.initialize(mContext);
    }

    // 获取全局Context;
    public static Context getContext() {
        return mContext;
    }
}
