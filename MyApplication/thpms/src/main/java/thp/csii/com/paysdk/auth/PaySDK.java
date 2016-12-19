/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.auth;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;

import java.io.*;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.MyApp;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.activities.MainActivity;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.exception.PaymentSDKException;
import thp.csii.com.paysdk.secure.CertificateCoder;
import thp.csii.com.paysdk.secure.DESedeCoder;

import thp.csii.com.paysdk.util.CommonUtil;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.paysdk.util.JsonUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

/**
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-13-12:02
 */

public class PaySDK {
    //public static String uniqueeID;

//    private static final Logger logger = LoggerFactory.getLogger(PaySDK.class);
    private final PayConfig payConfig;

    public PaySDK() {
        //1.读取指定绝对目录文件
        //ResourceBundle rb = ResourceBundle.getBundle("payment_sdk_config", Locale.getDefault());
        //this.payConfig = PayConfig.newInstance(rb);

        //2.可以读取当前项目中文件(未打成jar)
//        String configPath = getClass().getClassLoader().getResource("payment_sdk_config").getPath();
        //payConfig = PayConfig.newInstance(configPath);

        //3.可以读取jar中文件
        this.payConfig = PayConfig.newInstance();
    }

    @SuppressWarnings("Since15")
    public static Properties ReadJarFiProperties(String filePath) {
        Properties props = new Properties();
        try {
            InputStream ips = PaySDK.class.getResourceAsStream(filePath);
            BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
            props.load(ipss);
            //String value = props.getProperty(key);
            return props;
        } catch (FileNotFoundException e) {
            System.out.println("无法找到文件:"+filePath);
            return null;
        } catch (IOException e) {
            System.out.println("读文件出错:"+filePath+"---"+e.getMessage());
            return null;
        }
    }

