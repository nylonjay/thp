package thp.csii.com.activities;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__set__pre);
        setTitleText(R.string.pay_set);
        setBackView(R.drawable.u194);
        initViews();
        TianHongPayMentUtil.pwdactivities.add(this);
    }

    private void initViews() {
        re_stop= (RelativeLayout) findViewById(R.id.re_stop);
        re_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Pay_Set_Pre.this,InputPwdOpenActivity.class);
                in.putExtra("from","stop");
                startActivity(in);
            }
        });
        re_3= (RelativeLayout) findViewById(R.id.re3);
        re_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Pay_Set_Pre.this,InputPwdOpenActivity.class);
                in.putExtra("from","set");
                startActivity(in);
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay_Set_Pre.this.finish();
            }
        });
    }
}
