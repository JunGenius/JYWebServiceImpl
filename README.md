使用步骤:

1.build.gradle

            allprojects {
                repositories {
                    google()
                    jcenter()

                    maven { url 'https://www.jitpack.io' }
                }
            }

2. app build.gradle 

            dependencies {
                 implementation 'com.github.JunGenius:JYWebServiceImpl:Tag'
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

            ws.doRequest(new RequestCallBack() {

                @Override
                public void onSuccess(String data) {
                      txt.setText(data);
                }

                @Override
                public void onError(int result, String msg) {
                      txt.setText(msg);
                }

                @Override
                public void onStart() {
                   // Show Loading...
                }

                @Override
                public void onFinish() {
                   // Dismiss Loading..
                }
            });
