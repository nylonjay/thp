package thp.csii.com;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QRPaySuccedActivity extends BaseActivity {
    private LinearLayout ll_back;
    private String amount;
    private TextView tv_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpay_succed);
        setTitleText(R.string.pay_success);
        setBackView(R.drawable.u194);
        amount=getIntent().getStringExtra("amount");
        initViews();
    }

    private void initViews() {
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