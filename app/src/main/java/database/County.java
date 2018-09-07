package database;

import org.litepal.crud.DataSupport;

// 通过LitePal进行数据的存储，建立County实体类
public class County extends DataSupport {

    /**
     * {"id":921,"name":"南京","weather_id":"CN101190101"}
     */

    private int id;  // 主键id
    private String countyName;  // name : 南京
    private String weatherId;  // weather_id : CN101190101
    private int cityId;  //该县对应的市id（非市代码cityCode）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
