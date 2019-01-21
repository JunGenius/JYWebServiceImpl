package com.qj.webservice;



/**
 * author qujun
 * time 2019/1/21 11:13
 * Because had because, so had so, since has become since, why say why。
 **/

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public abstract class BaseWebService {

    private String xmlStr = "";

    private String method = "";

    private Map<String, String> valuesMap = null;

    private Call<ResponseBody> call = null;


    protected BaseWebService() {
        valuesMap = new HashMap<>();
    }

    private void init() {

        method = setMethod();

        valuesMap = setParamMap();

        initXml();
    }

    private void initXml() {

        xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + " <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + " <soap:Body>\n "
                + " <" + method + " xmlns=\"" + WebServiceConfigManager.getBuilder().NAME_SPACE + "\">\n"
                + getNodeValue() + "\n"
                + " </" + method + ">\n"
                + " </soap:Body>\n"
                + " </soap:Envelope>\n";

    }

    private String getNodeValue() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
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

    private String getXml() {
        return xmlStr;
    }

    /*
    *  处理请求 (必须先设置请求方法名)
    *
    * */
    @SuppressWarnings("unchecked")
    public void doRequest(final RequestCallBack callBack) {
        try {

            init();

            if (method.equals("")) {
                throw new RuntimeException("Method is NULL.");
            }

            RequestManager rm = RequestManager.getInstance();

            WsServiceImpl impl = rm.apiService;

            // 添加请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/xml; charset=utf-8");
            headers.put("SOAPAction", WebServiceConfigManager.getBuilder().NAME_SPACE + method);

            call = impl.getResponBody(
                    headers,
                    WebServiceConfigManager.getBuilder().HEAD_PAGE,
                    method,
                    getXml());

            rm.execute(call, method, callBack);

        } catch (Exception ex) {
            ex.printStackTrace();
            if (callBack != null) {
                callBack.onError(WS_REQUEST_RESULT.REQUEST_NOT_SERVER, "无法连接服务器");
                callBack.onFinish();
            }
        }
    }


    /*
    *  停止请求
    * */
    public void cancelRequest() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }


    public abstract Map<String, String> setParamMap();

    public abstract String setMethod();

}