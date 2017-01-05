package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.ShSwitchView;



public class Pay_SettingActivity extends BaseActivity implements View.OnClickListener{
    ShSwitchView shswitchview1,shswitchview2,shswitchview3;
    TextView tv_hide_string1,tv_hide_string2,tv_hide_string3;
    RelativeLayout mRetoPwdView,re_pf,re_pay,re_day;
    private LinearLayout ll_back;
    private Token token;
    private String pay_hwm,day_hwm;
    private String pf_hwm,pf_flag,pcode_flag,pf_day_hwm;
    boolean DataReady=false;
    private TextView tv_day_hwm,tv_pf_hwm,tv_pay_hwm;
    private String hf_str;
    private String pwd;
    private boolean shsready=false;
    String action;
    /**
     *Pay_SettingActivity  付款设置
     *@author nylon
     * * created at 2016/9/5 15:25
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__setting);
        initViews();
        pwd=getIntent().getStringExtra("pwd");
        setBackView(R.drawable.u194);
        setTitleText(R.string.pay_set);
        showProDialog();
        new Thread(thread).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    Runnable thread = new Runnable() {
        @Override
        public void run() {
            getData();

        }
    };
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 2://查询到了支付设置信息
                    if (pf_flag.equals("0")){
                        shswitchview1.setOn(false);
                        re_pf.setVisibility(View.GONE);
                    }else if (pf_flag.equals("1")){
                        shswitchview1.setOn(true);
                        re_pf.setVisibility(View.VISIBLE);
                        tv_pf_hwm.setText(pf_hwm+"/笔");
                    }
                    hf_str=pf_flag;
                    LogUtil.e(Pay_SettingActivity.this,"第一次获取到的hf_str=="+hf_str);
                    shsready=true;
                    tv_pay_hwm.setText(pay_hwm+"/笔");
                    tv_day_hwm.setText(day_hwm+"/笔");

                    break;
                case 3:
                    if (null!=hf_str&&hf_str.equals("0")){
                        shswitchview1.setOn(false);
                        re_pf.setVisibility(View.GONE);
                    }else if (null!=hf_str&&hf_str.equals("1")){
                        shswitchview1.setOn(true);
                        re_pf.setVisibility(View.VISIBLE);
                        tv_pf_hwm.setText(pf_hwm+"/笔");
                    }
                    tv_pay_hwm.setText(pay_hwm+"/笔");
                    tv_day_hwm.setText(day_hwm+"/笔");
                    //new Thread(thread).start();
                    break;
                case 5:
                    ModifyPaySetting(HttpUrls.modifyPayFunConfirm);
                    break;
            }
        }
    };


    private void QryCountDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                dismissDialog();
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                try {
                    if (null != res) {
                        if (res.getString("status").equals("0000")) {
                            LogUtil.e(Pay_SettingActivity.this,"查询支付状态"+res.toJSONString());
                            JSONObject datamap = res.getJSONObject("dataMap");
                            JSONObject rsvc=datamap.getJSONObject("rsvc");
                            pay_hwm=rsvc.getString("payHwm");
                            day_hwm=rsvc.getString("dayHwm");
                            pf_hwm=rsvc.getString("pfHwm");
                            pf_flag=rsvc.getString("pfFlag");
                            pcode_flag=rsvc.getString("pcodeFlag");
                            pf_day_hwm=rsvc.getString("pfdayHwm");
                            LogUtil.e(Pay_SettingActivity.this,"pay_hwm="+pay_hwm+"day_hwm="+day_hwm+"pf_hwm="+pf_hwm
                                    +"pf_flag=="+pf_flag+"pcodeflag=="+pcode_flag+"pf_dayhwm==="+pf_day_hwm);
                            hand.sendEmptyMessage(2);
                            DataReady=true;
                        } else{
                            ToastUtil.shortNToast(context,res.getString("errmsg"));
                            if ("00013".equals(res.getString("errcode"))){
                                //session过期弹出操作失败弹框
                                //  initSessionOutTime("操作失败"+("00013"));
                                ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                                //  TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished();
                            }

                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onError(Object o) {
                dismissDialog();
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void getData() {
        Map<String, Object> ajaxData = new HashMap();
        User user = new User();
        user.setAcno(TianHongPayMentUtil.currentUser.getAcno());
        PainObj painObj = new PainObj(user, null);
        painObj.setUserSign(TianHongPayMentUtil.userSign);
        //  LogUtil.e(MainActivity.this,"Accno=="+Accno+"userSign==="+userSign);
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            //  ToastUtil.shortToast(context, SharePreferencesUtils.getStringValue(context,"Cookie"));
            Log.i("err", "Cookie缓存===" + SharePreferencesUtils.getStringValue(context, "Cookie"));
            Log.i("err", "SESSIONID==" + SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
//            ajaxData.put("status", STATUS_OK);
//            ajaxData.put("redirectUrl", url);
            hand.sendEmptyMessage(1);
        } catch (Exception e) {
//            ajaxData.put("status", STATUS_ERR);
//            ajaxData.put("errmsg", e.getMessage());
//            hand.sendEmptyMessage(404);
            System.err.println("授权登录发生错误!" + e.getMessage());
        }
    }

    private void initViews() {
        re_pay= (RelativeLayout) findViewById(R.id.re_pay);
        re_day= (RelativeLayout) findViewById(R.id.re_day);
        re_pf= (RelativeLayout) findViewById(R.id.re_pf);
        re_pf.setOnClickListener(this);
        re_pay.setOnClickListener(this);
        re_day.setOnClickListener(this);
        tv_day_hwm= (TextView) findViewById(R.id.tv_day_hwm);
        tv_pf_hwm= (TextView) findViewById(R.id.tv_pay_hf);
        tv_pay_hwm= (TextView) findViewById(R.id.tv_pay_hwm);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay_SettingActivity.this.finish();
            }
        });
        tvBasetitleOK.setOnClickListener(this);
        shswitchview1 = (ShSwitchView) findViewById(R.id.shsview);
        tv_hide_string1 = (TextView) findViewById(R.id.tv_hide_string1);
        tv_hide_string2 = (TextView) findViewById(R.id.tv_hide_string2);
        tv_hide_string3 = (TextView) findViewById(R.id.tv_hide_string3);
        shswitchview1.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                String aa=hf_str;

                if (isOn) {
                    tv_hide_string1.setVisibility(View.VISIBLE);
                    re_pf.setVisibility(View.VISIBLE);
                    if (null==pf_hwm){
                        pf_hwm="300.00";
                    }
                    tv_pf_hwm.setText(pf_hwm);
                } else {
                    tv_hide_string1.setVisibility(View.GONE);
                    re_pf.setVisibility(View.GONE);
                }
                LogUtil.e(Pay_SettingActivity.this,"onswitch");
                if (null!=hf_str&&shsready){
                    LogUtil.e(Pay_SettingActivity.this,"更改之前的值为:"+aa);
                    // LogUtil.e(Pay_SettingActivity.this,"更改之后的值:"+hf_str);
                    action="switch";
                    showDialog(true);
                    new Thread(sendable).start();
                }

            }
        });
    }

    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token=null;
                token=getAccessGenToken(hand);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private final PayConfig payConfig=PayConfig.newInstance();
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
    private void ModifyPaySetting(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        LogUtil.e(Pay_SettingActivity.this,"hf_st=="+hf_str);
        param.put("pin_data",pwd);
        if (action.equals("switch")){
            param.put("pf_flag",Math.abs(Integer.parseInt(pf_flag)-1)+"");
        }else{
            param.put("pf_flag",pf_flag);
        }
        param.put("day_hwm",day_hwm);
        param.put("pay_hwm",pay_hwm);
        param.put("pf_hwm",pf_hwm);
        param.put("resToken",token.getUniqueId());
        if(null!=pf_day_hwm){
            param.put("pf_day_hwm",pf_day_hwm);
        }else {
            param.put("pf_day_hwm","500.00");
        }
        param.put("pcode_flag",pcode_flag);
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res){
                    if ("0000".equals(res.getString("status"))){
                        JSONObject dataMap=res.getJSONObject("dataMap");
                        JSONObject rsvc=dataMap.getJSONObject("rsvc");
                        if (null!=rsvc){
                            pay_hwm=rsvc.getString("payHwm");
                            day_hwm=rsvc.getString("dayHwm");
                            pf_hwm=rsvc.getString("pfHwm");
                            pf_flag=rsvc.getString("pfFlag");
                            pcode_flag=rsvc.getString("pcodeFlag");
                            pf_day_hwm=rsvc.getString("pfdayHwm");
                            hf_str=pf_flag;
                            LogUtil.e(Pay_SettingActivity.this,"modify:"+"pay_hwm="+pay_hwm+"day_hwm="+day_hwm+"pf_hwm="+pf_hwm
                                    +"pf_flag=="+pf_flag+"pcodeflag=="+pcode_flag+"pf_dayhwm==="+pf_day_hwm);
                            LogUtil.e(Pay_SettingActivity.this,"修改成功以后hf_str=="+hf_str);
                        }
                        hand.sendEmptyMessage(3);
                    }else{
                        if ("55".equals(res.getString("errcode"))){
                            //  initChanceDialog(dataMap.getString("pinRetry"));
                        }
                        if ("36".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(Pay_SettingActivity.this,res.getString("errmsg"));
                        }
                        if ("05".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(Pay_SettingActivity.this,res.getString("errmsg"));
                        }
                    }
                }
            }
            @Override
            public void onError(Object o) {
                showDialog(false);
                Log.i("res err", "" + o.toString());
            }


        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent in;
        if (i == R.id.re_pf) {
            in=new Intent(Pay_SettingActivity.this,SelectHwmActivity.class);
            in.putExtra("postion",0);
            in.putExtra("hwm",pf_hwm);
            startActivityForResult(in,101);
        }else if (i==R.id.re_pay){
            in=new Intent(Pay_SettingActivity.this,SelectHwmActivity.class);
            in.putExtra("postion",1);
            in.putExtra("hwm",pay_hwm);
            startActivityForResult(in,102);
        }else if (i==R.id.re_day){
            in=new Intent(Pay_SettingActivity.this,SelectHwmActivity.class);
            in.putExtra("postion",2);
            in.putExtra("hwm",day_hwm);
            startActivityForResult(in,103);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        //  ToastUtil.shortNToast(Pay_SettingActivity.this,"requesCode=="+requestCode);
        LogUtil.e(Pay_SettingActivity.this,"onactivityresult"+"resultcode=="+resultCode+"requestcode=="+requestCode);
        if (resultCode==RESULT_OK){
            action="result";
            ToastUtil.shortNToast(Pay_SettingActivity.this,"requesCode=="+requestCode);
            switch (requestCode){
                case 101:
                    pf_hwm=data.getStringExtra("hwm");
                    new Thread(sendable).start();
                    break;
                case 102:
                    pay_hwm=data.getStringExtra("hwm");
                    new Thread(sendable).start();
                    break;
                case 103:
                    day_hwm=data.getStringExtra("hwm");
                    new Thread(sendable).start();
                    break;
                default:
                    break;
            }
        }
    }
}
