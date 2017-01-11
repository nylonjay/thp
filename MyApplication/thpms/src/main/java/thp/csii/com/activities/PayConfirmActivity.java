package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.QRPaySuccedActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PayOrderListener;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.callback.peeditTextListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.MyDialog;
import thp.csii.com.views.WindowInfo;

public class PayConfirmActivity extends BaseTokenActivity implements View.OnClickListener{
    private ImageView img_back;
    private TextView tv_cancle;
    private LinearLayout ll_back;
    private Button btn_pay;
    // ProgressBar myprogress;
    ImageView myprogress;
    //com.csii.powerenter.PEEditText peed;
    private Token token;
    private TextView tv_orderinfo,tv_orderamount;
    private TextView[] peds;
    private Animation mAnimationRate;
    private LinearLayout ll_tvs;
    private MyDialog dialog;
    // private MyReciever myreciever=new MyReciever();
    public static String TO_PAY="to_pay";
    //public View view_bg;
    private String entMode,pcode,chanl,oid;
    private double amount;
    private boolean needpwd;
    private String errmsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);
        setTitleText(R.string.pay_by_card);
        errmsg=getIntent().getStringExtra("errmsg");
        needpwd=getIntent().getBooleanExtra("needpwd",true);
        entMode=getIntent().getStringExtra("entMode");
        pcode=getIntent().getStringExtra("pcode");
        chanl=getIntent().getStringExtra("chanl");
        oid=getIntent().getStringExtra("oid");
        amount=getIntent().getDoubleExtra("amount",0);
        initViews();
        TianHongPayMentUtil.pwdactivities.add(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startVGAlphaAnimation(){
        if (needpwd){
            startActivity(new Intent(PayConfirmActivity.this,DialogActivity.class));
        }else{
            showDialog(true);
            new Thread(sendable).start();
        }
        btn_pay.setClickable(false);
    }

    private void initViews() {
        char symbol=165;
        // view_bg=findViewById(R.id.view_bg);
        tv_orderinfo= (TextView) findViewById(R.id.order_info);
        tv_orderamount= (TextView) findViewById(R.id.order_amount);
        if (null!=TianHongPayMentUtil.currentOder){
            tv_orderinfo.setText("虹领巾订单-"+ TianHongPayMentUtil.currentOder.getOid());
            tv_orderamount.setText(String.valueOf(symbol)+TianHongPayMentUtil.currentOder.getAmount());
        }else if (null!=oid&&0!=amount){
            tv_orderinfo.setText("虹领巾订单-"+ oid);
            tv_orderamount.setText(String.valueOf(symbol)+amount);
        }
        tv_cancle= (TextView) findViewById(R.id.tv_cancle);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        btn_pay= (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);
        imageViewBack.setVisibility(View.GONE);
        tv_cancle.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_back) {
            initQuitDialog("确定要放弃这次交易吗?");
        }else if (i==R.id.btn_pay){
            btn_pay.setClickable(false);
            if (null==errmsg){
                startVGAlphaAnimation();
            }else{
               // ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,errmsg);
                showToastAutoDismiss(errmsg);
                btn_pay.setClickable(true);
//                for (Activity a:TianHongPayMentUtil.pwdactivities){
//                    a.finish();
//                }
            }
        }

    }

    private Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 9://输入玩6个字符
                    LogUtil.e(context,"开始获取交易token");
                    ShowPregressImage();
//                    true_peed.setVisibility(View.GONE);
                    new Thread(sendable).start();//获取交易Token
                    break;
                case 1://重新授权登录完毕
                    LogUtil.e(context,"会话失效以后重新授权登录成功");
                    ToastUtil.shortToast(context,"请重试");
                    break;
                case 5:
                    LogUtil.e(context,"开始订单消费");
                    if (!needpwd){
                        PayOrdersNoNeedPwd(HttpUrls.oderCounsume);
                        return;
                    }
                  //  PayOrders(HttpUrls.oderCounsume);
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
                case 99:
