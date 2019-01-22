使用步骤:

1. app build.gradle 

            dependencies {
                 implementation 'com.qj.webservice:QYWebservice:1.1.0'
            }
            

代码:

1. webservice: 参数配置 (需application中配置相关参数) (连接超时时间等参数不设置取默认值)

              new WebServiceConfigManager.Builder()
   
                .setBASE_URL("http://www.webxml.com.cn/WebServices/") // 请求地址
                
                .setNAME_SPACE("http://WebXml.com.cn/") // 命名空间
                
                .setHEAD_PAGE("WeatherWebService.asmx") // 页面
                
                .build();

2. Webservice


             public class TestWs extends BaseWebService {

                private String byProvinceName = "";

                public TestWs(String byProvinceName) {
                    this.byProvinceName = byProvinceName;
                }

                @Override
                protected Map<String, String> setParamMap() {

                    Map<String, String> map = new HashMap<>();
                    map.put("byProvinceName", byProvinceName); // 查询参数
                    return map;
                }

                @Override
                protected String setMethod() {
                    return "getSupportCity"; // 查询方法
                }
             }

3. 访问webservice

           TestWs ws = new TestWs("北京");

           ws.getRequest()
                // 处理相应的实体转换 
                .map(new Function<String, Object>() {
                    @Override
                    public Info apply(String s) throws Exception {
                        return new Object();
                    }
                }).subscribe(new Observer<Info>() {
            @Override
            public void onSubscribe(Disposable d) {
               
            }

            @Override
            public void onNext(Object o) {
               
            }

            @Override
            public void onError(Throwable e) {
                
            }

            @Override
            public void onComplete() {
                
            }
        });
