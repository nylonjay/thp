package thp.csii.com.activities;

import android.os.Bundle;
import android.view.View;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;

/**
*AssetsDetailsActivity 资产明细页面
*@author nylon
 * created at 2016/8/31 11:46
*/
public class AssetsDetailsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details);
        setBackView(R.drawable.u194);
        imageViewBack.setOnClickListener(this);
        setTitleText(R.string.assets_detail);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_basetitle_back) {
            AssetsDetailsActivity.this.finish();//dwdwd
            //wdk;wkd;lwk
        }
    }
}
