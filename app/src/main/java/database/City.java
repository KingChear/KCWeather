package database;

import org.litepal.crud.DataSupport;

// 通过LitePal进行数据的存储，建立City实体类
public class City extends DataSupport {

    /**
     * {"id":113,"name":"南京"}
     */

    private int id;  // 主键id
    private String cityName;  // name : 南京
    private String cityCode;  // id : 113
    private int provinceId;  // 该市对应的省id（非省代码provinceCode）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
