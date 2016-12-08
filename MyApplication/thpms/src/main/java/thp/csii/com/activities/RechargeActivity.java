package thp.csii.com.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
/**
*RechargeActivity  充值页面
*@author nylon
 * created at 2016/8/31 20:09
*/

public class RechargeActivity extends BaseActivity implements View.OnClickListener{
    Button Btnsubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initviews();

    }
    public void initDialog(){
        final AlertDialog dialog=new AlertDialog.Builder(RechargeActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.authen_dialog);
        dialog.getWindow().findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                dialog.dismiss();
            }
        });
        dialog.getWindow().findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RechargeActivity.this,AuthenticationActivity.class));
            }
        });
    }
    private void initviews() {
        Btnsubmit= (Button) findViewById(R.id.submit);
        Btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              initDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        
    }
}
