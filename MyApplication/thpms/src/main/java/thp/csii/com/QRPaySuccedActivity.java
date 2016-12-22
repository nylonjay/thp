package thp.csii.com;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QRPaySuccedActivity extends BaseActivity {
    private LinearLayout ll_back;
    private String amount;
    private TextView tv_amount;
    private Button btn_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpay_succed);
        setTitleText(R.string.pay_success);
        setBackView(R.drawable.u194);
        amount=getIntent().getStringExtra("amount");
        initViews();
        //nylon 2016.12.13
    }

    private void initViews() {
        btn_back2= (Button) findViewById(R.id.btn_back2);
        btn_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRPaySuccedActivity.this.finish();
                for (Activity a:TianHongPayMentUtil.pwdactivities){
                    a.finish();
                }
            }
        });
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        if (null!=amount){
            Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
            tv_amount.setTypeface(tf);
            tv_amount.setText("￥"+amount);
        }
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRPaySuccedActivity.this.finish();
            }
        });
    }
}
