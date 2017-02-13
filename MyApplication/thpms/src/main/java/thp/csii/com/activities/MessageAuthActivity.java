package thp.csii.com.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
import thp.csii.com.callback.EditChangedListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.CountDownTimerUtils;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

/**
 *MessageAuthActivity  短信验证页面
 *@author nylon
 * created at 2016/8/31 17:33
 */

public class MessageAuthActivity extends BaseTokenActivity implements View.OnClickListener {
    Button btn_next_step;
    TextView btn_sms,tv_phone;
    String message;
    LinearLayout ll_back;
    EditText ed_sms;
    CountDownTimerUtils mCountDownTimerUtils;
    String action;
    String code;
    private  TextView tv_cancle;
    private boolean sended=false;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_message_auth);

        action=getIntent().getStringExtra("action");
        code=getIntent().getStringExtra("pin_data");
        from=getIntent().getStringExtra("from");
        if (null!=from){
            if (from.equals("forget")){
                setTitleText(R.string.forget_code);
            }else{
                setTitleText(R.string.set_code);
            }
        }
        initViews();
        Log.i("res","sessionid="+SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));

    }
    String c1,c2;
    private void initViews() {
        tv_cancle= (TextView) findViewById(R.id.tv_cancle);
        tv_cancle.setVisibility(View.VISIBLE);
        tv_phone= (TextView) findViewById(R.id.tv_currentPhone);
        String ct=TianHongPayMentUtil.currentTel;
        if (null!=ct){
            c1=ct.substring(0,3);
            c2=ct.substring(ct.length()-2,ct.length());
        }
        tv_phone.setText(c1+"******"+c2);
        //setBackView(R.drawable.u194);
        ed_sms= (EditText) findViewById(R.id.ed_sms);
        ed_sms.addTextChangedListener(new EditChangedListener(ed_sms,handler));
        btn_sms = (TextView) findViewById(R.id.btn_sms);
        btn_next_step = (Button) findViewById(R.id.btn_next_step);
        btn_sms.setOnClickListener(this);
        btn_next_step.setOnClickListener(this);
        btn_next_step.setClickable(false);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        mCountDownTimerUtils = new CountDownTimerUtils(btn_sms, 60000, 1000);
    }

    private Token token;
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                token=null;
                token=getAccessGenToken(hand);
                LogUtil.e(context,"新的token="+token.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    SetPaycode(HttpUrls.resetTrsPwdConfirm);
                    break;
                case 0://设置成功 继续消费
                    break;
                case 500:
                    MessageAuthActivity.this.finish();
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sms) {
            showProDialog();
            GetSMSConfirm();
            mCountDownTimerUtils.start();
        } else if (i == R.id.btn_next_step) {
            showProDialog();
            new Thread(sendable).start();
            //hand.sendEmptyMessage(5);

        }else if (i==R.id.ll_back){
            initCancleDialog(getString(R.string.cancle_moifucation));

        }
    }

    public void initCancleDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog1);
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
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }
                MessageAuthActivity.this.finish();
                dialog.dismiss();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 9:
                    //验证已经达到六个字符
                    LogUtil.e(MessageAuthActivity.this,"99999");
                    btn_next_step.setClickable(true);
                    btn_next_step.setBackgroundResource(R.drawable.net_step_bg);
                    break;
                case 8:
                    //验证字符少于6个
                    LogUtil.e(MessageAuthActivity.this,"88888");
                    btn_next_step.setBackgroundResource(R.drawable.next_step_gray);
                    btn_next_step.setClickable(false);
                    break;
                case 404:
                    Bundle b=msg.getData();

                    ToastUtil.shortToast(context,b.getString("errmsg"));
                    break;
            }
        }
    };
    private void SetPaycode(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("pin_data",code);
        param.put("sms_code",ed_sms.getText().toString());
        if (null!=token){
            param.put("resToken",token.getUniqueId());
        }else if (!sended){
            ToastUtil.shortNToast(context,"请先获取验证码");
            return;
        }
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {

                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res&&res.getString("status").equals("0000")){
                    ToastUtil.shortNToast(context,"支付密码设置成功");
                    TianHongPayMentUtil.CodeSetted=true;
                    if (null!=action&&action.equals("bind")){

                        startActivity(new Intent(MessageAuthActivity.this,BindShoppingCardActivity.class));
                    }else if (null!=action&&action.equals("forgetcode")){
                        for (Activity a:TianHongPayMentUtil.spcactivities){
                            a.finish();
                        }
                    }else if (null!=action&&action.equals("modifycode")){
                        for (Activity a:TianHongPayMentUtil.spcactivities){
                            a.finish();
                        }
                    }else if (null!=action&&action.equals("qrcode")){
                        for (Activity a:TianHongPayMentUtil.spcactivities){
                            a.finish();
                        }
                    }else if (null!=action&&action.equals("pset")){
                        for (Activity a:TianHongPayMentUtil.spcactivities){
                            a.finish();
                        }
                        startActivity(new Intent(MessageAuthActivity.this,EnterCodeActivity.class));
                    }
                    MessageAuthActivity.this.finish();
                }else if ("4444".equals(res.getString("status"))){
                    Message msg=new Message();
                    Bundle b=new Bundle();
                    b.putString("errmsg",res.getString("errmsg"));
                    msg.setData(b);
                    msg.what=404;
                    handler.sendMessage(msg);
                    if ("00013".equals(res.getString("errcode"))){
                        //session过期弹出操作失败弹框
                        initSessionOutTime("操作失败"+("00013"));
                    }else if ("E0".equals(res.getString("errcode"))){
                        ToastUtil.shortNToast(MessageAuthActivity.this,"短信验证失败");
                       // mCountDownTimerUtils.onFinish();
                    }else if ("05".equals(res.getString("errcode"))){//密码过于简单
                        mCountDownTimerUtils.onFinish();
                        showToastAutoDismiss(res.getString("errmsg"));
                        //showMyToastAutoDismiss(res.getString("errmsg"),hand);
                        for (Activity a:TianHongPayMentUtil.spcactivities){
                            a.finish();
                        }

                    }

                }
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }

            }

            @Override
            public void onError(Object o) {
                ToastUtil.shortNToast(context,o.toString());
                Log.i("res err", "" + o.toString());
            }


        });
    }


    private void GetSMSConfirm() {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();


        String url =  Constant.SERVERHOST + Constant.AppName + HttpUrls.getSmsToken;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                dismissDialog();
                sended=true;
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if (null!=res){
                    if (res.get("status").equals("0000")) {
                        //JSONObject res = json.getJSONObject("res");
                        JSONObject datamap = res.getJSONObject("dataMap");
                        String rc = datamap.getString("rc");
                        String rc_detail = datamap.getString("rc_detail");
                        Log.i("res rc", "" + rc + "++++说明:" + rc_detail);
                        message="10086";
                        handler.sendEmptyMessage(1);

                    }else if (res.getString("status").equals("4444")){
                        ToastUtil.shortNToast(MessageAuthActivity.this,res.getString("errmsg"));
                    }
                }
            }

            @Override
            public void onError(Object o) {
                dismissDialog();
                ToastUtil.shortNToast(MessageAuthActivity.this,"网络错误");
                Log.i("res err", "" + o.toString());
            }


        });
    }
}