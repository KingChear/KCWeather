package gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    // "daily_forecast": [{
    //			"date": "2018-09-05",
    //			"cond": {
    //				"txt_d": "多云"
    //			},
    //			"tmp": {
    //				"max": "32",
    //				"min": "24"
    //			}
    //		}, {
    //			"date": "2018-09-07",
    //			"cond": {
    //				"txt_d": "雷阵雨"
    //			},
    //			"tmp": {
    //				"max": "25",
    //				"min": "22"
    //			}
    //		}]

    @SerializedName("date")
    public String date;

    @SerializedName("cond")
    public Cond cond;

    @SerializedName("tmp")
    public Degree degree;

    public class Cond {
        @SerializedName("txt_d")
        public String info;
    }

    public class Degree {
        @SerializedName("max")
        public String max;

        @SerializedName("min")
        public String min;
    }

}
