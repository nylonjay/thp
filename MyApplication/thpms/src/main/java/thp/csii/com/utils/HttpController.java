package thp.csii.com.utils;

import android.content.Context;
import android.os.Build;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.com.csii.mobile.http.volley.AuthFailureError;
import cn.com.csii.mobile.http.volley.DefaultRetryPolicy;
import cn.com.csii.mobile.http.volley.NetworkResponse;
import cn.com.csii.mobile.http.volley.RequestQueue;
import cn.com.csii.mobile.http.volley.Response;
import cn.com.csii.mobile.http.volley.VolleyError;
import cn.com.csii.mobile.volley.request.StringRequest;
import cn.com.csii.mobile.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/11/18.
 */
public class HttpController extends HttpControl {
    private Context mContext;
    private Map<String, String> httpHeaders = null;
    private RequestQueue requestQueue;
    public static List<X509Certificate> certificateList = new ArrayList();
    public static List<String> fingerprintList = new ArrayList();
    public HttpController(Context context) {
        super(context);
        this.mContext=context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
    }
    public void HttpExcute(final String url, final int requestType, final Map<String, String> map, final ResultInterface resultInterface) {
        LogUtil.d(this.mContext, "http数据请求开始！");
        LogUtil.d(this.mContext, "http数据请求URL=" + url);
        SsX509TrustManager.allowAllSSL();
        StringRequest sRequest = new StringRequest(requestType, url, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                String response=o.toString();
                LogUtil.d(HttpController.this.mContext, "http数据请求结束！");
                LogUtil.d(HttpController.this.mContext, "请求结果：" + response);
                resultInterface.onSuccess(response);
            }


        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                LogUtil.d(HttpController.this.mContext, "http数据请求错误！");
                LogUtil.e(HttpController.this.mContext, "错误信息：" + arg0.getMessage());
                if(arg0 != null && arg0.getMessage() != null) {
                    resultInterface.onError(arg0.getMessage());
                } else {
                    resultInterface.onError("服务器通信异常,请稍后重试!");
                }

            }
        }) {
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response superResponse = super.parseNetworkResponse(response);
                Map responseHeaders = response.headers;
                String rawCookies = (String)responseHeaders.get("Set-Cookie");
                LogUtil.d(HttpController.this.mContext, "服务器返回headers：" + responseHeaders);
                LogUtil.d(HttpController.this.mContext, "服务器返回cookie：" + rawCookies);
                if(rawCookies != null && rawCookies.startsWith("JSESSIONID")) {
                    String part1 = HttpController.this.substring(rawCookies, "", ";");
                    if(Build.VERSION.SDK_INT < 9) {
                        HttpController.cookies = part1;
                    }

                    LogUtil.d(HttpController.this.mContext, "cookies：" + HttpController.cookies);
                } else {
                    LogUtil.d(HttpController.this.mContext, "cookies为空！");
                }

                return superResponse;
            }

            protected Map<String, String> getParams() {
                if(map == null) {
                    HashMap params = new HashMap();
                    LogUtil.d(HttpController.this.mContext, "http请求参数：" + params);
                    return params;
                } else {
                    LogUtil.d(HttpController.this.mContext, "http请求参数：" + map);
                    return map;
                }
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                if(HttpController.cookies != null && HttpController.cookies.length() > 0) {
                    headers.put("Cookie", HttpController.cookies);
                }

                if(HttpController.this.httpHeaders != null) {
                    Iterator var3 = HttpController.this.httpHeaders.keySet().iterator();

                    while(var3.hasNext()) {
                        String key = (String)var3.next();
                        headers.put(key, (String)HttpController.this.httpHeaders.get(key));
                    }
                }

                LogUtil.d(HttpController.this.mContext, "http请求headers：" + headers);
                return headers;
            }
        };

        try {
            LogUtil.d(this.mContext, "sRequest请求headers：" + sRequest.getHeaders());
        } catch (AuthFailureError var7) {
            ;
        }

        sRequest.setShouldCache(false);
        sRequest.setRetryPolicy(new DefaultRetryPolicy(TimeOut, 0, 1.0F));
        this.requestQueue.add(sRequest);
    }
    private String substring(String src, String fromString, String toString) {
        int fromPos = 0;
        if(fromString != null && fromString.length() > 0) {
            fromPos = src.indexOf(fromString);
            fromPos += fromString.length();
        }

        int toPos = src.indexOf(toString, fromPos);
        return src.substring(fromPos, toPos);
    }
}
