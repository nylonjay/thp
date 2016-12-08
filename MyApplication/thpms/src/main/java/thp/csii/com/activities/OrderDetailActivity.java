package thp.csii.com.activities;

import android.os.Bundle;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;

public class OrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitleText(R.string.order_detail);
    }
}
