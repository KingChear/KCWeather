package database;

import org.litepal.crud.DataSupport;

// 通过LitePal进行数据的存储，建立Province实体类
public class Province extends DataSupport {

    /**
     * {"id":1,"name":"北京"}
     */

    private int id;  // 主键id
    private String provinceName;  // name : 北京
    private String provinceCode;  // id : 1

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
