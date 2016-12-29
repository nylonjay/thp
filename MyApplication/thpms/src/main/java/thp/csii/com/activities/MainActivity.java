package thp.csii.com.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csii.powerenter.PEEditText;
import com.csii.powerenter.PEEditTextAttrSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.THToast;
import thp.csii.com.BaseActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.adapter.MainGridAdapter;
import thp.csii.com.bean.MainBean;
import thp.csii.com.bean.TradeDetail;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.utils.HttpController;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;
import thp.csii.com.views.ActionSheet;

/**
 * @author nylon
 *         created at 2016/9/2 15:11
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    GridView gridViewMains;
    List<MainBean> beans;
    ActionSheet mActionSheet;
    private PEEditText password2;
    String uniqueId;
    private static final String PLAIN = "plain";
    private static final String STATUS_ERR = "4444";
    private static final String STATUS_OK = "0000";
    private static final String HTTP_CONTENT_TYPE_JSON_UTF = "application/json;charset=utf-8";
    // private ActionSheet wActionSheet;
    private LinearLayout mLinearCountDetail;
    private String pinTag;
    private TextView tv_counts, tv_cardcounts,tv_totalamt;
    private TextView tv_rmb;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private NumberFormat nf;
    private LinearLayout ll_back,ll_ok;
    private boolean locked=false;//账户是否锁定
    private String Accno="";//7800100000002282林再雄的账号 //7000290210000032307李锐的账号

    private String Oid="";
    private Double Amount;
    private String Mid="";
    private String userSign="";
    char symbol=165;
    //private String acclistJson;
    private String useraccno;
    private String userSigns;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // SetStatusColor();
        setContentView(R.layout.activity_main);
        setTitleText(R.string.shopping_card);
        getIntentData();
        setOkVisibity(true);
        setOkView(R.drawable.setting);
        setBackView(R.drawable.u194);
        initViews();
        loadView();
        TianHongPayMentUtil.activities.add(this);
        //startActivity(new Intent(MainActivity.this,QRCodeActivity.class));

    }



    @Override
    protected void onResume() {
        super.onResume();
        showProDialog();
        new Thread(thread).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();

    }


    private void getIntentData() {
        Accno=getIntent().getStringExtra("Accno");
        userSign=getIntent().getStringExtra("userSign");
    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 2:
                    //  CheckTHPayMentPayData();
                    break;
                case 3://显示红包余额 购物卡余额  科学计数转换成double显示
                    Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
                    Bundle b=msg.getData();
                    tv_rmb.setVisibility(View.VISIBLE);
                    tv_rmb.setTypeface(tf);
                    tv_rmb.setText("￥");
                    tv_counts.setTypeface(tf);
                    tv_cardcounts.setTypeface(tf);
                    tv_counts.setText("￥"+String.format("%.2f",Math.abs(b.getDouble("balamt"))));
                    tv_cardcounts.setText("￥"+String.format("%.2f",b.getDouble("cardamt")));
                    tv_totalamt.setText(""+String.format("%.2f",b.getDouble("totalamt")));
                    break;
                case 404:
                    dismissDialog();
                    ToastUtil.shortNToast(MainActivity.this,"查询账户信息失败");
                    break;

            }
        }
    };

    private void getData() {
        Map<String, Object> ajaxData = new HashMap();
        User user = new User();
        user.setAcno(Accno);
        PainObj painObj = new PainObj(user, null);
        painObj.setUserSign(userSign);
        LogUtil.e(MainActivity.this,"Accno=="+Accno+"userSign==="+userSign);
        //end
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            //  ToastUtil.shortToast(context, SharePreferencesUtils.getStringValue(context,"Cookie"));
            Log.i("err", "Cookie缓存===" + SharePreferencesUtils.getStringValue(context, "Cookie"));
            Log.i("err", "SESSIONID==" + SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
            ajaxData.put("status", STATUS_OK);
            ajaxData.put("redirectUrl", url);
            hand.sendEmptyMessage(1);
        } catch (Exception e) {
            ajaxData.put("status", STATUS_ERR);
            ajaxData.put("errmsg", e.getMessage());
            hand.sendEmptyMessage(404);
            System.err.println("授权登录发生错误!" + e.getMessage());
        }
    }

    Runnable thread = new Runnable() {
        @Override
        public void run() {
            getData();

        }
    };

    private void loadView() {
        beans = new ArrayList<MainBean>();

//        MainBean b2 = new MainBean("购买购物卡", "短信验证");
//        beans.add(b2);
        MainBean b1 = new MainBean("我的购物卡", "我的购物卡");
        beans.add(b1);
        MainBean b3 = new MainBean("绑定购物卡", "绑定购物卡");
        beans.add(b3);

        MainBean b4 = new MainBean("交易明细", "充值");
        beans.add(b4);
        MainBean b5 = new MainBean("买卡记录", "收银台");
        beans.add(b5);
        MainBean b6 = new MainBean("帮助中心", "交易明细");
        beans.add(b6);
//        MainBean b7=new MainBean("交易预判","交易预判");
//        beans.add(b7);


        MainGridAdapter mainGridAdapter = new MainGridAdapter(context, beans);
        gridViewMains.setAdapter(mainGridAdapter);
    }


    private void initViews() {
        tv_rmb= (TextView) findViewById(R.id.tv_rmb);
        nf=NumberFormat.getInstance();
        nf.setGroupingUsed(false);
// 设置数的小数部分所允许的最小位数
        nf.setMinimumFractionDigits(0);
// 设置数的小数部分所允许的最大位数
        nf.setMaximumFractionDigits(2);
//        mLinearCountDetail= (LinearLayout) findViewById(R.id.countDetails);
//        mLinearCountDetail.setOnClickListener(this);
        tv_counts = (TextView) findViewById(R.id.tv_counts);
        tv_cardcounts = (TextView) findViewById(R.id.tv_cardcounts);
        tv_totalamt= (TextView) findViewById(R.id.tv_totalamt);
        gridViewMains = (GridView) findViewById(R.id.GridViewMains);
        gridViewMains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in;
                if (position==1){
                    in = new Intent(MainActivity.this, BindShoppingCardActivity.class);
                    if (!locked){
                        if (!TianHongPayMentUtil.CodeSetted){
                            String action="bind";
                            initPayNotSettedDialog(getResources().getString(R.string.code_not_setted),action);
                        }else {
                            startActivity(in);
                        }
                    }else{
                        ToastUtil.shortToast(MainActivity.this,"账户已被锁定,请先进行解锁操作");
                    }

                }else if (position==4){
                    if (!locked){

                        in = new Intent(MainActivity.this, BaseWebActivity.class);
                        in.putExtra("url",Constant.HELP_CENTER);
                        in.putExtra("name","帮助中心");
                        startActivity(in);
                    }else{
                        ToastUtil.shortNToast(context,"账户已锁定");
                    }
                }else if (position==0){
                    if (!locked){
                        in = new Intent(MainActivity.this, MyShoppingCardActivity.class);
                        //    in.putExtra("accList",acclistJson);
                        startActivity(in);
                    }else{
                        ToastUtil.shortNToast(context,"账户已锁定");
                    }

                }else if (position==2){
                    if (!locked){
                        in = new Intent(MainActivity.this, TradeDetailActivity.class);
                        startActivity(in);
                    }else{
                        ToastUtil.shortNToast(context,"账户已锁定");
                    }
                }else if (position==3){
                    if (!locked){
                        in = new Intent(MainActivity.this, BuyCardRecordActivity.class);
                        //    in.putExtra("accList",acclistJson);
                        startActivity(in);
                    }else{
                        ToastUtil.shortNToast(context,"账户已锁定");
                    }
                }
            }
        });
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_ok= (LinearLayout) findViewById(R.id.ll_ok);
        ll_back.setOnClickListener(this);
        ll_ok.setOnClickListener(this);
        mActionSheet = new ActionSheet(this);
        mActionSheet.addMenuItem("修改支付密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locked){
                    if (!TianHongPayMentUtil.CodeSetted){
                        initPayNotSettedDialog(getResources().getString(R.string.code_not_setted),"modifycode");
                    }else {
                        startActivity(new Intent(MainActivity.this, ModifyPayCodeActivity.class));
                    }
                }else {
                    ToastUtil.shortToast(MainActivity.this,"账户已被锁定,请先进行解锁操作");
                }

            }
        });
        mActionSheet.addMenuItem("忘记支付密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if (!TianHongPayMentUtil.CodeSetted){
                //startActivity(new Intent(MainActivity.this, SetFastPayCodeActivity.class));
                if (!locked){
                    if (!TianHongPayMentUtil.CodeSetted){
                        initPayNotSettedDialog(getResources().getString(R.string.code_not_setted),"forgetcode");
                    }else {
                        Intent in=new Intent(MainActivity.this,SetPayCode_First_Activity.class);
                        in.putExtra("action","forgetcode");
                        in.putExtra("from","forget");
                        startActivity(in);
                    }
                }else {
                    ToastUtil.shortToast(MainActivity.this,"账户已被锁定,请先进行解锁操作");
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent in;
        int i = v.getId();
        if (i == R.id.ll_back) {
            for (Activity a:TianHongPayMentUtil.activities){
                a.finish();
            }
            TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished("");
            // wActionSheet.show();

        } else if (i == R.id.ll_ok) {
            mActionSheet.show();

        }

    }


    private void QryCountDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                dismissDialog();
                Double balamt;
                Double cardamt = 0.0;
                Double acye=0.0;
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");

                try {
                    if (null != res) {
                        if (res.getString("status").equals("0000")) {

                            JSONObject datamap = res.getJSONObject("dataMap");
                            if (null != datamap) {
                                JSONObject rsvc = datamap.getJSONObject("rsvc");
                                TianHongPayMentUtil.currentTel=rsvc.getString("mobile");
                                // balamt = Double.parseDouble(rsvc.getString("balAmt"));//账户总余额
                                balamt = nf.parse(rsvc.getString("balAmt")).doubleValue();
                                //  nf.format(nf.parse(rsvc.getString("balAmt")).doubleValue());
                                pinTag = rsvc.getString("pinTag");
                                if (pinTag.equals("00")) {
                                    TianHongPayMentUtil.CodeSetted = false;
                                    //ToastUtil.shortToast(context,"并未设置支付密码");
                                } else if (pinTag.equals("01")) {
                                    //ToastUtil.shortToast(context,"已设置支付密码");
                                    TianHongPayMentUtil.CodeSetted = true;
                                }
                                JSONObject acclist = rsvc.getJSONObject("accList");
                                //    acclistJson=acclist.toJSONString();
                                LogUtil.e(MainActivity.this,acclist.toJSONString());
                                if (null != acclist) {
                                    JSONArray account = acclist.getJSONArray("account");
                                    for (int i = 0; i < account.size(); i++) {
                                        //  cardamt += Double.parseDouble(account.getJSONObject(i).getString("balAmt"));
                                        if (!account.getJSONObject(i).getString("accno").equals(TianHongPayMentUtil.currentUser.getAcno())){

                                            cardamt += nf.parse(account.getJSONObject(i).getString("balAmt")).doubleValue();
                                        }else{
                                            acye+=nf.parse(account.getJSONObject(i).getString("balAmt")).doubleValue();
                                        }
                                    }//将所有卡的余额相加
                                    Message msg = new Message();
                                    Bundle b = new Bundle();
                                    b.putDouble("balamt", (acye));
                                    b.putDouble("cardamt", cardamt);
                                    b.putDouble("totalamt", balamt);
                                    msg.setData(b);
                                    msg.what = 3;
                                    hand.sendMessage(msg);
                                }
                            }
                            locked=false;
                        } else if ("05".equals(res.getString("errcode"))){
                            tv_rmb.setVisibility(View.GONE);
                            ToastUtil.shortToast(MainActivity.this,res.getString("errmsg"));
                            locked=true;
                            closeALLActvivities();
                            TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished("支付功能已锁定");
                        }else{
                            locked=false;
                            ToastUtil.shortToast(context,res.getString("errmsg"));
                            if ("00013".equals(res.getString("errcode"))){
                                //session过期弹出操作失败弹框
                              //  initSessionOutTime("操作失败"+("00013"));
                                ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                                //  TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished();
                            }

                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onError(Object o) {
                dismissDialog();
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://thp.csii.com.activities/http/host/path")
//        );
        //   AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://thp.csii.com.activities/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished("");
    }
}
