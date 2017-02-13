package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PeedChangeListener;
import thp.csii.com.callback.peeditTextListener;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;

public class SetPayCode_First_Activity extends BaseTokenActivity {
    private LinearLayout ll_back;
    private LinearLayout ll_tvs;
    private PEEditText true_peed;
    private TextView[] peds;
    private TextView pe3;
    private TextView pe2;
    private TextView pe1;
    private TextView pe4;
    private TextView pe5;
    private TextView pe6;
    private String action;
    private TextView tv_cancle;
    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_set_pay_code__first_);
//        setTitleText(R.string.set_code);
        action=getIntent().getStringExtra("action");
        from=getIntent().getStringExtra("from");
        if (null!=from){
            if (from.equals("forget")){
                setTitleText(R.string.forget_code);
            }else{
                setTitleText(R.string.set_code);
            }
        }
        initViews();
        TianHongPayMentUtil.spcactivities.add(this);
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
                SetPayCode_First_Activity.this.finish();
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
        true_peed.initialize(attr,SetPayCode_First_Activity.this);//新初始化方法，用户
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
    }

    private Handler hand=new Handler(){
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public void handleMessage(Message msg) {
            Bundle b=msg.getData();
            switch (msg.what){
                case 9://输入玩6个字符
                    //去到第二次输入密码的页面
                    showDialog(true);
                    new Thread(sendable).start();
//
                    break;
//                case 1:
//                    break;
                case 5:
                    showDialog(false);
                    if (null!=token){
                        String accesdate=Long.toString(token.getAccessDate());
                        LogUtil.e(context,"set1==accessdate=="+accesdate);
                        Intent in=new Intent(SetPayCode_First_Activity.this,SetPayCode_Second_Activity.class);
                        in.putExtra("action",action);
                        in.putExtra("Hash1",true_peed.getHash());
                        in.putExtra("accesdate",accesdate);
                        in.putExtra("from",from);
                        startActivity(in);
                    }else{
                        showToastAutoDismiss("获取交易token失败");
                    }
                      // SetPayCode_First_Activity.this.finish();
                    break;
                case  500:
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

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        true_peed.onDestroy();
    }
    private Token token;
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                token=null;
                token=getAccessGenToken(hand);
            } catch (Exception e) {
                e.printStackTrace();
                showDialog(false);
                showToastAutoDismiss("服务器无响应,请重试");
            }
        }
    };
}
