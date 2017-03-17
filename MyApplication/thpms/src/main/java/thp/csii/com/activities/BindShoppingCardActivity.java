package thp.csii.com.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditTextAttrSet;
import com.csii.powerenter.PEEncryptUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.LongedListener;
import thp.csii.com.callback.peeditTextListener;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.THProgressDialog;

/**
 *BindShoppingCardActivity 购物卡绑定页面
 *@author nylon
 * created at 2016/8/31 20:10
 */

public class BindShoppingCardActivity extends BaseTokenActivity {
    TextView tvAgreement;
    Button bind,unbind;
    Token token;
    com.csii.powerenter.PEEditText password;
    EditText ed_card;
    private String action="bind";
    private String PublicKey="81aef1e76b001c3bc19a0c632874b9ccf35731ea60f38fed4ac65067b2af97c4aec11d73692eecd51e6f4b8adeb41ae8983af769c28aa4ade9b91f24c01777b2529acbd5309ea727becbc916dcfa953bd3968897c710d18ba96c389ce64cbd9095e4542d6ffed62c990294aef14dad5a719f8c6cf092f1bc6d25f563af900afd";
    private int Setting_Pay_Code=1;
    private LinearLayout ll_back;
    private CheckBox check_agree;
    cn.rainbow.thbase.ui.THProgressDialog bindProgressDialog=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_bind_shopping_card);
        setTitleText(R.string.bind_card);
        setBackView(R.drawable.u194);
        initViews();

    }
    //    Thread td=new Thread(){
