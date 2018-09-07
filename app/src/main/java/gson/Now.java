package gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    // "now": {
    //			"tmp": "32",
    //			"cond": {
    //				"txt": "多云"
    //			}
    //		}

    @SerializedName("tmp")
    public String degree;

    @SerializedName("cond")
    public Cond cond;

    public class Cond {
        @SerializedName("txt")
        public String info;
    }

}
