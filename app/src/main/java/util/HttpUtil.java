package util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    // 用于发送Request网络请求，通过Callback进行回调，得到服务器传回的数据
    public static void sendOkHttpRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
