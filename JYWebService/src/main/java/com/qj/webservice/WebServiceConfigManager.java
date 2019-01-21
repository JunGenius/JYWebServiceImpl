package com.qj.webservice;


/**
 * Webservice configuration management  (require relevant parameters before request)
 * author qujun
 * time 2019/1/21 11:13
 * Because had because, so had so, since has become since, why say why。
 **/

public class WebServiceConfigManager {


    private static WebServiceConfigManager.Builder mBuilder = null;

    public WebServiceConfigManager(WebServiceConfigManager.Builder builder) {
        mBuilder = builder;
    }

    public static WebServiceConfigManager.Builder getBuilder() {
        if (mBuilder == null)
            throw new RuntimeException("Related parameters need to be configured before request..");

        return mBuilder;
    }

    public static class Builder {

        /*
        * WebService命名空间
        * */
        public String NAME_SPACE = "";

        /*
        * WebService访问地址
        * */
        public String BASE_URL = "";


        /*
        * WebService HEAD_PAGE
        * */
        public String HEAD_PAGE = "";

        /*
        * WebService 连接超时时间 (默认20s)
        * */
        public int CONNECT_TIMEOUT = 20; // ws 连接超时时间

        /*
        * WebService  读取超时时间(默认20s)
        * */
        public int READ_TIMEOUT = 20;

        /*
        * WebService  写入超时时间(默认20s)
        * */
        public int WRITE_TIMEOUT = 20;

        /*
        * WebService 缓存路径
        * */
        public String CACHE_PATH = "webservice/cache";

        /*
        * WebService  缓存大小
        * */
        public int CACHE_SIZE = 32 * 1024 * 1024;

        public Builder() {
        }

        public Builder setNAME_SPACE(String NAME_SPACE) {
            this.NAME_SPACE = NAME_SPACE;
            return this;
        }

        public Builder setBASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }


        public Builder setHEAD_PAGE(String HEAD_PAGE) {
            this.HEAD_PAGE = HEAD_PAGE;
            return this;
        }

        public Builder setCONNECT_TIMEOUT(int CONNECT_TIMEOUT) {
            this.CONNECT_TIMEOUT = CONNECT_TIMEOUT;
            return this;
        }

        public Builder setCACHE_PATH(String CACHE_PATH) {
            this.CACHE_PATH = CACHE_PATH;
            return this;
        }

        public Builder setREAD_TIMEOUT(int READ_TIMEOUT) {
            this.READ_TIMEOUT = READ_TIMEOUT;
            return this;
        }

        public Builder setWRITE_TIMEOUT(int WRITE_TIMEOUT) {
            this.WRITE_TIMEOUT = WRITE_TIMEOUT;
            return this;
        }

        public Builder setCACHE_SIZE(int CACHE_SIZE) {
            this.CACHE_SIZE = CACHE_SIZE;
            return this;
        }

        public WebServiceConfigManager build() {
            return new WebServiceConfigManager(this);
        }
    }
}

