package thp.csii.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import org.apache.commons.codec.DecoderException;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import thp.csii.com.activities.MainActivity;
import thp.csii.com.activities.MessageAuthActivity;
import thp.csii.com.activities.PayConfirmActivity;
import thp.csii.com.activities.SetPayCode_First_Activity;
import thp.csii.com.callback.BindCardCallBack;
import thp.csii.com.callback.EditChangedListener;
import thp.csii.com.callback.OnMainActivityFinished;
import thp.csii.com.callback.PayOrderListener;
import thp.csii.com.callback.QryAmountListner;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.CommonUtil;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.AES;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.THProgressDialog;

/**
 * Created by Administrator on 2016/10/20. 外接APP工具类
 */
public class TianHongPayMentUtil {
    // private Order order;
    private final PayConfig payConfig=PayConfig.newInstance();
    //private User user;
    private String pinOpen;//是否设置支付密码
    private String pinNeed;//是否需要支付密码
    private Token token;
    public PayOrderListener mPayOrderListener;
    public QryAmountListner mQryAmountListner;
    public static Context CurrentContext;
    public static Order currentOder;
    public static User currentUser;
    public static String currentTel;
    public static boolean CodeSetted=false;
    public static String userSign="";
    public static String consumeSign="";
    private String action;
    public static final String PUBLICKEY="80fda66cbe589be5f3b1056f0b982ac98d7fa54e5e879d99dfbba66b6d136576be9794b478f8b244082694134433a2e58e4fd94553645aae71c4609305e40fb90e855f5266ebd30f434fb550b3900eb002e3a3b27fad76d42cce72751f335c95392b7d1fea23675f565cf1e6f9a11ed258dc01c03f9bd270347cfebc52fd6d6f";
    public static TianHongPayMentUtil tianHongPayMentUtil;
    public static ArrayList<Activity> activities=new ArrayList<Activity>();
    public static ArrayList<Activity> spcactivities=new ArrayList<Activity>();
    public static ArrayList<Activity> pwdactivities=new ArrayList<Activity>();
    public static THProgressDialog thProgressDialog;
    public  BindCardCallBack bindCardCallBack;
    public  OnMainActivityFinished onMainActivityFinished;
    private String otid;

