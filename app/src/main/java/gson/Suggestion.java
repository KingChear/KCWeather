package gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    // "suggestion": {
    //			"comf": {
    //				"txt": "白天天气多云，并且空气湿度偏大，在这种天气条件下，您会感到有些闷热，不很舒适。"
    //			},
    //			"sport": {
    //				"txt": "天气较好，但因气温较高且风力较强，请适当降低运动强度并注意户外防风。推荐您进行室内运动。"
    //			},
    //			"cw": {
    //				"txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
    //			}
    //		}

    @SerializedName("comf")
    public Comf comf;

    @SerializedName("sport")
    public Sport sport;

    @SerializedName("cw")
    public CW cw;

    public class Comf {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }

    public class CW {
        @SerializedName("txt")
        public String info;
    }

}