//        @Override
//        public void run() {
//            try {
//                token=getAccessLoginToken();
//                Message msg=new Message();
//                msg.obj=token.getUniqueId();
//                handler.sendMessage(msg);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                token=null;
                token=getAccessGenToken(handler);
            } catch (Exception e) {
                e.printStackTrace();
                showBindDialog(false);
                Btn_Clickable();
                ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,"交易授权失败");
            }

        }
    };
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
//                    if (!TianHongPayMentUtil.CodeSetted){
//                        startActivityForResult(new Intent(BindShoppingCardActivity.this,Message_PayCode_Activity.class),Setting_Pay_Code);
//                        return;
//                    }
                    if (null!=token&&action.equals("bind")){
                        BindShoppingCard(HttpUrls.bindCardConfirm);
                    }

                    break;
                case 9:
                    if (check_agree.isChecked()){
                        if (password.validity_check()!=0){
                            bind.setBackgroundResource(R.drawable.next_step_gray);
                            bind.setClickable(false);
                        }else{
                            if (ed_card.length()>=19){
                                bind.setBackgroundResource(R.drawable.net_step_bg);
                                bind.setClickable(true);
                            }else{
                                bind.setBackgroundResource(R.drawable.next_step_gray);
                                bind.setClickable(false);
                                // ToastUtil.shortToast(BindShoppingCardActivity.this,"卡号的长度非法");
                            }
                        }
                    }else{
                        bind.setBackgroundResource(R.drawable.next_step_gray);
                        bind.setClickable(false);
                    }
                    break;
            }
            //ToastUtil.shortToast(context,"获取到的Tokenuniqueid是"+msg.obj);
        }
    };

    void Btn_Clickable(){
        bind.setBackgroundResource(R.drawable.net_step_bg);
        bind.setClickable(true);
    }
    void Btn_InClickable(){
        // bind.setBackgroundResource(R.drawable.next_step_gray);
        bind.setClickable(false);
    }


    private void initViews() {
        check_agree= (CheckBox) findViewById(R.id.check_agree);
        check_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handler.sendEmptyMessage(9);
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindShoppingCardActivity.this.finish();
            }
        });
        ed_card= (EditText) findViewById(R.id.ed_name);
        ed_card.addTextChangedListener(new LongedListener(ed_card,handler));
        password= (com.csii.powerenter.PEEditText) findViewById(R.id.ed_number);
        password.addTextChangedListener(new peeditTextListener(password,handler));
        PEEditTextAttrSet attr=new PEEditTextAttrSet();
        attr.name="bindcard1";
        attr.clearWhenOpenKbd=false;
        attr.softkbdType=1;
        attr.softkbdMode=1;
        attr.immersiveStyle=true;
        attr.kbdRandom=true;
        attr.kbdVibrator=true;
        attr.whenMaxCloseKbd=true;
        attr.minLength=6;
        attr.maxLength=6;
        attr.encryptType=0;//登录密码
        attr.inScrollView=false;
        password.initialize(attr);//新初始化方法，用户交互体验更佳

        tvAgreement= (TextView) findViewById(R.id.agreement);
        tvAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(BindShoppingCardActivity.this, BaseWebActivity.class);
                in.putExtra("url",Constant.TH_AGREEMENT);
                in.putExtra("name","虹支付使用协议");
                startActivity(in);
                //  BindShoppingCardActivity.this.finish();
            }
        });
        bind= (Button) findViewById(R.id.submit);
        unbind= (Button) findViewById(R.id.unbind);
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBindDialog(true);
                action="bind";
                Btn_InClickable();
                new Thread(sendable).start();

                // BindShoppingCard();
            }
        });
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="unbind";
                new Thread(sendable).start();
            }
        });
    }

    private void BindShoppingCard(String mUrl) {
        int v_check=password.validity_check();
        if(v_check==0){
            LogUtil.i(BindShoppingCardActivity.this,"密码格式正确");
        }else {
            if(v_check==-1){
                Toast.makeText(BindShoppingCardActivity.this,"密码为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(v_check==-2){
                Toast.makeText(BindShoppingCardActivity.this,"密码长度小于最小长度", Toast.LENGTH_SHORT).show();
                return;
            }
            if(v_check==-3){
                Toast.makeText(BindShoppingCardActivity.this,"密码内容不合法", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        final Map<String, String> param = new HashMap<String, String>();
//        Date date = new Date();
//        Long  msec = date.getTime();
        String timeStamp = Long.toString(token.getAccessDate());
        // cardnum.setPublicKey(MyApp.PUBLICKEY);
        password.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
        LogUtil.e(TianHongPayMentUtil.CurrentContext,password.getValue(timeStamp));
        param.put("pin_data", password.getValue(timeStamp));
        param.put("pan",ed_card.getText().toString());
        param.put("resToken",token.getUniqueId());
        String url =Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showBindDialog(false);
                Btn_Clickable();
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (null!=res) {
                    if ("0000".equals(res.getString("status"))){
                        if (action.equals("bind")){
                            startActivity(new Intent(BindShoppingCardActivity.this,BindCardSucceedActivity.class));
                            BindShoppingCardActivity.this.finish();
                        }
                        else{
                            showMyToastAutoDismiss("解除绑定购物卡成功",handler);
                        }
                    }else if ("4444".equals(res.getString("status"))){
                        JSONObject dataMap=res.getJSONObject("dataMap");

//                        JSONObject rsvc=dataMap.getJSONObject("rsvc");
//                        if (null!=dataMap){
//                            String pinRetry=dataMap.getString("pinRetry");
//                        }
                        //w4代表购物卡已绑定
                        if ("55".equals(res.getString("errcode"))){
                            initChanceDialog(dataMap.getString("pinRetry"));
                        }

                        if ("36".equals(res.getString("errcode"))){
                            //  initChanceDialog("0");
                            ToastUtil.shortNToast(BindShoppingCardActivity.this,res.getString("errmsg"));
                        }
                        ToastUtil.shortToast(context,res.getString("errmsg"));
                        LogUtil.i(context,res.toString());

                        if ("00013".equals(res.getString("errcode"))){
                            //session过期弹出操作失败弹框
                            initSessionOutTime("操作失败"+("00013"));
                        }
                        // showMyToastAutoDismiss(res.getString("errmsg"));
                        ToastUtil.shortNToast(BindShoppingCardActivity.this,res.getString("errmsg"));
                    }

                }


            }

            @Override
            public void onError(Object o) {
                showBindDialog(false);
                Btn_Clickable();
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
            tv.setText("购物卡已锁定，请次日凌晨3点后再使用");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bindProgressDialog=null;
        password.onDestroy();
    }

    protected void showBindDialog(boolean isShow) {

        if (bindProgressDialog == null && isShow) {
            bindProgressDialog = cn.rainbow.thbase.ui.THProgressDialog.createDialog(BindShoppingCardActivity.this);
            bindProgressDialog.setMessage(R.string.loading);
        }

        if (bindProgressDialog != null) {

            if (isShow) {
                bindProgressDialog.show();
            } else {
                bindProgressDialog.dismiss();
            }
        }
    }


}


