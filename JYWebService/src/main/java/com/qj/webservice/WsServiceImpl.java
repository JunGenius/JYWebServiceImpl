package com.qj.webservice;

import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    Call<ResponseBody> getResponBody(
            @HeaderMap Map<String, String> headers,
            @Path("param") String param,
            @Query("op") String op,
            @Body String requestEnvelope);
}
