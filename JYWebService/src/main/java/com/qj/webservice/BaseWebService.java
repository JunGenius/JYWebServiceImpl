package com.qj.webservice;



import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.ResponseBody;


/**
 * author qujun
 * time 2019/1/21 11:13
 * Because had because, so had so, since has become since, why say why。
 **/

public abstract class BaseWebService {

    private String xmlStr = "";

    private String method = "";

    private Map<String, String> valuesMap = null;

    private Observable<ResponseBody> call = null;

    protected BaseWebService() {
        valuesMap = new HashMap<>();
    }

    private void init() {

        method = setMethod();

        valuesMap = setParamMap();

    }

    /*
    *  处理请求 (必须先设置请求方法名)
    * */
    @SuppressWarnings("unchecked")
    public Observable<String> getRequest() {

        init();

        if (method.equals("")) {
            throw new RuntimeException("Method is NULL.");
        }

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                RequestManager.getInstance().execute(call, method, valuesMap, emitter);
            }
        });
    }

    /*
    *  停止请求
    * */
    public void cancelRequest() {
        RequestManager.getInstance().cancelRequest();
    }

    public abstract Map<String, String> setParamMap();

    public abstract String setMethod();

}