//                    true_peed.onDestroy();
                    dialog.dismiss();
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
                LogUtil.e(context, "新的restoken==" + token.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void PayOrdersNoNeedPwd(String mUrl) {
        showDialog(true);
        HttpControl httpControl = new HttpControl(TianHongPayMentUtil.CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        if (TianHongPayMentUtil.from.equals("qr")){
            param.put("entMode",entMode);
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
        headers.put("Cookie",SharePreferencesUtils.getStringValue(TianHongPayMentUtil.CurrentContext,"Cookie"));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if ("0000".equals(res.getString("status"))) {
                    JSONObject dataMap=res.getJSONObject("dataMap");
                   // TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PaySucced(res.getString("msg"));
//                    if (null!=dataMap){
                        LogUtil.e(PayConfirmActivity.this,"from==="+TianHongPayMentUtil.from);
                        if ("qr".equals(TianHongPayMentUtil.from)){
                            Intent in = new Intent(PayConfirmActivity.this,QRPaySuccedActivity.class);
                            startActivity(in);
                        }else{
                            PayConfirmActivity.this.finish();
                            TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PaySucced(res.getString("msg"));
                        }
//                    }else{
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("msg"));
//                    }
                    // PayConfirmActivity.this.onPaySuccess(res.getString("msg"));
                } else if ("4444".equals(res.getString("status"))) {
                    TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(res.getString("errmsg"));
                    ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                    //LogUtil.e(TianHongPayMentUtil.CurrentContext,"payfailed");
//                    if (res.getString("errcode").equals("00005")){//令牌校验失败
//                        ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("00013")){//用户会话失效
//                        // initSessionOutTime("操作失败"+("00013"));
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("4444")){
//                        ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("A5")){
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("61")){//一次性交易金额过大
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("51")){//余额不足
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("00047")){//验签失败
//                        ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,res.getString("操作失败"+"("+"errmsg"+")"));
//                    }else if (res.getString("errcode").equals("ED")){//验签失败
//                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                    }

//                    else{//交给APP处理
//                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PushItoApp(json.toJSONString());
//
//                    }
                }else{
                    TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(res.getString("errmsg"));
                }
                btn_pay.setClickable(true);
            }
            @Override
            public void onError(Object o) {
                showDialog(false);
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(o.toString());
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }

//    private void PayOrders(String mUrl) {
//        HttpControl httpControl = new HttpControl(context);
//        httpControl.TimeOut = 20 * 1000;
//        Map<String, String> headers = new HashMap<String, String>();
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("entMode", "00");
//        // param.put("pcode","1008645423131");
//        String timestamp=String.valueOf(token.getAccessDate());
////        true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
////        param.put("pin_data",true_peed.getValue(timestamp));
//        //  param.put("sms_code","");
//        param.put("resToken", token.getUniqueId());
//        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
//        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
//        headers.put("Accept", "application/json");
//        headers.put("Connection", "Keep-Alive");
//        headers.put("Cookie", SharePreferencesUtils.getStringValue(context,"Cookie"));
//        httpControl.setHeaders(headers);
//        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
//            @Override
//            public void onSuccess(Object o) {
//                JSONObject json = JSON.parseObject((String) o);
//                JSONObject res = json.getJSONObject("res");
//                //myprogress.setVisibility(View.INVISIBLE);
//                StopPregressImage();
//                if ("0000".equals(res.getString("status"))) {
//                    ToastUtil.shortToast(context,res.getString("msg"));
//                    TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PaySucced(json.toJSONString());
////                    true_peed.clear();
//                    dialog.dismiss();
//                    PayConfirmActivity.this.finish();
//                }else if ("4444".equals(res.getString("status"))){
//                    TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayFailed(json.toJSONString());
//                    if (res.getString("errcode").equals("55")){
//
//                        initChanceDialog(res.getJSONObject("dataMap").getString("pinRetry"));
//                    }
//
//                    if (null!=res&&"00013".equals(res.getString("errcode"))){
//                        //用户会话失效
//                        initSessionOutTime("操作失败(00013)");
//
//                    }
//                    if (res.getString("errcode").equals("00046")){//密码输入超时
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//
//                    }else if (res.getString("errcode").equals("00005")){//令牌校验失败
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("00013")){//用户会话失效
//                        initSessionOutTime("操作失败"+("00013"));
//                    }else if (res.getString("errcode").equals("4444")){//通信故障
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("A5")){//订单号重复
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("61")){//一次性交易金额过大
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("51")){//余额不足
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("99")){//密码格式错误
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("errmsg"));
//                    }else if (res.getString("errcode").equals("00047")){//验签失败
//                        ToastUtil.shortToast(PayConfirmActivity.this,res.getString("操作失败"+"("+"errmsg"+")"));
//                    }
//
//                    else{//交给APP处理
//                        TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PushItoApp(json.toJSONString());
////                        true_peed.clear();
//                        dialog.dismiss();
//                    }
//                }
//            }
//            @Override
//            public void onError(Object o) {
//                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.OnNetWorkError();
//                Log.i("res err", "" + o.toString());
//            }
//        });
//    }

    public void initQuitDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText(sum);
        dialog.getWindow().findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                if (null!=dialog){
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(context,AuthenticationActivity.class));
                TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayCancled();
                if (null!=dialog){
                    dialog.dismiss();
                }
                PayConfirmActivity.this.finish();
            }
        });
    }

    public void initChanceDialog(String sum){
        //sum="3";
        dialog.dismiss();
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.enter_chance);
        Button btn_enter= (Button) dialog.getWindow().findViewById(R.id.positive);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText("您今日剩余的尝试机会为"+sum+"次");
        if (sum.equals("0")){
            tv.setText("购卡支付已锁定，请次日凌晨三点以后再使用");
            btn_enter.setText("确定");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    PayConfirmActivity.this.finish();
                }
            });
        }else{
            btn_enter.setText("再次输入");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    startVGAlphaAnimation();
                    //  showNewSixPeed();
                    //  showPayEnterDialog();
                    //PayConfirmActivity.this.HandleItMySelf("");

                }
            });
        }

    }
    TextView pe1,pe2,pe3,pe4,pe5,pe6;
    LinearLayout ll_close;

    void showPayEnterDialog(){
        if (null==dialog){
            dialog = new MyDialog(this);
            dialog.setContentView(R.layout.popuwindow_six_peed);
        }
        dialog.setCanceledOnTouchOutside(true);
        ll_close= (LinearLayout) dialog.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                true_peed.onDestroy();
                dialog.dismiss();
            }
        });
        myprogress= (ImageView) dialog.findViewById(R.id.myprogress);
        ll_tvs= (LinearLayout) dialog.findViewById(R.id.ll_tvs);
        ll_tvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv:peds){
                    tv.setText("");
//                    true_peed.clear();
//                    true_peed.openPEKbd();
                }
            }
        });
        myprogress= (ImageView) dialog.findViewById(R.id.myprogress);
