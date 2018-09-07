package gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    // "basic": {
    //			"city": "苏州",
    //			"id": "CN101190401",
    //			"update": {
    //				"loc": "2018-09-05 11:45",
    //			}
    //		}

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("update")
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}
