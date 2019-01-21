package com.qj.webservice;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Webservice parameter interface
 * author qujun
 * time 2019/1/21 11:13
 * Because had because, so had so, since has become since, why say why。
 **/

public interface WsServiceImpl {

    @POST("{param}")
    Observable<ResponseBody> getResponBody(
            @HeaderMap Map<String, String> headers,
            @Path("param") String param,
            @Query("op") String op,
            @Body String requestEnvelope);
}