//        initDialogpess();
        WindowInfo windowInfo = new WindowInfo(this);
        dialog.getWindow().setLayout(windowInfo.getWindowWidth(),
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 425, this.getResources().getDisplayMetrics()));
        //windowInfo.getWindowHeight() / 1.2)
        //(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 425, this.getResources().getDisplayMetrics())
        dialog.show();
//        true_peed.openPEKbd();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pe1=null;pe2=null;pe3=null;pe4=null;pe5=null;pe6=null;
                // true_peed.onDestroy();
//                true_peed.clear();
            }
        });

    }


    public void ShowPregressImage(){
        myprogress.setVisibility(View.VISIBLE);
        mAnimationRate = AnimationUtils.loadAnimation(context, R.anim.progess);
        myprogress.startAnimation(mAnimationRate);
    }

    public void StopPregressImage(){
        myprogress.setVisibility(View.INVISIBLE);
        //mAnimationRate = AnimationUtils.loadAnimation(context, R.anim.progess);
        //  myprogress.startAnimation(mAnimationRate);
        // myprogress.sto
        mAnimationRate=null;
    }
//    private void initDialogpess() {
//        true_peed= (PEEditText) dialog.findViewById(R.id.true_peed);
//        PEEditTextAttrSet attr=new PEEditTextAttrSet();
//        attr.name="true";
//        attr.clearWhenOpenKbd=true;
//        attr.softkbdType=1;
//        attr.immersiveStyle=true;
//        attr.softkbdMode=1;
//        attr.kbdRandom=true;
//        attr.kbdVibrator=true;
//        attr.whenMaxCloseKbd=true;
//        attr.minLength=6;
//        attr.maxLength=6;
//        attr.encryptType=0;//登录密码
//        attr.inScrollView=true;
//        true_peed.initialize(attr,PayConfirmActivity.this);//新初始化方法，用户
//        true_peed.addTextChangedListener(new PeedChangeListener(true_peed,hand));
//
//
//        peds=new TextView[6];
//        pe1= (TextView) dialog.findViewById(R.id.password_edit1);
//        pe2= (TextView) dialog.findViewById(R.id.password_edit2);
//        pe3= (TextView) dialog.findViewById(R.id.password_edit3);
//        pe4= (TextView) dialog.findViewById(R.id.password_edit4);
//        pe5= (TextView) dialog.findViewById(R.id.password_edit5);
//        pe6= (TextView) dialog.findViewById(R.id.password_edit6);
//        peds[0]=pe1;
//        peds[1]=pe2;
//        peds[2]=pe3;
//        peds[3]=pe4;
//        peds[4]=pe5;
//        peds[5]=pe6;
//    }

