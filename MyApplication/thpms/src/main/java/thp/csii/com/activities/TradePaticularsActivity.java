package thp.csii.com.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.bean.TradeDetail;

public class TradePaticularsActivity extends BaseActivity {
    TextView action,actionsum,time,tvTime,order,orderNum;
    TradeDetail td;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_trade_paticulars);
        getData();
        setTitleText(R.string.jyxq);
        initViews();
    }

    private void getData() {
        Intent in=getIntent();
        td= (TradeDetail) in.getSerializableExtra("td");
    }

    private void initViews() {
        imageViewBack.setImageResource(R.drawable.u194);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradePaticularsActivity.this.finish();
            }
        });
        action= (TextView) findViewById(R.id.action);
        actionsum= (TextView) findViewById(R.id.actionsum);
        time= (TextView) findViewById(R.id.time);
        tvTime= (TextView) findViewById(R.id.tvTime);
        order= (TextView) findViewById(R.id.order);
        orderNum= (TextView) findViewById(R.id.orderNum);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
        actionsum.setTypeface(tf);
        if (td.getTxnId().equals("2")){
            action.setText("消费金额");
            time.setText("消费时间");
            actionsum.setText("￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
        }else if (td.getTxnId().equals("3")){
            action.setText("退款金额");
            time.setText("退款时间");
            actionsum.setText("￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
        }else if (td.getTxnId().equals("4")){
            action.setText("退款金额");
            time.setText("退款时间");
            actionsum.setText("￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
        }

        tvTime.setText(td.getTxnDate());
        orderNum.setText(td.getVoucher());
//        actionsum.setText(td.getSum());
//        tvTime.setText(td.getDateAndTime());
    }


}
