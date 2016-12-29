package thp.csii.com.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.bean.CardRecord;
import thp.csii.com.bean.RecordDetail;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class OrderDetailActivity extends BaseActivity {
    private String voucher;
    private RecordDetail rd;
    private TextView tv_time,tv_price,tv_fp;
    private String invouses;
    private LinearLayout ll_back;
    private TextView tv_orderno,tv_tradeno,tv_createtime;
    private CardRecord cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitleText(R.string.order_detail);
        setBackView(R.drawable.u194);
        cr= (CardRecord) getIntent().getSerializableExtra("cr");
        voucher=cr.getVoucher();
        invouses=cr.getInvoice();
        showDialog(true);
        initViews();
        OrderDetail(HttpUrls.GetCardDetails);
    }

    private void initViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailActivity.this.finish();
            }
        });
        tv_time= (TextView) findViewById(R.id.tv_time);
        tv_price= (TextView) findViewById(R.id.tv_price);
        tv_fp= (TextView) findViewById(R.id.tv_fp);
        tv_orderno= (TextView) findViewById(R.id.tv_orderno);
        tv_tradeno= (TextView) findViewById(R.id.tv_tradeno);
        tv_createtime= (TextView) findViewById(R.id.tv_creaetime);
        tv_orderno.setText(cr.getVoucher());
        tv_tradeno.setText(cr.getCard_num());

    }

    Handler handle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            showDialog(false);
            switch (msg.what){
                case 200:
                    tv_time.setText(rd.getCtime());
                    Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
                    tv_price.setTypeface(tf);
                    tv_price.setText("￥"+rd.getTrs_amt());
                    if (invouses.isEmpty()){
                        tv_fp.setText("不开发票");
                    }else{
                        tv_fp.setText(invouses);
                    }
                    break;
                case 404:
                    break;
            }
        }
    };
    private void OrderDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl+"?"+"voucher="+voucher;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                LogUtil.e(OrderDetailActivity.this,json.toJSONString());
                JSONObject res=json.getJSONObject("res");
                if (res.getString("status").equals("0000")){
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    rd=JSON.parseObject(dataMap.toJSONString(),RecordDetail.class);
                    handle.sendEmptyMessage(200);
                }else{
                    ToastUtil.shortNToast(context,res.getString("errmsg"));
                    if ("00036".equals(res.getString("errcode"))){
                        Message msg=new Message();
                        msg.what=404;
                        msg.obj=res.getString("errmsg");
                    }

                }
            }
            @Override
            public void onError(Object o) {
                showDialog(false);
                Log.i("res err", "" + o.toString());
            }
        });
    }
}