    public String getAccessLoginURI(PainObj painObj) throws Exception {
        String url = null;
        Token token = getAccessLoginToken();
        //商城对请求得到的token进行RSA公钥加密,以向支付前置请求密匙:
        try {
            String uniqueId = token.getUniqueId();
            System.err.println("uniqueId===="+uniqueId);
           // uniqueeID=uniqueId;
            // byte[] uniqueIdEncrypt = CertificateCoder.encryptByPublicKey(uniqueId.getBytes(), payConfig.getCertificatePath());
            //改为传入公钥，不传传入路径，用户可以不用关心jiiiin.cer的路径
            byte[] uniqueIdEncrypt = CertificateCoder.encryptByPublicKey(uniqueId.getBytes(), payConfig.getPublicKey());
            String encryptUniqueID = CommonUtil.encodeHexString(uniqueIdEncrypt);
       //     logger.debug("商城对请求得到的token进行RSA加密,以向支付前置请求密匙: uniqueId:{} plainByteArr:{} uniqueIdEncrypt:{}", uniqueId, encryptUniqueID);
            String requestKeyUrl = payConfig.getAccessSecretKeyUrl().replace("ENCRYPTUNIQUEID", encryptUniqueID);
            LogUtil.e(TianHongPayMentUtil.CurrentContext,"请求秘钥路径=="+requestKeyUrl);
            JSONObject keyjson = ConnectUtil.doGet(requestKeyUrl, "utf-8");
            if (null == keyjson) {
                throw new Exception("获取授权登录加密密钥失败!");
            }
            JSONObject res = keyjson.getJSONObject("res");
            if (null == res) {
                throw new Exception("获取授权登录加密密钥失败!");
            }
            String status = res.getString("status");
            if (status.equals("4444")) {
             //   logger.error("支付平台错误提示:" + res);
                throw new Exception( res.getString("errcode") + " " + res.getString("errmsg"));
            } else {
                String encodedKeyStr = res.getJSONObject("dataMap").getString(payConfig.getKey());
                //解密得到密钥
                //byte[] desKey = CertificateCoder.decryptByPublicKey(CommonUtil.decodeHex(encodedKeyStr), payConfig.getCertificatePath());
                //改为传入公钥，不传传入路径，用户可以不用关心jiiiin.cer的路径
                byte[] desKey = CertificateCoder.decryptByPublicKey(CommonUtil.decodeHex(encodedKeyStr),  payConfig.getPublicKey());
                String desKeyStr = CommonUtil.encodeHexString(desKey);
             //   logger.debug("商城得到的加密密钥:{} hexkey:{}", encodedKeyStr, desKeyStr);
                // 商城加密用户数据
                String painStr = JsonUtil.obj2json(painObj);
                LogUtil.e(TianHongPayMentUtil.CurrentContext,"传进去的painstr==="+painStr);
                //  logger.debug("用户提交pain:{} ", painStr);
                String inputStr = new StringBuffer(painStr).toString();
                // 加密
                byte[] userEncrypt = DESedeCoder.encrypt(inputStr.getBytes(), desKey);
                String userEncryptData = CommonUtil.encodeHexString(userEncrypt);
            //    logger.error("商城加密用户信息后传递给支付前置:{}", userEncryptData);
                //组织参数
                String getaccessloginurl = payConfig.getAccessLoginAddrUrl();
                NameValuePair[] params = { new NameValuePair("userEncryptData",userEncryptData)};
                JSONObject jsonurl = ConnectUtil.doPost(getaccessloginurl, params,"utf-8");

                if (null == jsonurl) {
                    if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("获取登录授权加密失败");
                    }
                    throw new Exception("获取授权登录加密密钥失败!");
                }
                JSONObject res2 = jsonurl.getJSONObject("res");
                if (null == res2) {
                    if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("获取登录授权加密失败");
                    }
                    throw new Exception("获取授权登录加密密钥失败!");
                }
                String status2 = res2.getString("status");
                if (status2.equals("4444")) {
                    if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("获取登录授权加密失败");
                    }
                    throw new Exception("获取授权登录加密密钥失败:" + res2.getString("errcode") + " " + res2.getString("errmsg"));
                } else {
                    url = res2.getJSONObject("dataMap").getString("redirectUrl");
                    LogUtil.e(TianHongPayMentUtil.CurrentContext,"登录url=="+url);
                    //SharePreferencesUtils.save(MyApp.getInstance(),"tel",painObj.getUser().getTel());
                }
            }

        } catch (Exception e) {
            if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("获取登录授权加密失败");
            }
            e.printStackTrace();
            throw new PaymentSDKException(e.getMessage(), e);
        }
        return url;
    }

    public void logOut() throws Exception{
        String getlogOuturl = payConfig.getLogOutUrl();
        System.out.println(getlogOuturl + "-----退出的地址");
        NameValuePair[] params = { new NameValuePair("userEncryptData","123")};
        JSONObject jsonurl = ConnectUtil.doPost(getlogOuturl,params ,"utf-8");
        if (null == jsonurl) {
            throw new Exception("退出失败!");
        }
        JSONObject res2 = jsonurl.getJSONObject("res");
        if (null == res2) {
            throw new Exception("没有找到res结果集");
        }
        String status2 = res2.getString("status");
        if (status2.equals("4444")) {
            throw new Exception("退出失败:" + res2.getString("errcode") + " " + res2.getString("errmsg"));
        }
    }

    public Token getAccessLoginToken() throws Exception {
        try {
            if (true||PayConfig.token == null || checkTokenExpiresIn(PayConfig.token.getDelayTime(), new Date(PayConfig.token.getAccessDate()))) {
               System.out.print("开启请求Token");
                String requestUrl = payConfig.getAccessLoginTokenUrl().replace("APPID", payConfig.getAppId()).replace("SECRET", payConfig.getAppSecret());
                LogUtil.e(TianHongPayMentUtil.CurrentContext,"正式包的AccessLoginUrl==="+payConfig.getAccessLoginTokenUrl());
                JSONObject json = ConnectUtil.doGet(requestUrl, "utf-8");
                if (null == json) {
                    throw new Exception("获取授权登录Token凭证失败!");
                }
                JSONObject res = json.getJSONObject("res");
                if (null == res) {
                    throw new Exception("获取授权登录Token凭证失败!");
                }
                String status = res.getString("status");
                if (status.equals("4444")) {
                    throw new Exception("获取授权登录Token凭证失败!" + res.getString("errcode") + res.getString("errmsg"));
                } else {
                    JSONObject accessToken = res.getJSONObject("dataMap").getJSONObject("accessToken");
                    PayConfig.token = new TokenImpl(accessToken.getString("uniqueId"), accessToken.getLong("accessDate"), accessToken.getIntValue("delayTime"));
                   // Log.e("商城获取到支付系统的Token {}"+ PayConfig.token);
                    System.out.print("token=="+PayConfig.token);
                }
            }
        } catch (Exception e) {
          //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }
        return PayConfig.token;
    }

    private static boolean checkTokenExpiresIn(long expiresIn, Date getDaeTime) {
        long tmp = expiresIn * 1000;
        Date currDate = new Date();
        if ((currDate.getTime() - getDaeTime.getTime()) < tmp) {
           // logger.info("token没有失效!");
            return false;
        } else {
            return true;
        }
    }


}
