package thp.csii.com.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;

public class Qry_RevokeActivity extends BaseTokenActivity {
    Token token;
    Button mBtnrevoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qry__revoke);
        setTitleText(R.string.qryrevoke);
        initviews();
        //OrderTracking(HttpUrls.orderTracking);//订单查询
    }
    private void initviews() {
        mBtnrevoke= (Button) findViewById(R.id.btn_revoke);
        mBtnrevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(sendable).start();//订单撤销
            }
        });
    }

    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token=null;
                token=getAccessGenToken(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    OrderRevoke(HttpUrls.orderRevoke);
                    break;
            }
        }
    };


    private void OrderTracking(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
        //  param.put("resToken",token.getUniqueId());
        param.put("voucher",TianHongPayMentUtil.currentOder.getOid());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
            }
            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void OrderRevoke(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("ent_mode","00");
//        param.put("pcode","1008645423131");
        //  param.put("resToken",token.getUniqueId());
        param.put("tr_amt",TianHongPayMentUtil.currentOder.getAmount()+"");
        param.put("mid",TianHongPayMentUtil.currentOder.getMid());
        param.put("voucher",TianHongPayMentUtil.currentOder.getOid());
        param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
            }
            @Override
            public void onError(Object o) {
                Log.i("res err", "" + o.toString());
            }
        });
    }

}
