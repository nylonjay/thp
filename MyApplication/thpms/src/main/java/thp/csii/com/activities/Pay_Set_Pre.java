package thp.csii.com.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;

public class Pay_Set_Pre extends BaseActivity {
    private LinearLayout ll_back;
    private RelativeLayout re_3,re_stop,re_2;
    private String pf_hwm,pay_hwm,day_hwm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__set__pre);
        setTitleText(R.string.pay_set);
        setBackView(R.drawable.u194);
        pf_hwm=getIntent().getStringExtra("pf_hwm");
        pay_hwm=getIntent().getStringExtra("pay_hwm");
        day_hwm=getIntent().getStringExtra("day_hwm");
        initViews();
        TianHongPayMentUtil.pwdactivities.add(this);
    }

    private void initViews() {
        re_stop= (RelativeLayout) findViewById(R.id.re_stop);
        re_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re_stop.setClickable(false);
                Intent in=new Intent(Pay_Set_Pre.this,InputPwdOpenActivity.class);
                in.putExtra("from","stop");
                startActivity(in);
            }
        });
        re_3= (RelativeLayout) findViewById(R.id.re3);
        re_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re_3.setClickable(false);
                Intent in=new Intent(Pay_Set_Pre.this,InputPwdOpenActivity.class);
                in.putExtra("from","set");
                startActivity(in);
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(false);
                Pay_Set_Pre.this.finish();
            }
        });
    }
    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (!re_stop.isClickable()){
                        re_stop.setClickable(true);
                    }
                    if (!re_3.isClickable()){
                        re_3.setClickable(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        han.sendEmptyMessageDelayed(1,1000);
    }
}
