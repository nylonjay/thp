package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;

public class BindCardSucceedActivity extends BaseActivity implements View.OnClickListener{
    Button btn_tobuy;
    private ImageView img1,img2;
    private TextView tv_last;
    private LinearLayout ll_back;
    private TextView tv_tosp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_bind_card_succeed);
        setTitleText(R.string.bind_card);
        setBackView(R.drawable.u194);
        initViews();
    }

    private void initViews() {
        tv_tosp= (TextView) findViewById(R.id.tv_tosp);
        tv_tosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindCardSucceedActivity.this,MyShoppingCardActivity.class));
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        tv_last= (TextView) findViewById(R.id.tv_last);
        tv_last.setText("成为"+"\"天虹"+"\"微信会员, 可通过微信将账户里的有效购物卡转赠给好友");
        img1= (ImageView) findViewById(R.id.img1);
        img2= (ImageView) findViewById(R.id.img2);
        btn_tobuy= (Button) findViewById(R.id.btn_tobuy);
        btn_tobuy.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_back) {
            BindCardSucceedActivity.this.finish();
        }else if (i==R.id.btn_tobuy){
            TianHongPayMentUtil.tianHongPayMentUtil.bindCardCallBack.onBindSucced();
            BindCardSucceedActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
