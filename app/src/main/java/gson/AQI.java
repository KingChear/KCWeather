package gson;

import com.google.gson.annotations.SerializedName;

public class AQI {

    // "aqi": {
    //			"city": {
    //				"aqi": "54",
    //				"pm25": "28",
    //			}
    //		}

    @SerializedName("city")
    public City city;

    public class City {

        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;
    }

}
