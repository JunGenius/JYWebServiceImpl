package com.qj.webservice;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author qujun
 * @time 2018/5/30 10:41
 * Lonely people will pretend to be very busy.
 */

public class WS_REQUEST_RESULT {

    /*
    *  无法连接服务器
    * */
    static final int REQUEST_NOT_SERVER = 1001;

    /*
    *  请求连接发生异常(超时 ，断网 etc)
    * */
    static final int REQUEST_NET_ERROR = 1002;

    /*
    * 请求发生异常
    * */
    static final int REQUEST_ERROR = 1003;

    /*
    *  请求结果xml解析异常
    * */
    static final int REQUEST_RESULT_ERROR = 1004;

    /*
    * 请求开始
    * */
    static final int REQUEST_START = 1005;

    /*
    *  请求结束
    * */
    static final int REQUEST_FINISH = 1006;

    @IntDef({REQUEST_NOT_SERVER, REQUEST_NET_ERROR, REQUEST_ERROR, REQUEST_START, REQUEST_FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WS_REQUEST_RESULT_TYPE {
    }
}
