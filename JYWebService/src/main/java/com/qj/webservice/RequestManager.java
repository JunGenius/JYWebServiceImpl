package com.qj.webservice;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Request  ( Retrofit2+Okhttp3 requests WebService via SOAP protocol )
 * author qujun
 * time 2019/1/21 11:13
 * Because had because, so had so, since has become since, why say why。
 **/

public class RequestManager {

    private static RequestManager manager;//管理者实例

    WsServiceImpl apiService = null;

    private String webserviceResult = "";

    private Disposable mDisposable;

    public static synchronized RequestManager getInstance() {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();
        apiService = mRetrofit.create(WsServiceImpl.class);
    }


    private String getXml(String method, Map<String, String> map) {

        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + " <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + " <soap:Body>\n "
                + " <" + method + " xmlns=\"" + WebServiceConfigManager.getBuilder().NAME_SPACE + "\">\n"
                + getNodeValue(map) + "\n"
                + " </" + method + ">\n"
                + " </soap:Body>\n"
                + " </soap:Envelope>\n";

    }

    private String getNodeValue(Map<String, String> map) {
        StringBuilder str = new StringBuilder();

        if (map != null)
            for (Map.Entry<String, String> entry : map.entrySet()) {
                str.append(toStart(entry.getKey())).append(entry.getValue()).append(toEnd(entry.getKey()));
            }
        return str.toString();
    }

    private String toStart(String key) {
        return "<" + key + ">";
    }

    private String toEnd(String key) {
        return "</" + key + ">";
    }


    /**
     * 访问webservice
     */
    void execute(Observable<ResponseBody> call, final String method, Map<String, String> map, final ObservableEmitter<String> emitter) {

        // 添加请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml; charset=utf-8");
        headers.put("SOAPAction", WebServiceConfigManager.getBuilder().NAME_SPACE + method);

        call = apiService.getResponBody(
                headers,
                WebServiceConfigManager.getBuilder().HEAD_PAGE,
                method,
                getXml(method, map));


        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }


                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(getErrorMsg(WS_REQUEST_RESULT.REQUEST_NET_ERROR, "请求连接发生异常(超时 ，断网 etc)"));
                    }

                    @Override
                    public void onComplete() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody == null) {
                            emitter.onError(getErrorMsg(WS_REQUEST_RESULT.REQUEST_ERROR, "请求发生错误"));
                            return;
                        }
                        try {
                            if (analysisResult(responseBody.string(), method)) {
                                emitter.onNext(webserviceResult);
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        emitter.onError(getErrorMsg(WS_REQUEST_RESULT.REQUEST_RESULT_ERROR, "请求结果xml解析错误.."));
                    }

                });
    }


    private Throwable getErrorMsg(int code, String msg) {
        return new Throwable("code:" + WS_REQUEST_RESULT.REQUEST_RESULT_ERROR + " msg:" + msg);
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


    public void cancelRequest() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
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
