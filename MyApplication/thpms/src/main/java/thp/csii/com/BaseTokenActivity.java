package thp.csii.com;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;

public class BaseTokenActivity extends BaseActivity {
    private final PayConfig payConfig=PayConfig.newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void DotherThings(){

    }
    protected Token getAccessGenToken(Handler hand) throws Exception {
        Token token;
        try {
            String requestUrl = payConfig.getAccessGenTokenUrl().replace("APPID", payConfig.getAppId()).replace("SECRET", payConfig.getAppSecret());
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
                JSONObject accessToken = res.getJSONObject("dataMap").getJSONObject("resubmitToken");
                token = new TokenImpl(accessToken.getString("uniqueId"), accessToken.getLong("accessDate"), accessToken.getIntValue("delayTime"));
                hand.sendEmptyMessage(5);
                //   logger.debug("商城获取到支付系统的Token {}", PayConfig.token);
            }

        } catch (Exception e) {
            //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }

        return token;
    }

    protected void GETLoginToken(Handler hand){
        Map<String, Object> ajaxData = new HashMap();
//        User user = new User();
//        Order order= new Order();
//        user.setTel("13556873047");
////        user.setPassword("123457");
//        user.setPassword("342307");
//        user.setAcno(TianHongPayMentUtil.c);
//        order.setOid("65e4wf6weeww16w1w65");
//        order.setMid("00103");
//        order.setAmount(1.00);
//        order.setAccno("7000290210000032307");
        //user.setPin_tag("");
        PainObj painObj = new PainObj(TianHongPayMentUtil.currentUser,TianHongPayMentUtil.currentOder);
        //end
        painObj.setUserSign(TianHongPayMentUtil.userSign);
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
//            TianHongPayMentUtil.currentOder=order;
//            TianHongPayMentUtil.currentUser=user;
            hand.sendEmptyMessage(1);
            //  ToastUtil.shortToast(context, SharePreferencesUtils.getStringValue(context,"Cookie"));
        } catch (Exception e) {
            System.err.println("授权登录发生错误!" + e.getMessage());
        }
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
