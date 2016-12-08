package thp.csii.com.http;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Constant {

    //    auth.serverHost=192.168.199.101:8080
//    auth.certificatePath=/jiiiiiin.cer
//    auth.appid=TIANHONG_APPID
//    auth.secret=TIANHONG_SECRET
//    auth.accessLoginTokenUrl=http://SERVER_HOST/payment-access/generateAccessToken?appid=APPID&secret=SECRET
//    auth.accessSecretKeyUrl=http://SERVER_HOST/payment-access/getAccessSecretKey?encryptUniqueID=ENCRYPTUNIQUEID
//    auth.accessLoginAddrUrl=http://SERVER_HOST/payment-access/getAccessLoginURI
//    auth.logOutUrl=http://SERVER_HOST/payment-access/logOut
//    auth.key=Key
    public static void setSERVERHOST(String url){
        SERVERHOST=url;
        accessLoginTokenUrl=SERVERHOST+"/payment-access/generateAccessToken?appid=APPID&secret=SECRET";
        accessSecretKeyUrl=SERVERHOST+"/payment-access/getAccessSecretKey?encryptUniqueID=ENCRYPTUNIQUEID";
        accessLoginAddrUrl=SERVERHOST+"/payment-access/getAccessLoginURI";
        logOutUrl=SERVERHOST+"/payment-access/logOut";
        accessGenTokenUrl=SERVERHOST+"/payment-access/genToken?appid=APPID&secret=SECRET";
    }
    public static String  getAccessLoGinUrl(){
        return accessLoginTokenUrl;
    }
    public static String getSecretKeyUrl(){
        return accessSecretKeyUrl;
    }
    public static String getAccessLoginAddrUrl(){
        return accessLoginAddrUrl;
    }
    public static String getLogOutUrl(){
        return logOutUrl;
    }
    public static  String SERVERHOST = "http://192.168.163.38:8080";
    //http://192.168.100.148:8080 茶瑞东本地
    //192.168.191.1:8080
    //天虹本地http://192.168.163.38:8080
    //正式 https://hzf.tianhong.cn
    public static  String AppName="/payment-access";
    public static  String CertificalPath="/jiiiiiin.cer";
    //
    public static  String APPID="TIANHONG_APPID";
    //appid
    public static  String SECRET="TIANHONG_SECRET";
    //秘密
    public static  String accessLoginTokenUrl=SERVERHOST+"/payment-access/generateAccessToken?appid=APPID&secret=SECRET";
    //url
    public static  String accessSecretKeyUrl=SERVERHOST+"/payment-access/getAccessSecretKey?encryptUniqueID=ENCRYPTUNIQUEID";
    //keyurl
    public static  String accessLoginAddrUrl=SERVERHOST+"/payment-access/getAccessLoginURI";
    //loginurl
    public static  String logOutUrl=SERVERHOST+"/payment-access/logOut";

    public static  String key="Key";

    public static  String getVerifyCode=SERVERHOST+"/payment-access/getSmsToken";

    public static  String chnl="04";//渠道号

    public static  String org_code="00195";//机构编号

    public static  String teller="001";//操作员号

    public static  String accessGenTokenUrl=SERVERHOST+"/payment-access/genToken?appid=APPID&secret=SECRET";

    public static String HELP_CENTER="http://api.honglingjin.cn/v2.1/hzf";

    public static String TH_AGREEMENT="http://api.honglingjin.cn/v2.1/hzf/safety";

}
