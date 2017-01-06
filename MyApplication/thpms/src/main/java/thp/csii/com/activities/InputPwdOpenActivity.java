package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class InputPwdOpenActivity extends Activity {
    private TextView[] peds;
    private PEEditText true_peed;
    private TextView pe1,pe2,pe3,pe4,pe5,pe6;
    private TextView tv_forget;
    private ImageView ll_close;
    private Token token;
    private ImageView myprogress;
    private Animation mAnimationRate;
    private LinearLayout ll_tvs;
    private boolean reclick=false;
    private RelativeLayout re_contaner;
    public  View v;
    private MyReciever myreciever=new MyReciever();
    String requestcode;
    String pwd;
    String from;
    private String pf_hwm,pay_hwm,day_hwm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd_open);
        initDialogpess();
        requestcode=getIntent().getStringExtra("requestcode");
        from=getIntent().getStringExtra("from");
        pf_hwm=getIntent().getStringExtra("pf_hwm");
        pay_hwm=getIntent().getStringExtra("pay_hwm");
        day_hwm=getIntent().getStringExtra("day_hwm");
        LogUtil.e(InputPwdOpenActivity.this,"pf pay day=="+pf_hwm+"/"+pay_hwm+"/"+day_hwm);
        TianHongPayMentUtil.pwdactivities.add(this);

    }

    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 0:
                    //该页面被手动关闭 开关设置为关闭状态
                    StopPregressImage();
                    setResult(RESULT_CANCELED);
                    InputPwdOpenActivity.this.finish();
                    break;
                case 1:
                    //验证密码通过  开关设置为开启状态
                    if (from.equals("stop")){
                        new Thread(sendable1).start();
                        return;
                    }
                    StopPregressImage();
                    Intent in=new Intent(InputPwdOpenActivity.this,Pay_SettingActivity.class);
                    in.putExtra("pwd",pwd);
                    startActivity(in);
                    InputPwdOpenActivity.this.finish();
                    break;
                case 5:
                    ValidatePayCode(HttpUrls.trsPwdValidate);
                    break;
                case 6:
                    ShowPregressImage();
                    ModifyPaySetting(HttpUrls.modifyPayFunConfirm);
                    break;
                case 9:
                    ShowPregressImage();
                    LogUtil.e(InputPwdOpenActivity.this,"开始验证支付密码");
                    new Thread(sendable).start();
                    break;
                case 101:
                    for (TextView ed:peds){
                        ed.setText("");
                    }
                    String s=b.getString("slength");
                    LogUtil.e(InputPwdOpenActivity.this,"101 s=="+s);
                    if (s.length()>0){
                        for (int i=0;i<s.length();i++){
                            peds[i].setText("*");
                        }
                    }
                    break;
            }
        }
    };
    private void ModifyPaySetting(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("pin_data",pwd);
        param.put("pf_flag","0");
        param.put("day_hwm",day_hwm);
        param.put("pay_hwm",pay_hwm);
        param.put("pf_hwm",pf_hwm);
        param.put("resToken",token.getUniqueId());
        param.put("pf_day_hwm","500.00");
        param.put("pcode_flag","0");
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                StopPregressImage();
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res){
                    if ("0000".equals(res.getString("status"))){
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed("付款码支付已关闭");
                        for (Activity a:TianHongPayMentUtil.pwdactivities){
                            a.finish();
                        }
                    }else{
                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                    }
                }
            }
            @Override
            public void onError(Object o) {
                StopPregressImage();
                Log.i("res err", "" + o.toString());
            }


        });
    }

    private void ValidatePayCode(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        String timestamp=Long.toString(token.getAccessDate());
        param.put("pin_data",true_peed.getValue(timestamp));
        pwd=true_peed.getValue(timestamp);
        param.put("new_pin",true_peed.getValue(timestamp));
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
                StopPregressImage();
                //  showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res){
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    if ("0000".equals(res.getString("status"))){
                        hand.sendEmptyMessage(1);
                        ToastUtil.shortNToast(InputPwdOpenActivity.this,"验证通过");
                    }else{
                        if ("55".equals(res.getString("errcode"))){
                            initChanceDialog(dataMap.getString("pinRetry"));
                        }
                        if ("36".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(InputPwdOpenActivity.this,res.getString("errmsg"));
                        }
                        if ("05".equals(res.getString("errcode"))){
                            ToastUtil.shortNToast(InputPwdOpenActivity.this,res.getString("errmsg"));
                        }
                    }
                }


            }

            @Override
            public void onError(Object o) {
                StopPregressImage();
                Log.i("res err", "" + o.toString());
            }


        });
    }
    public void initChanceDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(InputPwdOpenActivity.this).create();
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
    Runnable sendable1 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token=null;
                token=getAccessGenToken1(hand);
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
            }

        } catch (Exception e) {
            //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }

        return token;
    }
    protected Token getAccessGenToken1(Handler hand) throws Exception {
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
                hand.sendEmptyMessage(6);
            }

        } catch (Exception e) {
            //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }

        return token;
    }

    public void ShowPregressImage(){
        myprogress.setVisibility(View.VISIBLE);
        mAnimationRate = AnimationUtils.loadAnimation(InputPwdOpenActivity.this, R.anim.progess);
        myprogress.startAnimation(mAnimationRate);
    }

    public void StopPregressImage(){
        //myprogress.
        myprogress.clearAnimation();
        myprogress.setVisibility(View.GONE);
        //  mAnimationRate.cancel();
        LogUtil.e(InputPwdOpenActivity.this,"取消进度条");
    }
    private void initDialogpess() {
        ll_close= (ImageView) findViewById(R.id.icon_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hand.sendEmptyMessage(0);
                InputPwdOpenActivity.this.finish();
            }
        });
        myprogress= (ImageView) findViewById(R.id.myprogress);
        ll_tvs= (LinearLayout) findViewById(R.id.ll_tvs);
        ll_tvs.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                reclick=false;
                if (null!=true_peed&&!InputPwdOpenActivity.this.isDestroyed()){
                    true_peed.openPEKbd();
                }
            }
        });
        re_contaner= (RelativeLayout) findViewById(R.id.enter_re);
        re_contaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclick=true;
            }
        });
        registerReceiver(myreciever,new IntentFilter("com.csii.powerenter.action.Send_msg"));
        v=findViewById(R.id.view_top);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclick=false;
                hand.sendEmptyMessage(0);
            }
        });
        true_peed= (PEEditText) findViewById(R.id.true_peed);
        PEEditTextAttrSet attr=new PEEditTextAttrSet();
        attr.name="dialog1";
        attr.clearWhenOpenKbd=true;
        attr.softkbdType=1;
        attr.immersiveStyle=true;
        attr.softkbdMode=1;
        attr.kbdRandom=true;
        attr.kbdVibrator=true;
        attr.whenMaxCloseKbd=true;
        attr.minLength=6;
        attr.maxLength=6;
        attr.encryptType=0;//登录密码
        attr.inScrollView=false;
        true_peed.initialize(attr,InputPwdOpenActivity.this);//新初始化方法，用户
        true_peed.addTextChangedListener(new PeedChangeListener(true_peed,hand));
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
        tv_forget= (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(InputPwdOpenActivity.this,SetPayCode_First_Activity.class);
                in.putExtra("from","forget");
                startActivity(in);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myreciever);
        true_peed.onDestroy();
    }

    class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null!=intent.getExtras().getString("PEKbdInfo")&&
                    intent.getExtras().getString("PEKbdInfo").equals("CloseInfo")
                    ){
                if (null!=intent.getExtras().getString("length")&&
                        intent.getExtras().getString("length").equals("6")){
                    LogUtil.e(context,"length=="+intent.getExtras().getString("length"));
                    return;
                }
                if (reclick){
                    hand.sendEmptyMessage(0);
                }
                //dialog.dismiss();
            }
        }
    }
}
