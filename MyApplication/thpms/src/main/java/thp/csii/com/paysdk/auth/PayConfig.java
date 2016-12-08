/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.auth;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Properties;
import java.util.ResourceBundle;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.paysdk.entity.Token;

import static thp.csii.com.paysdk.secure.CertificateCoder.CERT_TYPE;

/**
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-13-22:07
 */

public class PayConfig {

   // private static final Logger logger = LoggerFactory.getLogger(PaySDK.class);

    private static PayConfig payConfig;
    private String certificatePath;
    private PublicKey publicKey;
    private String appId;
    private String appSecret;
    private String accessLoginTokenUrl;
    private String accessSecretKeyUrl;
    private String accessLoginAddrUrl;
    private String key;
    private String serverHost;
    private String logOutUrl;

    public String getLogOutUrl() {
        return logOutUrl;
    }

    public void setLogOutUrl(String logOutUrl) {
        this.logOutUrl = logOutUrl;
    }

    public static Token token;

    private PayConfig() {
    }

    public static PayConfig newInstance(String configPath) {
        if (payConfig == null) {
            payConfig = new PayConfig();
            init(payConfig,configPath);
        }
        return payConfig;
    }

    public static PayConfig newInstance(ResourceBundle rb) {
        if (payConfig == null) {
            payConfig = new PayConfig();
            init(rb);
        }
        return payConfig;
    }

    public static PayConfig newInstance() {
        if (payConfig == null) {
            payConfig = new PayConfig();
            init();
        }
        return payConfig;
    }

    private static void init(PayConfig payConfig,String configPath) {
        try {
//            String configPath = getClass().getClassLoader().getResource("payment_sdk_config.properties").getPath();
            Properties prop = new Properties();
            prop.load(new FileInputStream(configPath));
          //  logger.debug("PayConfig prop>>"+prop);
            payConfig.setServerHost(prop.getProperty("auth.serverHost"));
            payConfig.setCertificatePath(prop.getProperty("auth.certificatePath"));
            payConfig.setAppId(prop.getProperty("auth.appid"));
            payConfig.setAppSecret(prop.getProperty("auth.secret"));
            payConfig.setAccessLoginTokenUrl(prop.getProperty("auth.accessLoginTokenUrl"));
            payConfig.setAccessSecretKeyUrl(prop.getProperty("auth.accessSecretKeyUrl"));
            payConfig.setAccessLoginAddrUrl(prop.getProperty("auth.accessLoginAddrUrl"));
            payConfig.setKey(prop.getProperty("auth.key"));
            payConfig.setPublicKey(prop.getProperty("auth.certificatePath"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void init(ResourceBundle rb) {
        payConfig.setServerHost(rb.getString("auth.serverHost"));
        payConfig.setCertificatePath(rb.getString("auth.certificatePath"));
        payConfig.setAppId(rb.getString("auth.appid"));
        payConfig.setAppSecret(rb.getString("auth.secret"));
        payConfig.setAccessLoginTokenUrl(rb.getString("auth.accessLoginTokenUrl"));
        payConfig.setAccessSecretKeyUrl(rb.getString("auth.accessSecretKeyUrl"));
        payConfig.setAccessLoginAddrUrl(rb.getString("auth.accessLoginAddrUrl"));
        payConfig.setKey(rb.getString("auth.key"));
        payConfig.setPublicKey(rb.getString("auth.certificatePath"));
    }

    private static void init() {
       // LogUtil.e(TianHongPayMentUtil.CurrentContext,"SERVEHOST==="+Constant.SERVERHOST);
        payConfig.setServerHost(Constant.SERVERHOST);
        payConfig.setCertificatePath(Constant.CertificalPath);
        payConfig.setAppId(Constant.APPID);
        payConfig.setAppSecret(Constant.SECRET);
        payConfig.setAccessLoginTokenUrl(Constant.accessLoginTokenUrl);
        payConfig.setAccessSecretKeyUrl(Constant.accessSecretKeyUrl);
        payConfig.setAccessLoginAddrUrl(Constant.accessLoginAddrUrl);
        payConfig.setKey(Constant.key);
        payConfig.setPublicKey(Constant.CertificalPath);
        payConfig.setLogOutUrl(Constant.logOutUrl);
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    public String getAccessGenTokenUrl(){
        return Constant.accessGenTokenUrl;
    }

    public String getAccessLoginTokenUrl() {
        return accessLoginTokenUrl;
    }

    public void setAccessLoginTokenUrl(String accessLoginTokenUrl) {
        this.accessLoginTokenUrl = accessLoginTokenUrl.replace("SERVER_HOST",this.serverHost);
    }

    public String getAccessSecretKeyUrl() {
        return accessSecretKeyUrl;
    }

    public void setAccessSecretKeyUrl(String accessSecretKeyUrl) {
        this.accessSecretKeyUrl = accessSecretKeyUrl.replace("SERVER_HOST",this.serverHost);
    }

    public String getAccessLoginAddrUrl() {
        return accessLoginAddrUrl;
    }

    public void setAccessLoginAddrUrl(String accessLoginAddrUrl) {
        this.accessLoginAddrUrl = accessLoginAddrUrl.replace("SERVER_HOST",this.serverHost);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        PayConfig.token = token;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String  certificatePath) {
        CertificateFactory certificateFactory = null;
        InputStream in = null;
        try {
            // 实例化证书工厂
            certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
            // 取得证书文件流
            in = PayConfig.class.getResourceAsStream(certificatePath);
            // 生成证书
            Certificate certificate = certificateFactory.generateCertificate(in);
            //获取公钥
            publicKey = certificate.getPublicKey();
            // 关闭证书文件流
            in.close();
        } catch (CertificateException e) {
            e.printStackTrace();
       //     logger.debug("证书实例化失败,{}",e.getMessage());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        //    logger.debug("证书文件读取失败,{}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
         //   logger.debug("证书文件读取失败,{}",e.getMessage());
        } finally {
            try {
                // 关闭证书文件流
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
