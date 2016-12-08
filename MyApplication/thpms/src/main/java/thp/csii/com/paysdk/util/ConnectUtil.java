/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import thp.csii.com.BaseActivity;
import thp.csii.com.MyApp;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.utils.SharePreferencesUtils;

/**
 * Created by zhaojin on 15/5/8.
 */
public class ConnectUtil {



    public static final String POST = "POST";

    public static final String GET = "GET";

    /**
     * httpClient的get请求方式
     *
     * @return
     * @throws Exception
     */
    public static JSONObject doGet(String url, String charset)
            throws Exception {

        JSONObject jsonObject = null;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        GetMethod getMethod = new GetMethod(url);
        // Header header = new Header("Accept", "application/json");
           //getMethod.addRequestHeader("Content-Type","text/html;charset=utf-8");
        getMethod.addRequestHeader("Accept","application/json");
        if (null!=SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext)){
            getMethod.addRequestHeader("Cookie",SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        }
      //  getMethod.addRequestHeader("Cookie", SharePreferencesUtils.getStringValue(MyApp.getInstance(), "Cookie").split(";")[0].split("=")[1]);
        // getMethod.setRequestHeader(header);
//        getMethod.addRequestHeader("Cookie",Comm);
        // 设置 get 请求超时为 5 秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + getMethod.getStatusLine());
            }
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h : headers){
                 System.out.println("gentoken:"+h.getName() + "------------ " + h.getValue());
                if (h.getName().equals("Set-Cookie")){
//                    SharePreferencesUtils.save(MyApp.getInstance(),"Cookie",h.getValue());
                    android.util.Log.i("res","doget返回了setCookie");
                  //  BaseActivity.sessionID=SharePreferencesUtils.getStringValue(MyApp.getInstance(), "Cookie").split(";")[0].split("=")[1];
                }
            }
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
            // InputStream response = getMethod.getResponseBodyAsStream();
            response = new String(responseBody, charset);
            System.out.println("----------response str:" + response);
            jsonObject = JSON.parseObject(response);
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            System.out.println("发生网络异常!");

            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return jsonObject;
    }


    /**
     * httpClient的post请求方式
     *
     * @return
     * @throws Exception
     */
    public static JSONObject doPost(String url, NameValuePair[] params, String charset)
            throws Exception {

        JSONObject jsonObject = null;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
        PostMethod postMethod = new PostMethod(url);
        // 把参数值放入postMethod中
        postMethod.setRequestBody(params);
        //  Header header = new Header("Accept", "application/json");
        postMethod.addRequestHeader("Accept","application/json");
       // postMethod.addRequestHeader("Cookie",SharePreferencesUtils.getStringValue(MyApp.getInstance(), "Cookie").split(";")[0].split("=")[1]);
        //  postMethod.setRequestHeader(header);
        // 设置 get 请求超时为 5 秒
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + postMethod.getStatusLine());
            }
            Header[] headers = postMethod.getResponseHeaders();
            for (Header h : headers){
                android.util.Log.i("header",""+h.getName()+"===="+h.getValue());
                 System.out.println(h.getName() + "------------ " + h.getValue());
                if (h.getName().equals("Set-Cookie")){
                    SharePreferencesUtils.save(TianHongPayMentUtil.CurrentContext,"Cookie",h.getValue());
                    android.util.Log.i("res","dopost保存Cookie成功");
//                    String aa=SharePreferencesUtils.getStringValue(MyApp.getInstance(),"Cookie").split("=")[1];
//                    String sess=aa.split(";")[0];
                   // TianHongPayMentUtil.SESSIONID=SharePreferencesUtils.getStringValue(TianHongPayMentUtil.CurrentContext,"Cookie").split(";")[0];

                }
            }
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = postMethod.getResponseBody();// 读取为字节数组
            // InputStream response = postMethod.getResponseBodyAsStream();
            response = new String(responseBody, charset);
            System.out.println("----------response str:" + response);
            jsonObject = JSON.parseObject(response);
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        try {
            ConnectUtil.doGet("http://www.baidu.com/","utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