//    private void initpess(View v) {
//        true_peed= (PEEditText) v.findViewById(R.id.true_peed);
//        PEEditTextAttrSet attr=new PEEditTextAttrSet();
//        attr.name="true";
//        attr.clearWhenOpenKbd=true;
//        attr.softkbdType=1;
//        attr.softkbdMode=1;
//        attr.kbdRandom=true;
//        attr.kbdVibrator=true;
//        attr.whenMaxCloseKbd=true;
//        attr.minLength=0;
//        attr.maxLength=6;
//        attr.encryptType=0;//登录密码
//        attr.inScrollView=false;
//        true_peed.initialize(attr);//新初始化方法，用户
//        true_peed.addTextChangedListener(new peeditTextListener(true_peed,hand));
//
//
//        peds=new TextView[6];
//        pe1= (TextView) v.findViewById(R.id.password_edit1);
//        pe2= (TextView) v.findViewById(R.id.password_edit2);
//        pe3= (TextView) v.findViewById(R.id.password_edit3);
//        pe4= (TextView) v.findViewById(R.id.password_edit4);
//        pe5= (TextView) v.findViewById(R.id.password_edit5);
//        pe6= (TextView) v.findViewById(R.id.password_edit6);
//        peds[0]=pe1;
//        peds[1]=pe2;
//        peds[2]=pe3;
//        peds[3]=pe4;
//        peds[4]=pe5;
//        peds[5]=pe6;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        //view_bg.setVisibility(View.GONE);
//        if (null!=true_peed){
//            true_peed.openPEKbd();
//        }
        if (null!=btn_pay)
            btn_pay.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (null!=mPopupWindow)
//            mPopupWindow=null;
        if (null!=myprogress)
            myprogress=null;
//        if (null!=true_peed)
//            true_peed.onDestroy();

        // unregisterReceiver(myreciever);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        initQuitDialog("确定要放弃这次交易吗？");

        if (null!=TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener){
            TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener.PayCancled();
        }
    }
//



}