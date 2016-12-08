package thp.csii.com.activities;

import android.os.Bundle;
import android.view.View;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;

/**
*AuthenticationActivity 实名认证页面
*@author nylon
 * created at 2016/8/31 17:41
*/

public class AuthenticationActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setTitleText(R.string.authentication);
        setBackView(R.drawable.u236);
        initViews();
    }

    private void initViews() {
        imageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_basetitle_back) {
            AuthenticationActivity.this.finish();

        }

    }
}
