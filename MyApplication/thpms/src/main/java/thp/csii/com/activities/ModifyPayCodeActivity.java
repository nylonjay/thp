package thp.csii.com.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

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
import thp.csii.com.callback.peeditTextListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class ModifyPayCodeActivity extends BaseTokenActivity {
    PEEditText ed_pay_code,ed_new_code,ed_confirm_code;
    Button submit;
    Token token;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///SetStatusColor();
        setContentView(R.layout.activity_modify_pay_code);
        setTitleText(R.string.modify_pay_code);
        initViews();
        initPEEditors();
    }

    private void initViews() {
        imageViewBack.setBackgroundResource(R.drawable.u194);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyPayCodeActivity.this.finish();
            }
        });
        submit= (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProDialog();
                new Thread(sendable).start();
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5://获取到交易token以后
                    LogUtil.e(context,"55555");
                    ModifyPayCode(HttpUrls.modifyTrsPwdConfirm);
                    break;
                case 9:
                    LogUtil.e(ModifyPayCodeActivity.this,"9");
                    if (ed_pay_code.validity_check()==0&&ed_new_code.validity_check()==0&&ed_confirm_code.validity_check()==0){
                        submit.setBackgroundResource(R.drawable.net_step_bg);
                        submit.setClickable(true);
                    }else{
                        submit.setBackgroundResource(R.drawable.next_step_gray);
                        submit.setClickable(false);
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
                token=null;
                token=getAccessGenToken(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void ModifyPayCode(String mUrl) {
        int v_check1=ed_confirm_code.validity_check();
        if(v_check1==0){
            //ToastUtil.shortToast(ModifyPayCodeActivity.this,"确认密码格式正确");
        }else {
            if(v_check1==-1)Toast.makeText(ModifyPayCodeActivity.this,"确认密码为空", Toast.LENGTH_SHORT).show();
            if(v_check1==-2)Toast.makeText(ModifyPayCodeActivity.this,"确认密码长度小于最小长度", Toast.LENGTH_SHORT).show();
            if(v_check1==-3)Toast.makeText(ModifyPayCodeActivity.this,"确认密码内容不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        int v_check=ed_new_code.validity_check();
        if(v_check==0){// ToastUtil.shortToast(ModifyPayCodeActivity.this,"新密码格式正确");
        }else{
            if(v_check==-1)Toast.makeText(ModifyPayCodeActivity.this,"新密码为空", Toast.LENGTH_SHORT).show();
            if(v_check==-2)Toast.makeText(ModifyPayCodeActivity.this,"新密码长度小于最小长度", Toast.LENGTH_SHORT).show();
            if(v_check==-3)Toast.makeText(ModifyPayCodeActivity.this,"新密码内容不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ed_new_code.getHash().equals(ed_confirm_code.getHash())){
            ToastUtil.shortNToast(context,"两次输入的密码不一致");
            ed_new_code.clear();
            ed_confirm_code.clear();
            return;
        }
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        ed_pay_code.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        ed_new_code.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        ed_confirm_code.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        String timestamp=Long.toString(token.getAccessDate());
        param.put("pin_data",ed_pay_code.getValue(timestamp));
        param.put("new_pin",ed_confirm_code.getValue(timestamp));
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
                dismissDialog();
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res){
                    if (res.getString("status").equals("0000")){
                        ModifyPayCodeActivity.this.finish();
                        ToastUtil.shortNToast(context,"密码修改成功");
                    }else if (res.getString("status").equals("4444")){
                        ToastUtil.shortToast(context,res.getString("errmsg"));
                        JSONObject datamap=res.getJSONObject("dataMap");
                        if (null!=datamap){
                            if ("55".equals(res.getString("errcode"))){
                                String pinRetry=datamap.getString("pinRetry");
                                initChanceDialog(pinRetry);
                            }else if ("36".equals(res.getString("errcode"))){
                                initChanceDialog("0");
                            } else{
                                ToastUtil.shortNToast(context,res.getString("errmsg"));
                                ed_new_code.clear();
                                ed_confirm_code.clear();
                            }
                        }
                    }

                }else{
                    ToastUtil.shortToast(context,res.getString("errmsg"));
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

    private void initPEEditors() {

        ed_pay_code= (PEEditText) findViewById(R.id.ed_pay_code);
        ed_new_code= (PEEditText) findViewById(R.id.ed_new_code);
        ed_confirm_code= (PEEditText) findViewById(R.id.ed_confirm_code);
        ed_pay_code.addTextChangedListener(new peeditTextListener(ed_pay_code,handler));
        ed_new_code.addTextChangedListener(new peeditTextListener(ed_new_code,handler));
        ed_confirm_code.addTextChangedListener(new peeditTextListener(ed_confirm_code,handler));
        PEEditTextAttrSet attr1=new PEEditTextAttrSet();//初始化属性集
        attr1.name="modify1";//控件名
        attr1.clearWhenOpenKbd=false;//密码键盘打开时，是否清空输入框
        attr1.softkbdType=1;//键盘类型
        attr1.softkbdMode=1;//键盘是否开启触控效果，0开启，1关闭 ，2开启，但无放大效果
        attr1.kbdRandom=true;//键盘按键顺序是否随机
        attr1.kbdVibrator=true;//触控键盘是否震动
        attr1.immersiveStyle=true;
        attr1.whenMaxCloseKbd=false;//当密码到达最大长度时是否自动关闭键盘
        attr1.minLength=6;//密码最小长度
        attr1.maxLength=6;//密码最大长度
        attr1.encryptType=0;//密码加密类型
        attr1.inScrollView=true;//密码是否在ScrollView中
        //attr1.accept="^[0-9]{6}$";//密码内容正则表达式
        ed_pay_code.initialize(attr1,ModifyPayCodeActivity.this);//新初始化方法，用户交互体验更佳
        PEEditTextAttrSet attr=new PEEditTextAttrSet();
        attr.name="modi1";
        attr.clearWhenOpenKbd=true;
        attr.softkbdType=1;
        attr.softkbdMode=1;
        attr.kbdRandom=true;
        attr.kbdVibrator=true;
        attr.whenMaxCloseKbd=false;
        attr.immersiveStyle=true;
        attr.minLength=6;
        attr.maxLength=6;
        attr.encryptType=0;//登录密码
        attr.inScrollView=false;
        ed_new_code.initialize(attr,ModifyPayCodeActivity.this);//老初始化方法，稳定性强
        PEEditTextAttrSet attr2=new PEEditTextAttrSet();
        attr2.name="modi3";
        attr2.clearWhenOpenKbd=false;
        attr2.softkbdType=1;
        attr2.softkbdMode=1;
        attr2.kbdRandom=true;
        attr2.immersiveStyle=true;
        attr2.kbdVibrator=true;
        attr2.whenMaxCloseKbd=false;
        attr2.minLength=6;
        attr2.maxLength=6;
        attr2.encryptType=0;//登录密码
        attr2.inScrollView=false;
        ed_confirm_code.initialize(attr2,ModifyPayCodeActivity.this);//新初始化方法，用户交互体验更佳
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ed_confirm_code.onDestroy();
        ed_pay_code.onDestroy();
        ed_new_code.onDestroy();
    }

    public void initChanceDialog(String sum){
        //sum="3";
        // dialog.dismiss();
        final AlertDialog dialog=new AlertDialog.Builder(ModifyPayCodeActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.enter_chance);
        Button btn_enter= (Button) dialog.getWindow().findViewById(R.id.positive);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText("密码错误，您今日剩余的尝试机会为"+sum+"次");
        if (sum.equals("0")){
            tv.setText("购卡支付已锁定，请次日凌晨三点以后再使用");
            btn_enter.setText("确定");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    ModifyPayCodeActivity.this.finish();
                }
            });
        }else{
            btn_enter.setText("重新输入");
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(context,AuthenticationActivity.class));
                    dialog.dismiss();
                    //  showNewSixPeed();
                    ed_pay_code.clear();
                    ed_new_code.clear();
                    ed_confirm_code.clear();
                    ed_pay_code.requestFocus();
                    dialog.dismiss();
                    //PayConfirmActivity.this.HandleItMySelf("");

                }
            });
        }

    }
}
