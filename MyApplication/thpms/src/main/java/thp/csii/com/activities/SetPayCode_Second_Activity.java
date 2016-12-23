package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.callback.peeditTextListener;
import thp.csii.com.http.Constant;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class SetPayCode_Second_Activity extends BaseTokenActivity {
    private String Hash2;
    private PEEditText true_peed;
    private TextView[] peds;
    private TextView pe3;
    private TextView pe2;
    private TextView pe1;
    private TextView pe4;
    private TextView pe5;
    private TextView pe6;
    private LinearLayout ll_back;
    private String action;
    private LinearLayout ll_container;
    private String accesdate;
    private String Hash1;
    private TextView tv_cancle;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_set_pay_code__second_);
        from=getIntent().getStringExtra("from");
        if (null!=from){
            if (from.equals("forget")){
                setTitleText(R.string.forget_code);
            }else{
                setTitleText(R.string.set_code);
            }
        }
        setTitleText(R.string.set_code);
     //   imageViewBack.setImageResource(R.drawable.u194);

        action=getIntent().getStringExtra("action");
        accesdate=getIntent().getStringExtra("accesdate");
        Hash1=getIntent().getStringExtra("Hash1");
        initViews();
        TianHongPayMentUtil.spcactivities.add(this);
    }

    private void initViews() {
        tv_cancle= (TextView) findViewById(R.id.tv_cancle);
        tv_cancle.setVisibility(View.VISIBLE);
        ll_container= (LinearLayout) findViewById(R.id.ll_tv_container);
        ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv:peds){
                    tv.setText("");
                }
                true_peed.openPEKbd();
                true_peed.clear();
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // initCancleDialog("是否要放弃设置支付密码?");
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }
            }
        });
        initpess();
    }

    private void initpess() {
        true_peed= (PEEditText)findViewById(R.id.true_peed);
        PEEditTextAttrSet attr=new PEEditTextAttrSet();
        attr.name="set2";
        attr.clearWhenOpenKbd=true;
        attr.softkbdType=1;
        attr.softkbdMode=1;
        attr.kbdRandom=true;
        attr.immersiveStyle=true;
        attr.kbdVibrator=true;
        attr.whenMaxCloseKbd=true;
        attr.minLength=0;
        attr.maxLength=6;
        attr.encryptType=0;//登录密码
        attr.inScrollView=false;
        true_peed.initialize(attr,SetPayCode_Second_Activity.this);//新初始化方法，用户
        true_peed.addTextChangedListener(new PeedChangeListener(true_peed,hand));
        true_peed.openPEKbd();

        peds=new TextView[6];
        pe1= (TextView)findViewById(R.id.password_edit1);
        pe2= (TextView)findViewById(R.id.password_edit2);
        pe3= (TextView)findViewById(R.id.password_edit3);
        pe4= (TextView)findViewById(R.id.password_edit4);
        pe5= (TextView) findViewById(R.id.password_edit5);
        pe6= (TextView) findViewById(R.id.password_edit6);
        peds[0]=pe1;
        peds[1]=pe2;
        peds[2]=pe3;
        peds[3]=pe4;
        peds[4]=pe5;
        peds[5]=pe6;
    }

    private String code2;
    private Handler hand=new Handler(){
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 9://输入玩6个字符
                    LogUtil.e(context,"set2==accessdate=="+accesdate);
                    true_peed.setPublicKey(TianHongPayMentUtil.PUBLICKEY);
                    code2=true_peed.getValue(accesdate);
                    Hash2=true_peed.getHash();
                    //去到第二次输入密码的页面
                    if (Hash1.equals(Hash2)){
                        hand.sendEmptyMessage(5);

                    }
                    else {
                        ToastUtil.shortNToast(context,"两次输入的密码不一致");
                        true_peed.openPEKbd();
                        true_peed.clear();
                        for (TextView tv:peds){
                            tv.setText("");
                        }
                    }
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
                case 5:
                  //  String timestamp=String.valueOf(token.getAccessDate());
                    Intent in=new Intent(SetPayCode_Second_Activity.this,MessageAuthActivity.class);
                    in.putExtra("pin_data",code2);
                    //in.putExtra("resToken",token.getUniqueId());
                    in.putExtra("from",from);
                    in.putExtra("action",action);
                   startActivity(in);
                    break;

            }
        }
    };

    public void initCancleDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
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
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }

                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        true_peed.onDestroy();
    }

}
