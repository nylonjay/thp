package thp.csii.com.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitleText(R.string.order_detail);
        voucher=getIntent().getStringExtra("voucher");
        showDialog(true);
        OrderDetail(HttpUrls.GetCardDetails);
    }

    Handler handle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            showDialog(false);
            switch (msg.what){
                case 200:
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
