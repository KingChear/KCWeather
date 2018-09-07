package database;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gson.Weather;

public class HandleData {

    // 处理省级的json数据
    public static boolean handleProvinceResponse(String jsonResponse) {
        // 判断jsonResponse是否非空
        if (!TextUtils.isEmpty(jsonResponse)) {
            try {
                JSONArray allProvinces = new JSONArray(jsonResponse);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));  // "name":"北京"
                    province.setProvinceCode(provinceObject.getString("id"));  // "id":1
                    province.save();  // 将当前province的内容保存到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 处理市级的json数据
    public static boolean handleCityResponse(String jsonResponse, int provinceId) {
        // 判断jsonResponse是否非空
        if (!TextUtils.isEmpty(jsonResponse)) {
            try {
                JSONArray allCities = new JSONArray(jsonResponse);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));  // "name":"南京"
                    city.setCityCode(cityObject.getString("id"));  // "id":113
                    city.setProvinceId(provinceId);  // 该市对应的省id
                    city.save();  // 将当前city的内容保存到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 处理县级的json数据
    public static boolean handleCountyResponse(String jsonResponse, int cityId) {
        // 判断jsonResponse是否非空
        if (!TextUtils.isEmpty(jsonResponse)) {
            try {
                JSONArray allCounties = new JSONArray(jsonResponse);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));  // "name":"南京"
                    county.setWeatherId(countyObject.getString("weather_id"));  // "weather_id":"CN101190101"
                    county.setCityId(cityId);  // 该县对应的市id
                    county.save();  // 将当前city的内容保存到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 处理天气的json数据
    public static Weather handleWeatherResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
