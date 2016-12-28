package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.QRPaySuccedActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.AppUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.UserDefinedDialog;

public class DialogActivity extends Activity {

    private TextView[] peds;
    private PEEditText true_peed;
    private TextView pe1,pe2,pe3,pe4,pe5,pe6;
    private TextView tv_forget;
    public  View v;
    private MyReciever myreciever=new MyReciever();
    private RelativeLayout re_contaner;
    private boolean reclick=false;
    private LinearLayout ll_tvs;
    private ImageView myprogress;
    private ImageView ll_close;
    private Token token;
    private Animation mAnimationRate;
    private UserDefinedDialog dia;
    private RelativeLayout enter_re;
    private String entMode,pcode,chanl;
    private String amount;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initDialogpess();
        getIntenData();
        TianHongPayMentUtil.pwdactivities.add(this);
        if (null!=true_peed){
            true_peed.openPEKbd();
        }
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    private void getIntenData() {
        entMode=getIntent().getStringExtra("entMode");
        pcode=getIntent().getStringExtra("pcode");
        amount=getIntent().getStringExtra("amount");
        chanl=getIntent().getStringExtra("chanl");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDialogpess() {
        enter_re= (RelativeLayout) findViewById(R.id.enter_re);
        ll_close= (ImageView) findViewById(R.id.icon_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hand.sendEmptyMessage(0);
            }
        });
        myprogress= (ImageView) findViewById(R.id.myprogress);
        ll_tvs= (LinearLayout) findViewById(R.id.ll_tvs);
        ll_tvs.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                reclick=false;
                if (null!=true_peed&&!DialogActivity.this.isDestroyed()){
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
        true_peed.initialize(attr,DialogActivity.this);//新初始化方法，用户
        true_peed.addTextChangedListener(new PeedChangeListener(true_peed,hand));
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
                Intent in=new Intent(DialogActivity.this,SetPayCode_First_Activity.class);
                in.putExtra("from","forget");
                startActivity(in);
            }
        });
    }

    Handler hand=new Handler(){
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 9:
                    LogUtil.e(DialogActivity.this,"开始获取交易token");
                    ShowPregressImage();
                    true_peed.setVisibility(View.GONE);
                    new Thread(sendable).start();//获取交易Token
                    break;
                case 0://关闭当前Activity
                    // v.setAlpha(0.0f);
                    LogUtil.e(DialogActivity.this,"000000000");
                    true_peed.onDestroy();
                    DialogActivity.this.finish();
                   // overridePendingTransition(R.anim.activity_close,0);
                    break;
                case 5:
                    LogUtil.e(DialogActivity.this,"开始非免密订单消费");
                    PayOrders(HttpUrls.oderCounsume);
                    break;
                case 101:
                    for (TextView ed:peds){
                        ed.setText("");
                    }
                    String s=b.getString("slength");
                    LogUtil.e(DialogActivity.this,"101 s=="+s);
                    if (s.length()>0){
                        for (int i=0;i<s.length();i++){
                            peds[i].setText("*");
                        }
                    }
                    break;
            }
        }
    };
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token = null;
                token = getAccessGenToken(hand);
                LogUtil.e(DialogActivity.this, "新的restoken==" + token.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPause() {
        super.onPause();
        // v.setAlpha(0.0f);
    }

    private void PayOrders(String mUrl) {
        HttpControl httpControl = new HttpControl(DialogActivity.this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        if (null!=entMode){
            param.put("entMode",entMode);
        }else{
            param.put("entMode", "00");
        }
        if (null!=pcode){
            param.put("pcode",pcode);
        }
        if (null!=chanl){
            param.put("chanl",chanl);
        }
        String timestamp=String.valueOf(token.getAccessDate());
        true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        param.put("pin_data",true_peed.getValue(timestamp));
        //  param.put("sms_code","");
        param.put("resToken", token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getStringValue(DialogActivity.this,"Cookie"));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                //myprogress.setVisibility(View.INVISIBLE);
                LogUtil.e(TianHongPayMentUtil.CurrentContext,"扫码支付之后 res=="+res.toJSONString());
                StopPregressImage();
                if ("0000".equals(res.getString("status"))) {
                //ToastUtil.shortToast(DialogActivity.this,res.getString("msg"));
                   // showMyToastAutoDismiss(res.getString("msg"));
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    true_peed.clear();
                    if (TianHongPayMentUtil.from.equals("qr")){
                        Intent in=new Intent(DialogActivity.this, QRPaySuccedActivity.class);
                        in.putExtra("amount",amount);
                        startActivity(in);
                    }else {
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PaySucced(res.getString("msg"));
                        for (Activity a:TianHongPayMentUtil.pwdactivities){
                            a.finish();
                        }
                    }
                }else if ("4444".equals(res.getString("status"))){
                    TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(res.getString("errmsg"));
                    if (res.getString("errcode").equals("55")){
                        initChanceDialog(res.getJSONObject("dataMap").getString("pinRetry"));
                    }
                    if (res.getString("errcode").equals("5")){
                        ToastUtil.shortNToast(DialogActivity.this,res.getString("errmsg"));
                    }
                    if (res.getString("errcode").equals("36")){
                        initChanceDialog("0");
                    }
                    if (null!=res&&"00013".equals(res.getString("errcode"))){
                        //用户会话失效
                        initSessionOutTime("操作失败(00013)");
                    }
                    if (res.getString("errcode").equals("00046")){//密码输入超时
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00005")){//令牌校验失败
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00013")){//用户会话失效
                        initSessionOutTime("操作失败"+("00013"));
                    }else if (res.getString("errcode").equals("4444")){//通信故障
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("A5")){//订单号重复
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("61")){//一次性交易金额过大
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("51")){//余额不足
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("99")){//密码格式错误
                        showMyToastAutoDismiss(res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00047")){//验签失败
                       // ToastUtil.shortToast(DialogActivity.this,res.getString("操作失败"+"("+"errmsg"+")"));
                        showMyToastAutoDismiss(res.getString("操作失败"+"("+"errmsg"+")"));
                    }

                    else{//交给APP处理
                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PushItoApp(json.toJSONString());
                        true_peed.clear();
                        //dialog.dismiss();
                    }
                }
            }
            @Override
            public void onError(Object o) {
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }
    public void initSessionOutTime(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(DialogActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText(sum);
        dialog.getWindow().findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                dialog.dismiss();
            }
        });
        dialog.getWindow().findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(context,AuthenticationActivity.class));
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }
                TianHongPayMentUtil.activities.get(0).finish();
                dialog.dismiss();
            }
        });
    }
    public void initChanceDialog(String sum){
        //sum="3";
        // dialog.dismiss();
        final AlertDialog dialog=new AlertDialog.Builder(DialogActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.enter_chance);
        Button btn_enter= (Button) dialog.getWindow().findViewById(R.id.positive);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText("密码错误,您今日剩余的尝试机会为"+sum+"次");
        if (sum.equals("0")){
            tv.setText("购物卡支付已锁定，请次日凌晨三点以后再使用");
            btn_enter.setText("确定");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    // DialogActivity.this.finish();
                    hand.sendEmptyMessage(0);
                }
            });
        }else{
            btn_enter.setText("重新输入");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    if (null!=true_peed){
                        StopPregressImage();
                        true_peed.openPEKbd();
                    }
                    //  showNewSixPeed();
                    // showPayEnterDialog();
                    //DialogActivity.this.HandleItMySelf("");

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hand.sendEmptyMessage(0);
    }
    public void ShowPregressImage(){
        myprogress.setVisibility(View.VISIBLE);
        mAnimationRate = AnimationUtils.loadAnimation(DialogActivity.this, R.anim.progess);
        myprogress.startAnimation(mAnimationRate);
    }

    public void StopPregressImage(){
        //myprogress.
        myprogress.clearAnimation();
        myprogress.setVisibility(View.GONE);
      //  mAnimationRate.cancel();
        LogUtil.e(DialogActivity.this,"取消进度条");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myreciever);
        if (null!=true_peed){
            true_peed.onDestroy();
        }
    }

    public void showMyToastAutoDismiss(String msg){
        final UserDefinedDialog dia=  new UserDefinedDialog(this, msg, null, null);
        dia.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dia.dismiss();
//                handler.sendEmptyMessage(500);
            }
        },1500);
    }
}