    public void JumpTOMainActivity(){
        Intent in=new Intent(CurrentContext,MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra("Accno",currentUser.getAcno());
        in.putExtra("userSign",TianHongPayMentUtil.userSign);
        LogUtil.e(TianHongPayMentUtil.CurrentContext,"userSign=="+TianHongPayMentUtil.userSign);
        CurrentContext.startActivity(in);
    }

    public TianHongPayMentUtil(Context context) {
        CurrentContext=context;
        //  this.CurrentContext.registerReceiver(new MyBroadcastReceiver());
    }
    public static TianHongPayMentUtil getInstance(Context context){

        CurrentContext=context;
        thProgressDialog=THProgressDialog.createDialog(context);
        if (null==tianHongPayMentUtil){
            tianHongPayMentUtil=new TianHongPayMentUtil(context);
        }
        return tianHongPayMentUtil;
    };

    public void setUO(User u,Order o,PayOrderListener payOrderListener){
        currentUser=u;
        currentOder=o;
        mPayOrderListener=payOrderListener;

    }
    public void setQA(User u,QryAmountListner qryAmountListner){
        currentUser=u;
        mQryAmountListner=qryAmountListner;
    }
    public void toPayPredict(){//开始订单消费
        action = "predict";
        new Thread(sendablea).start();
    }
    public void toQryAcount(){//获取红包余额
        action = "qryacount";
        new Thread(sendablea).start();
    }

    Runnable sendablea = new Runnable() {//支付
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                if (action.equals("qryacount")){
                    GETQryLoginToken(hand);//查询红包余额的授权登录
                }
                else{
                    GETLoginToken(hand);//支付预判的授权登录
                }
            } catch (Exception e) {
                LogUtil.e(TianHongPayMentUtil.CurrentContext,"当前的action=="+action);
                e.printStackTrace();
                Message msg=new Message();
                msg.obj=e.toString();
                msg.what=406;
                hand.sendMessage(msg);


            }
        }
    };
    public void GetHBYE(){

        //获取红包余额接口
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
        //param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + HttpUrls.payFunDetaQry;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                Double balamt;
                Double cardamt = 0.0;
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                NumberFormat nf=NumberFormat.getInstance();
                nf.setGroupingUsed(false);
// 设置数的小数部分所允许的最小位数
                nf.setMinimumFractionDigits(0);
// 设置数的小数部分所允许的最大位数
                nf.setMaximumFractionDigits(2);
                try {
                    if (null != res) {
                        if (res.getString("status").equals("0000")) {
                            JSONObject datamap = res.getJSONObject("dataMap");
                            if (null != datamap) {
                                JSONObject rsvc = datamap.getJSONObject("rsvc");
                                // balamt = Double.parseDouble(rsvc.getString("balAmt"));//账户总余额
                                balamt = nf.parse(rsvc.getString("balAmt")).doubleValue();
                                if (null!=balamt&&balamt!=-1.0){
                                    //查询余额的接口
                                    mQryAmountListner.OnQryAmountHBYESUceed(balamt);
                                }
                                //mPayOrderListener.OnQryAmountHBYESUceed(balamt);
                                JSONObject acclist = rsvc.getJSONObject("accList");
                                if (null != acclist) {
                                    JSONArray account = acclist.getJSONArray("account");
                                    for (int i = 0; i < account.size(); i++) {
                                        //  cardamt += Double.parseDouble(account.getJSONObject(i).getString("balAmt"));
                                        cardamt += nf.parse(account.getJSONObject(i).getString("balAmt")).doubleValue();
                                    }//将所有卡的余额相加
                                }
                            }
                        }else{
                            mQryAmountListner.OnQryAmountHBYEFailed(json.toJSONString());
                            if ("00013".equals(res.getString("errcode"))){
                                //session过期弹出操作失败弹框
                                // initSessionOutTime("操作失败"+("00013"));
                                ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                            }
                            ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));

                        }
                    }else{
                        mQryAmountListner.OnQryAmountHBYEFailed(json.toString());
                    }
                }catch (Exception e){
                    mQryAmountListner.OnQryAmountHBYEFailed(json.toString());
                }
            }
            @Override
            public void onError(Object o) {
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
                mQryAmountListner.OnQryAmountHBYEFailed(o.toString());
            }
        });

    }
    public void GETLoginToken(Handler hand){
        if (null==currentUser||null==currentOder){
            LogUtil.e(CurrentContext,"还未设置用户名和订单号");
            return;
        }
        PainObj painObj = new PainObj(currentUser,currentOder);
        if (consumeSign.isEmpty()||userSign.isEmpty()){
            mPayOrderListener.PushItoApp("缺少参数userSign,consumeSign,无法授权登录");
            return;
        }
        painObj.setConsumeSign(consumeSign);
        painObj.setUserSign(userSign);
        LogUtil.e(CurrentContext,"consumsign=="+consumeSign+"userSign="+userSign);
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            hand.sendEmptyMessage(1);

        } catch (Exception e) {
            if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
               mPayOrderListener.PayFailed("获取登录授权加密失败");
            }
            System.err.println("授权登录发生错误!" + e.getMessage());
         //   mPayOrderListener.OnAcessLoginFailed();
            hand.sendEmptyMessage(404);
        }
    }
    protected void GETQryLoginToken(Handler hand){
        PainObj painObj = new PainObj(currentUser,null);
        painObj.setUserSign(userSign);
        LogUtil.e(CurrentContext,"userSign="+userSign);
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            hand.sendEmptyMessage(20);
        } catch (Exception e) {
            System.err.println("授权登录发生错误!" + e.getMessage());
          //  hand.sendEmptyMessage(404);
            //mPayOrderListener.OnAcessLoginFailed();
            mQryAmountListner.OnQryAmountHBYEFailed("授权错误");
            // hand.sendEmptyMessage(406);

        }
    }
    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5:
                    if (action.equals("qryacount")) {
                        // ValidatePayCode(HttpUrls.trsPwdValidate);
                    } else if (action.equals("beg")) {
                        // BegforpayCode(HttpUrls.getUnlineQrCode);
                    } else if (action.equals("revoke")) {
                        //   OrderRevoke(HttpUrls.orderRevoke);
                    } else if (action.equals("cost")) {
                        if (null!=token){
                            LogUtil.e(CurrentContext,"符合免密条件 开始消费");
                            PayOrders(HttpUrls.oderCounsume);
                        }else{
                            LogUtil.e(CurrentContext,"未获取到交易token");
                        }
                    }
                    break;
                case 0:
                    //支付预判之前的授权登录之后
                    //现在开始查看账户详情情况 是否设置支付密码
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 2:
                    //查询到支付账户详情之后 开始支付预判
                    LogUtil.i(CurrentContext, "开始支付预判");
                    PayPredict(HttpUrls.payPredict);
                    break;
                case 1:
                    //授权登录完毕
                    mPayOrderListener.OnAcessLoginSucced();
                    LogUtil.e(CurrentContext,"授权登录完毕 action=="+action);
                    if (action.equals("cost")) {
                        LogUtil.e(CurrentContext,"授权登录完毕 action=="+action);
                        new Thread(sendable).start();//获取交易Token
                        return;
                    }
                    //如果action.equals qr开始查询账户详情
                    //predict  登录完毕以后开始查询支付账户详情
                    LogUtil.e(CurrentContext,"不符合免密条件，开始账户查询");
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 9:
                    //输入完6个字符
                    action="cost";
                    new Thread(sendable).start();
                    // ValidatePayCode(HttpUrls.trsPwdValidate);
                    //开始验证支付密码
                    break;
                case 404:
                   // ToastUtil.shortToast(CurrentContext,"服务器无响应");
                   // mPayOrderListener.OnNetWorkError();
                    break;
                case 20:
                    if (action.equals("qr")){
                        GetOrderInfoApp(otid);
                        return;
                    }
                    GetHBYE();
                    break;
                case 406:

                    String exc=msg.obj.toString();
                    if (null==exc)
                        exc="无响应信息";
                    if (action.equals("qryacount")){
                        mQryAmountListner.OnQryAmountHBYEFailed(exc);
                    }else if (action.equals("predict")){
                        mPayOrderListener.PayFailed(exc);
                    }
                    break;
            }
        }
    };
    private String chanl,ent_mode,pcode;

    private void GetOrderInfoApp(String otid){
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        //param.put("entMode", "00");
        String url =  Constant.SERVERHOST + Constant.AppName + HttpUrls.getOrderInfoAPP+"?otid"+otid;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res){
                    if (res.getString("status").equals("0000")){
                        LogUtil.e(TianHongPayMentUtil.CurrentContext,res.toJSONString());
                        JSONObject dataMap=res.getJSONObject("dataMap");
                        chanl=dataMap.getString("chanl");
                        pcode=dataMap.getString("pcode");
                        ent_mode=dataMap.getString("ent_mode");
                        String oid=dataMap.getString("oid");
                        String amount=dataMap.getString("trsAmt");
                        Order order=new Order();
                        order.setOid(oid);
                        order.setAmount(Double.parseDouble(amount));
                        TianHongPayMentUtil.currentOder=order;
                        hand.sendEmptyMessage(1);//开始查询账户信息
                        // b.putString("chanl");
                    }else {
                        mPayOrderListener.PayFailed(res.getString("errmsg"));
                    }
                }else {
                    mPayOrderListener.PayFailed("获取订单信息失败");
                }

            }

            @Override
            public void onError(Object o) {
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void PayOrders(String mUrl) {
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        if (from.equals("qr")){
            param.put("entMode",ent_mode);
            param.put("chanl",chanl);
            param.put("pcode",pcode);
        }else {
            param.put("entMode", "00");
            param.put("chanl","01");//主扫传03  线上支付传01
        }
        // param.put("pcode","1008645423131");
        param.put("resToken", token.getUniqueId());

        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie",SharePreferencesUtils.getStringValue(CurrentContext,"Cookie"));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if ("0000".equals(res.getString("status"))) {
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    mPayOrderListener.PaySucced(res.getString("msg"));
                    if ("qr".equals(from)){
                        Intent in = new Intent(CurrentContext,QRPaySuccedActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("amount",dataMap.getString("trsAmt"));
                        CurrentContext.startActivity(in);
                    }
                    // PayConfirmActivity.this.onPaySuccess(res.getString("msg"));
                } else if ("4444".equals(res.getString("status"))) {
                    mPayOrderListener.PayFailed(res.getString("errmsg"));
                    if (res.getString("errcode").equals("00005")){//令牌校验失败
                        ToastUtil.shortToast(CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00013")){//用户会话失效
                        // initSessionOutTime("操作失败"+("00013"));
                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("4444")){
                        ToastUtil.shortToast(CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("A5")){
                        ToastUtil.shortToast(CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("61")){//一次性交易金额过大
                        ToastUtil.shortNToast(CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("51")){//余额不足
                        ToastUtil.shortNToast(CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00047")){//验签失败
                        ToastUtil.shortToast(CurrentContext,res.getString("操作失败"+"("+"errmsg"+")"));
                    }

                    else{//交给APP处理
                        mPayOrderListener.PushItoApp(json.toJSONString());

                    }
                }else{
                    mPayOrderListener.PayFailed(json.toJSONString());
                }
            }
            @Override
            public void onError(Object o) {
                mPayOrderListener.PayFailed(o.toString());
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }
    private void PayPredict(String mUrl) {//支付预判
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        if (action.equals("qr")){
            param.put("entMode",ent_mode);
        }else {
            param.put("entMode", "00");
        }
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                String status=res.getString("status");
                if ("0000".equals(status)) {
                    JSONObject datamap = res.getJSONObject("dataMap");
                    if (null != datamap) {
                        JSONObject rsvc = datamap.getJSONObject("rsvc");
                        if (null != datamap.getString("pinTag")) ;
                        LogUtil.i(CurrentContext, "pinTag==" + rsvc.getString("pinTag"));
                        pinNeed = rsvc.getString("pinTag");
                        if ("01".equals(pinNeed)) {
                            //需要交易密码
                            if ("01".equals(pinOpen)) {
                                //跳转到输入支付密码页面
                                LogUtil.e(CurrentContext, "需要输入密码，跳转到支付订单页面");
                                Intent in = new Intent(CurrentContext, PayConfirmActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra("action", "cost");
                                in.putExtra("chanl", chanl);
                                in.putExtra("entMode", ent_mode);
                                in.putExtra("pcode", pcode);
                                in.putExtra("needpwd",true);
                                CurrentContext.startActivity(in);
                            } else {
                                //未设置支付密码 跳转到设置支付密码页面和短信验证页面
                                Intent in = new Intent(CurrentContext, SetPayCode_First_Activity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra("action", "cost");
                                CurrentContext.startActivity(in);
                                // CurrentContext.startActivity(new Intent(CurrentContext,MessageAuthActivity.class));
                            }
                        } else {
                            //不需要支付密码 直接开始订单消费
                            ToastUtil.shortToast(CurrentContext, "符合免密条件,开始订单消费");
//                            action = "cost";
//                            new Thread(sendable).start();
                            Intent in = new Intent(CurrentContext, PayConfirmActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("action", "cost");
                            in.putExtra("chanl", chanl);
                            in.putExtra("entMode", ent_mode);
                            in.putExtra("pcode", pcode);
                            in.putExtra("needpwd",false);
                            CurrentContext.startActivity(in);
                        }
                    }
                }else {
                    mPayOrderListener.PayFailed(res.getString("errmsg"));
                }
            }
            @Override
            public void onError(Object o) {
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }
    public void GETQR(String msg,PayOrderListener mPayOrderListener){
        final String ms=msg;
        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener=mPayOrderListener;
        Runnable getqr=new Runnable() {
            @Override
            public void run() {
                GetQRSecrectMessage(ms);
            }
        };
        new Thread(getqr).start();

    }
    public static String from="";
    public void GetQRSecrectMessage(String msg){
        String a=msg.substring(msg.indexOf("&"),msg.length());
        otid=a.substring(a.indexOf("="),a.length());
        LogUtil.e(CurrentContext,"otid==="+otid);
        action="qr";
        from="qr";
        GETQryLoginToken(hand);

    }




    private void QryCountDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
        //param.put("resToken",token.getUniqueId());
        String url = Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if (null != res) {
                    if ("0000".equals(res.getString("status"))){
                        JSONObject datamap = res.getJSONObject("dataMap");
                        if (null != datamap) {
                            JSONObject rsvc = datamap.getJSONObject("rsvc");
                            pinOpen = rsvc.getString("pinTag");
                            TianHongPayMentUtil.currentTel = rsvc.getString("mobile");
                            LogUtil.e(CurrentContext, "pinOpen===" + pinOpen);

                            PayPredict(HttpUrls.payPredict);
                        }
                    }else{
                        if ("05".equals(res.getString("errcode"))){
                            TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(res.getString("errmsg"));
                        }
                        if ("00013".equals(res.getString("errcode"))){
                            //session过期弹出操作失败弹框
                            ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                            //  initSessionOutTime("操作失败"+("00013"));

                        }
                    }
                }
            }
            @Override
            public void onError(Object o) {
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }

    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token = null;
                token = getAccessGenToken();
                LogUtil.e(CurrentContext, "新的restoken==" + token.getUniqueId());
                hand.sendEmptyMessage(5);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    protected Token getAccessGenToken() throws Exception {//获取交易Token
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

                Log.i("new_token",""+token.getUniqueId());
                //   logger.debug("商城获取到支付系统的Token {}", PayConfig.token);
            }

        } catch (Exception e) {
            //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }

        return token;
    }

    public void LogOut() {
        HttpControl httpControl = new HttpControl(CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("entMode", "00");
//        // param.put("pcode","1008645423131");
//        param.put("resToken", token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName +"/logOut";
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if ("0000".equals(res.getString("status"))) {
                    ToastUtil.shortToast(CurrentContext,"登出成功");
                    SharePreferencesUtils.cleanUpData(TianHongPayMentUtil.CurrentContext);
                    TianHongPayMentUtil.currentOder=null;
                    TianHongPayMentUtil.currentUser=null;
                }
            }
            @Override
            public void onError(Object o) {
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }


        });
    }

    //跳转到主页面登录授权+查询账户信息的步骤:

}
