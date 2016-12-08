package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import thp.csii.com.utils.ToastUtil;

public class SetFastPayCodeActivity extends BaseTokenActivity {
    private Token token;
    private Button submit,modifycounts;
    private PEEditText ed_first,ed_second;
    String action="paycode";
    private String sms;
    private LinearLayout ll_back;
    String paction="p";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_fast_pay_code);
        setBackView(R.drawable.u194);
        setTitleText(R.string.resetpwd);
        paction=getIntent().getStringExtra("action");
        sms=getIntent().getStringExtra("sms");
        initViews();
    }

    private void initViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFastPayCodeActivity.this.finish();
            }
        });
        ed_first= (PEEditText) findViewById(R.id.ed_name);
        ed_second= (PEEditText) findViewById(R.id.ed_number);

        PEEditTextAttrSet attr1=new PEEditTextAttrSet();//初始化属性集
        attr1.name="set1";//控件名
        attr1.clearWhenOpenKbd=true;//密码键盘打开时，是否清空输入框
        attr1.softkbdType=1;//键盘类型
        attr1.softkbdMode=2;//键盘是否开启触控效果，0开启，1关闭 ，2开启，但无放大效果
        attr1.kbdRandom=true;//键盘按键顺序是否随机
        attr1.kbdVibrator=true;//触控键盘是否震动
        attr1.whenMaxCloseKbd=false;//当密码到达最大长度时是否自动关闭键盘
        attr1.minLength=6;//密码最小长度
        attr1.maxLength=6;//密码最大长度
        attr1.encryptType=0;//密码加密类型
        attr1.inScrollView=true;//密码是否在ScrollView中
        //attr1.accept="^[0-9]{6}$";//密码内容正则表达式
        ed_first.initialize(attr1,SetFastPayCodeActivity.this);//新初始化方法，用户交互体验更佳
        PEEditTextAttrSet attr2=new PEEditTextAttrSet();//初始化属性集
        attr2.name="set2";//控件名
        attr2.clearWhenOpenKbd=true;//密码键盘打开时，是否清空输入框
        attr2.softkbdType=1;//键盘类型
        attr2.softkbdMode=2;//键盘是否开启触控效果，0开启，1关闭 ，2开启，但无放大效果
        attr2.kbdRandom=true;//键盘按键顺序是否随机
        attr2.kbdVibrator=true;//触控键盘是否震动
        attr2.whenMaxCloseKbd=false;//当密码到达最大长度时是否自动关闭键盘
        attr2.minLength=6;//密码最小长度
        attr2.maxLength=6;//密码最大长度
        attr2.encryptType=0;//密码加密类型
        attr2.inScrollView=true;//密码是否在ScrollView中
        //attr1.accept="^[0-9]{6}$";//密码内容正则表达式
        ed_second.initialize(attr2,SetFastPayCodeActivity.this);//新初始化方法，用户交互体验更佳

        submit= (Button) findViewById(R.id.submit);
        modifycounts= (Button) findViewById(R.id.modifycounts);
        modifycounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="modifycounts";
                new Thread(sendable).start();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="paycode";
                new Thread(sendable).start();
            }
        });
    }

    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    if (action.equals("paycode"))
                        SetPaycode(HttpUrls.resetTrsPwdConfirm);
                    else
                        // ModifyCounts(HttpUrls.modifyPayFunConfirm);
                        break;
                case 0://设置成功 继续消费
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
                token=getAccessGenToken(hand);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void SetPaycode(String mUrl) {
        int v_check=ed_first.validity_check();
        if(v_check==0){
            //ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"密码格式正确");
        }else {
            if(v_check==-1)Toast.makeText(SetFastPayCodeActivity.this,"原密码为空", Toast.LENGTH_SHORT).show();
            if(v_check==-2)Toast.makeText(SetFastPayCodeActivity.this,"原密码长度小于最小长度", Toast.LENGTH_SHORT).show();
            if(v_check==-3)Toast.makeText(SetFastPayCodeActivity.this,"原密码内容不合法", Toast.LENGTH_SHORT).show();
        }

        int v_check1=ed_second.validity_check();
        if(v_check1==0){
            //ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"密码格式正确");
        }else {
            if(v_check1==-1)Toast.makeText(SetFastPayCodeActivity.this,"新密码为空", Toast.LENGTH_SHORT).show();
            if(v_check1==-2)Toast.makeText(SetFastPayCodeActivity.this,"新密码长度小于最小长度", Toast.LENGTH_SHORT).show();
            if(v_check1==-3)Toast.makeText(SetFastPayCodeActivity.this,"新密码内容不合法", Toast.LENGTH_SHORT).show();
        }

        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        ed_first.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        ed_second.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        String timestamp=String.valueOf(token.getAccessDate());
        if (!ed_second.getHash().equals(ed_first.getHash())){
            ToastUtil.shortToast(context,"两次输入的密码不一致");
            return;
        }
        param.put("pin_data",ed_second.getValue(timestamp));
        param.put("sms_code",sms);
        param.put("resToken",token.getUniqueId());
        String url = Constant.SERVERHOST + Constant.AppName + mUrl;
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
                    ToastUtil.shortToast(context,"设置支付密码成功");
                    if (null!=paction&&paction.equals("cost")){
                        startActivity(new Intent(SetFastPayCodeActivity.this,PayConfirmActivity.class));

                    }
                    SetFastPayCodeActivity.this.finish();
                    //设置成功跳回首页
                }


            }

            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }


        });
    }

    private void ModifyCounts(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("pin_data","100867878");
        param.put("sms_code","1008645423131");
        param.put("pay_hwm","100867878");
        param.put("day_hwm","1008645423131");
        param.put("pf_hwm","100867878");
        param.put("pf_day_hwm","1008645423131");
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
                JSONObject json = JSON.parseObject((String) o);



            }

            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }


        });
    }
}
