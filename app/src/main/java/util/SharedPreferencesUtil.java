package util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import application.KCApplication;

public class SharedPreferencesUtil {

    private static SharedPreferences sharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(KCApplication.getContext());
    private static SharedPreferences.Editor editor = sharedPreferences.edit();

    /*------------------------------------- String类型 --------------------------------------------*/

    /**
     * 存储String类型的值
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 返回的默认值为空字符串
     * @param key
     * @return
     */
    public static String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 自定义返回的String默认值
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /*------------------------------------- int类型 --------------------------------------------*/

    /**
     * 存储int类型的值
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 返回的默认值为0
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 自定义返回的int默认值
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /*------------------------------------- float类型 --------------------------------------------*/

    /**
     * 存储float类型的值
     * @param key
     * @param value
     */
    public static void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 返回的默认值为0
     * @param key
     * @return
     */
    public static Float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    /**
     * 自定义返回的float默认值
     * @param key
     * @param defValue
     * @return
     */
    public static Float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    /*------------------------------------- boolean类型 --------------------------------------------*/

    /**
     * 存储boolean类型的值
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 返回的默认值为false
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 自定义返回的Boolean默认值
     * @param key
     * @param defValue
     * @return
     */
    public static Boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }
}
