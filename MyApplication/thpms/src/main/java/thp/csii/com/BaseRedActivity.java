package thp.csii.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import thp.csii.com.activities.AuthenticationActivity;
import thp.csii.com.views.SystemBarTintManager;

public class BaseRedActivity extends AppCompatActivity {
    private RelativeLayout llRoot;
    private LinearLayout llBasetitleBack;
    private TextView tvBasetitleTitle;
    public ImageView tvBasetitleOK;
    public ImageView imageViewBack;
    public Context context;
    public Map<String,String> staticTradeMap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_red);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        findView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏与标题栏颜色保持一致
            tintManager.setStatusBarTintResource(R.color.base_red);
        }
    }

    public void initDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.authen_dialog);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText(sum);
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
                startActivity(new Intent(context,AuthenticationActivity.class));
            }
        });
    }

    private void findView() {
        llRoot = (RelativeLayout) findViewById(R.id.ll_basetitle_root);
        llBasetitleBack = (LinearLayout) findViewById(R.id.ll_basetitle_back);
        tvBasetitleTitle = (TextView) findViewById(R.id.tv_basetitle_title);
        tvBasetitleOK = (ImageView) findViewById(R.id.tv_basetitle_ok);
        imageViewBack= (ImageView) findViewById(R.id.tv_basetitle_back);
    }

    /**
     重点是重写setContentView，让继承者可以继续设置setContentView
     * 重写setContentView
     * @param resId
     */
    @Override
    public void setContentView(int resId) {
        View view = getLayoutInflater().inflate(resId, null);
        context=this;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.ll_basetitle);
        if (null != llRoot)
            llRoot.addView(view, lp);
    }

    /**
     *
     * 设置中间标题文字
     * @param resId
     */
    public void setTitleText(int resId) {
        if (tvBasetitleTitle != null)
            tvBasetitleTitle.setText(resId);
    }

//    /**
//     * 设置右边标题
//     * @param c
//     */
//    public void setOKText(CharSequence c) {
//        if (tvBasetitleOK != null)
//            tvBasetitleOK.setText(c);
//    }

    /**
     * 设置右边按钮是否显示
     * @param visible
     */
    public void setOkVisibity(boolean visible) {
        if (tvBasetitleOK != null) {
            if (visible)
                tvBasetitleOK.setVisibility(View.VISIBLE);
            else
                tvBasetitleOK.setVisibility(View.GONE);
        }
    }
    public void setOkView(int res){
        tvBasetitleOK.setImageResource(res);
    }
    public void setBackView(int res){
        imageViewBack.setImageResource(res);
    }


    public LinearLayout getLlBasetitleBack() {
        return llBasetitleBack;
    }


    public TextView getTvBasetitleTitle() {
        return tvBasetitleTitle;
    }



    public ImageView getTvBasetitleOK() {
        return tvBasetitleOK;
    }
}
