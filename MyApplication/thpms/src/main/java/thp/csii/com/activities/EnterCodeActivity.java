package thp.csii.com.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.OnPasswordInputFinish;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.PasswordView;

/**
 *EnterCodeActivity 如果用户没有开启请求付款码，在此页面输入密码然后跳转到付款码设置页面
 *@author nylon
 * created at 2016/8/31 11:48
 */
public class EnterCodeActivity extends BaseTokenActivity implements View.OnClickListener{
    PEEditText true_peed;
    Button submit;
    String message;
    Token token;
    private LinearLayout ll_tvs;
    private LinearLayout ll_back;
    private TextView[] peds;
    private TextView pe3;
    private TextView pe2;
    private TextView pe1;
    private TextView pe4;
    private TextView pe5;
    private TextView pe6;
    private String action="validate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        setTitleText(R.string.inputcode);
        //  setBackView(R.drawable.u194);
        message=getIntent().getStringExtra("message");
        TianHongPayMentUtil.pwdactivities.add(this);
        initViews();

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 5://获取到交易token以后
                    LogUtil.e(context,"开始验证支付密码");
                    if (action.equals("validate")){
                        ValidatePayCode(HttpUrls.trsPwdValidate);
                    }else if (action.equals("open")){
                        OpenQRPay(HttpUrls.ModifyPayFunConfirm);
                    }
                    break;
                case 1://验证成功
                    action="open";
                    new Thread(sendable).start();
                    //EnterCodeActivity.this.finish();
                   // startActivity(new Intent(EnterCodeActivity.this,Pay_Set_Pre.class));
                    break;
                case 2://开启付款码支付成功
                    LogUtil.e(EnterCodeActivity.this,"开启付款码支付成功");
                    LogUtil.e("pfhwm","开启的时候pfhwm=="+TianHongPayMentUtil.CurrentPf_Hwm);
                    Intent in=new Intent(EnterCodeActivity.this,QRCodeActivity.class);
                    startActivity(in);
                    EnterCodeActivity.this.finish();
                    break;
                case 101:
                    for (TextView ed:peds){
                        ed.setText("");
                    }
                    String s=b.getString("slength");
                    LogUtil.e(context,"101 s=="+s);
                    for (int i=0;i<s.length();i++){
                        peds[i].setText("*");
                    }
                    break;
                case 9:
                    showDialog(true);
                    action="validate";
                    new Thread(sendable).start();
                    break;
            }
        }
    };
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token=null;
                token=getAccessGenToken(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private void ValidatePayCode(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        String timestamp=Long.toString(token.getAccessDate());
        param.put("pin_data",true_peed.getValue(timestamp));
       // param.put("new_pin",true_peed.getValue(timestamp));
        param.put("resToken",token.getUniqueId());
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
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    if ("0000".equals(res.getString("status"))){
                        handler.sendEmptyMessage(1);
                    }else{
                        if ("55".equals(res.getString("errcode"))){
                            initChanceDialog(dataMap.getString("pinRetry"));
                        }
                        if ("36".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(EnterCodeActivity.this,res.getString("errmsg"));
                        }
                        if ("05".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(EnterCodeActivity.this,res.getString("errmsg"));
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
    public void initChanceDialog(String sum){
        //sum="3";
        LogUtil.e(context,"sum==="+sum);
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.enter_chance);
        Button btn_enter= (Button) dialog.getWindow().findViewById(R.id.positive);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText("密码错误,您今日剩余的尝试机会为"+sum+"次");
        if (null!=sum&&sum.equals("0")){
            tv.setText("账户已锁定，请次日凌晨3点后再使用");
            btn_enter.setText("确定");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    // BindShoppingCardActivity.this.finish();
                }
            });
        }else{
            btn_enter.setText("再次输入");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();

                }
            });
        }

    }

    private void OpenQRPay(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        String timestamp=Long.toString(token.getAccessDate());
        param.put("pin_data",true_peed.getValue(timestamp));
        param.put("resToken",token.getUniqueId());
        param.put("pcode_flag","1");
        param.put("pay_hwm",TianHongPayMentUtil.CurrentPay_Hwm);
        param.put("pf_day_hwm","500.00");
        param.put("day_hwm",TianHongPayMentUtil.CurrentDay_Hwm);
        param.put("pf_hwm",TianHongPayMentUtil.CurrentPf_Hwm);
        param.put("pf_flag",TianHongPayMentUtil.CurrentPf_flag);
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
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    if ("0000".equals(res.getString("status"))){
                        handler.sendEmptyMessage(2);
                    }else{
                        if ("55".equals(res.getString("errcode"))){
                            initChanceDialog(dataMap.getString("pinRetry"));
                        }
                        if ("36".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(EnterCodeActivity.this,res.getString("errmsg"));
                        }
                        if ("05".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(EnterCodeActivity.this,res.getString("errmsg"));
                        }
                    }
                }else{
                ToastUtil.shortNToast(EnterCodeActivity.this,"修改失败");
                }


            }

            @Override
            public void onError(Object o) {
                showDialog(false);
                Log.i("res err", "" + o.toString());
            }


        });
    }

    private void initViews() {
        tv_cancle= (TextView) findViewById(R.id.tv_cancle);
        tv_cancle.setVisibility(View.VISIBLE);
        ll_tvs= (LinearLayout) findViewById(R.id.ll_tvs);
        ll_tvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv:peds){
                    tv.setText("");
                }
                true_peed.openPEKbd();
//                true_peed.clear();
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("已取消");
                for (Activity a:TianHongPayMentUtil.pwdactivities){
                    a.finish();
                }
            }
        });
        initpess();
    }

    private void initpess() {
        true_peed= (PEEditText) findViewById(R.id.true_peed);
        PEEditTextAttrSet attr=new PEEditTextAttrSet();
        attr.name="set";
        attr.clearWhenOpenKbd=true;
        attr.softkbdType=1;
        attr.softkbdMode=1;
        attr.immersiveStyle=true;
        attr.kbdRandom=true;
        attr.kbdVibrator=true;
        attr.whenMaxCloseKbd=true;
        attr.minLength=0;
        attr.maxLength=6;
        attr.encryptType=0;//登录密码
        attr.inScrollView=false;
        true_peed.initialize(attr,EnterCodeActivity.this);//新初始化方法，用户
        true_peed.addTextChangedListener(new PeedChangeListener(true_peed,handler));
        true_peed.openPEKbd();

        peds=new TextView[6];
        pe1= (TextView) findViewById(R.id.password_edit1);
        pe2= (TextView) findViewById(R.id.password_edit2);
        pe3= (TextView) findViewById(R.id.password_edit3);
        pe4= (TextView) findViewById(R.id.password_edit4);
        pe5= (TextView) findViewById(R.id.password_edit5);
        pe6= (TextView) findViewById(R.id.password_edit6);
        peds[0]=pe1;
        peds[1]=pe2;
        peds[2]=pe3;
        peds[3]=pe4;
        peds[4]=pe5;
        peds[5]=pe6;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_basetitle_back) {
            TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("已取消");
            for (Activity a:TianHongPayMentUtil.pwdactivities){
                a.finish();
            }
           // EnterCodeActivity.this.finish();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        true_peed.onDestroy();
    }


}