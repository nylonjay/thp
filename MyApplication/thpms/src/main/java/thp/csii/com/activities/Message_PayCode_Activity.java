package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;

/**
 *Message_Close_PayCode_Activity  输入支付密码和验证码
 *@author nylon
 * created at 2016/9/13 9:08
 */

public class Message_PayCode_Activity extends BaseTokenActivity implements View.OnClickListener{
    private PEEditText ed_pay_code;
    private Button mBtnSms;
    private Token token;
    private EditText ed_sms;
    private Button mBtnSetttingCode;
    //PasswordView pdv;
    /// TextView tvForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_open__close__pay_code_);
        setBackView(R.drawable.u194);
        initViews();
        //  CheckPayState();
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

    android.os.Handler hand=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
               SetPayCode();
                    break;
            }
            //ToastUtil.shortToast(context,"获取到的Tokenuniqueid是"+msg.obj);
        }
    };



    private void initViews() {
        mBtnSetttingCode= (Button) findViewById(R.id.btn_set_code);
        mBtnSetttingCode.setOnClickListener(this);
        ed_sms= (EditText) findViewById(R.id.ed_sms);
        mBtnSms= (Button) findViewById(R.id.btn_sms);
        mBtnSms.setOnClickListener(this);
        ed_pay_code= (PEEditText) findViewById(R.id.ed_pe);
        PEEditTextAttrSet attr1=new PEEditTextAttrSet();//初始化属性集
        attr1.name="password1";//控件名
        attr1.clearWhenOpenKbd=true;//密码键盘打开时，是否清空输入框
        attr1.softkbdType=2;//键盘类型
        attr1.softkbdMode=2;//键盘是否开启触控效果，0开启，1关闭 ，2开启，但无放大效果
        attr1.kbdRandom=true;//键盘按键顺序是否随机
        attr1.kbdVibrator=true;//触控键盘是否震动
        attr1.whenMaxCloseKbd=false;//当密码到达最大长度时是否自动关闭键盘
        attr1.minLength=6;//密码最小长度
        attr1.maxLength=16;//密码最大长度
        attr1.encryptType=0;//密码加密类型
        attr1.inScrollView=true;//密码是否在ScrollView中
        //attr1.accept="^[0-9]{6}$";//密码内容正则表达式
        ed_pay_code.initialize(attr1,Message_PayCode_Activity.this);//新初始化方法，用户交互体验更佳


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sms) {
            GetSMSConfirm();

        } else if (i == R.id.btn_set_code) {
            new Thread(sendable).start();

        }

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
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if (null!=res&&res.get("status").equals("0000")) {
                    //JSONObject res = json.getJSONObject("res");
                    JSONObject datamap = res.getJSONObject("dataMap");
                    String rc = datamap.getString("rc");
                    String rc_detail = datamap.getString("rc_detail");
                    Log.i("res rc", "" + rc + "++++说明:" + rc_detail);
//                    message="10086";
//                    handler.sendEmptyMessage(1);

                }
            }

            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }


        });
    }


    private void SetPayCode() {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String timestamp=Long.toString(token.getAccessDate());
        ed_pay_code.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        param.put("pin_data", ed_pay_code.getValue(timestamp));
        param.put("sms_code",ed_sms.getText().toString());
        param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + HttpUrls.resetTrsPwdConfirm;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if (null!=res&&res.get("status").equals("0000")) {
                    //JSONObject res = json.getJSONObject("res");
                    JSONObject datamap = res.getJSONObject("dataMap");
                    setResult(RESULT_OK);
//                    message="10086";
//                    handler.sendEmptyMessage(1);

                }
            }

            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }


        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ed_pay_code.onDestroy();
    }
}
