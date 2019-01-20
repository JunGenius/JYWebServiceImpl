package com.qj.webservice;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 *
 *                                                    __----~~~~~~~~~~~------___
 *                                   .  .   ~~//====......          __--~ ~~
 *                   -.            \_|//     |||\\  ~~~~~~::::... /~
 *                ___-==_       _-~o~  \/    |||  \\            _/~~-
 *        __---~~~.==~||\=_    -_--~/_-~|-   |\\   \\        _/~
 *    _-~~     .=~    |  \\-_    '-~7  /-   /  ||    \      /
 *  .~       .~       |   \\ -_    /  /-   /   ||      \   /
 * /  ____  /         |     \\ ~-_/  /|- _/   .||       \ /
 * |~~    ~~|--~~~~--_ \     ~==-/   | \~--===~~        .\
 *          '         ~-|      /|    |-~\~~       __--~~
 *                      |-~~-_/ |    |   ~\_   _-~            /\
 *                           /  \     \__   \/~                \__
 *                       _--~ _/ | .-~~____--~-/                  ~~==.
 *                      ((->/~   '.|||' -_|    ~~-/ ,              . _||
 *                                 -_     ~\      ~~---l__i__i__i--~~_/
 *                                 _-~-__   ~)  \--______________--~~
 *                               //.-~~~-~_--~- |-------~~~~~~~~
 *                                      //.-~~~--\
 *                               神兽保佑
 *                              代码无BUG!
 *
 *
 * @des 请求处理 ( Retrofit2+Okhttp3 通过SOAP协议请求WebService )
 * @author qujun
 * @time 2019/1/19 22:37
 */
public class RequestManager {

    private static RequestManager manager;//管理者实例

    WsServiceImpl apiService = null;

    private String webserviceResult = "";

    static synchronized RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();
        return manager;
    }

    private RequestManager() {
        build();
    }

    private void build() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(Environment.getExternalStorageDirectory(), WebServiceConfigManager.getBuilder().CACHE_PATH);

        Cache cache = new Cache(httpCacheDirectory, WebServiceConfigManager.getBuilder().CACHE_SIZE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.readTimeout(WebServiceConfigManager.getBuilder().READ_TIMEOUT, TimeUnit.SECONDS);

        builder.writeTimeout(WebServiceConfigManager.getBuilder().WRITE_TIMEOUT, TimeUnit.SECONDS);

        builder.connectTimeout(WebServiceConfigManager.getBuilder().CONNECT_TIMEOUT, TimeUnit.SECONDS);

        builder.retryOnConnectionFailure(true);

        builder.addInterceptor(interceptor);

        builder.cache(cache);

        OkHttpClient mClient = builder.build();

        String BASE_URl = WebServiceConfigManager.getBuilder().BASE_URL;

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mClient)
                .build();
        apiService = mRetrofit.create(WsServiceImpl.class);
    }

    /**
     * 访问webservice
     */
    void execute(Call<ResponseBody> call, final String method, final RequestCallBack callBack) {

        callBack.onStart();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body() == null) {
                    callBack.onError(WS_REQUEST_RESULT.REQUEST_ERROR, "请求发生错误..");
                    callBack.onFinish();
                    return;
                }

                try {
                    if (analysisResult(response.body().string(), method)) {
                        callBack.onSuccess(webserviceResult);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                callBack.onError(WS_REQUEST_RESULT.REQUEST_RESULT_ERROR, "请求结果xml解析错误..");
                callBack.onFinish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onError(WS_REQUEST_RESULT.REQUEST_NET_ERROR, t.toString());
                callBack.onFinish();
            }
        });
    }


    // 解析xml
    private boolean analysisResult(String response, final String method) {

        String startTag = "<" + method + "Result" + ">";
        String endTag = "</" + method + "Result" + ">";
        if (response.contains(startTag) && response.contains(endTag)) {
            int startIndex = response.indexOf(startTag) + startTag.length();
            int endIndex = response.lastIndexOf(endTag);
            webserviceResult = response.substring(startIndex, endIndex);
            return true;
        }

        return false;
    }

    /*
    *  清除实例
    * */
    public void clear() {

        if (manager != null)
            manager = null;

        if (apiService != null)
            apiService = null;
    }
}
