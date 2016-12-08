package thp.csii.com;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import thp.csii.com.activities.AuthenticationActivity;
import thp.csii.com.activities.SetFastPayCodeActivity;
import thp.csii.com.activities.SetPayCode_First_Activity;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.views.SystemBarTintManager;
import thp.csii.com.views.THProgressDialog;
import thp.csii.com.views.UserDefinedDialog;

/**
 *BaseActivity 基本的activity
 *@author nylon
 * created at 2016/8/31 11:47
 */
public class BaseActivity extends AppCompatActivity {
    private RelativeLayout llRoot;
    private LinearLayout llBasetitleBack;
    private TextView tvBasetitleTitle;
    public ImageView tvBasetitleOK;
    public ImageView imageViewBack;
    public TextView tv_cancle;
    public Context context;
    public Map<String,String> staticTradeMap=null;
    private cn.rainbow.thbase.ui.THProgressDialog mTHProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        //push test


        findView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏与标题栏颜色保持一致
            tintManager.setStatusBarTintResource(R.color.trans_black);
        }
//        staticTradeMap=new HashMap<String, String>();
//        staticTradeMap.put("txn_id","04 红领巾");
//        staticTradeMap.put("chnl","04");
//        staticTradeMap.put("accno","18788520844");//账号
//        staticTradeMap.put("systrace","04");
//        staticTradeMap.put("acq_id","");
//        staticTradeMap.put("org_code","00195");
//        staticTradeMap.put("teller","001");

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
                // startActivity(new Intent(context,AuthenticationActivity.class));
            }
        });
    }

    public void initWaitDialog(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
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
                // startActivity(new Intent(context,AuthenticationActivity.class));

                dialog.dismiss();
            }
        });
    }

    public void initSessionOutTime(String sum){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();

        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText(sum);
        dialog.getWindow().findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                if (null!=dialog){
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(context,AuthenticationActivity.class));
                for (Activity a:TianHongPayMentUtil.spcactivities){
                    a.finish();
                }
                TianHongPayMentUtil.activities.get(0).finish();
                if (null!=dialog){
                    dialog.dismiss();
                }
            }
        });
    }
    public void showProDialog(){
        if (null!=TianHongPayMentUtil.thProgressDialog){
            TianHongPayMentUtil.thProgressDialog.show();
        }
    }
    public void closeALLActvivities(){
        for (Activity a:TianHongPayMentUtil.spcactivities){
            a.finish();
        }
        for (Activity a:TianHongPayMentUtil.activities){
            a.finish();
        }
    }


    public void dismissDialog(){
        if (null!=TianHongPayMentUtil.thProgressDialog)
            TianHongPayMentUtil.thProgressDialog.dismiss();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void SetStatussssColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.trans_black));
        }
    }

    public void initPayNotSettedDialog(String sum, final String action){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();

        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
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
                // startActivity(new Intent(context,AuthenticationActivity.class));
                Intent in=new Intent(context, SetPayCode_First_Activity.class);
                in.putExtra("from","set");
                in.putExtra("action",action);
                startActivity(in);
                dialog.dismiss();
            }
        });
    }

    private void findView() {
        tv_cancle= (TextView) findViewById(R.id.tv_cancle);
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
    public void setTitleString(String s){
        if (tvBasetitleTitle != null)
            tvBasetitleTitle.setText(s);
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

    public void showMyToastAutoDismiss(String msg, final Handler handler){
        final UserDefinedDialog dia=  new UserDefinedDialog(this, msg, null, null);
        dia.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dia.dismiss();
                handler.sendEmptyMessage(500);
            }
        },1500);
    }
    protected void showDialog(boolean isShow) {

        if (mTHProgressDialog == null && isShow) {
            mTHProgressDialog = cn.rainbow.thbase.ui.THProgressDialog.createDialog(this);
            mTHProgressDialog.setMessage(R.string.loading);
        }

        if (mTHProgressDialog != null) {

            if (isShow) {
                mTHProgressDialog.show();
            } else {
                mTHProgressDialog.dismiss();
            }
        }
    }

}