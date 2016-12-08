package thp.csii.com.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.views.ShSwitchView;

/**
*InvoiceInfoActivity  发票信息界面
*@author nylon
 * created at 2016/9/9 17:06
*/
public class InvoiceInfoActivity extends BaseActivity {
    EditText ed_taitou;
    ShSwitchView ivo_check;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_info);
        setTitleText(R.string.invoice_info);

        initViews();
    }

    private void initViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceInfoActivity.this.finish();
            }
        });
        imageViewBack.setImageResource(R.drawable.u194);
        ed_taitou= (EditText) findViewById(R.id.ed_taitou);
        ivo_check= (ShSwitchView) findViewById(R.id.invo_check);
        ivo_check.requestFocus();
    }
}
