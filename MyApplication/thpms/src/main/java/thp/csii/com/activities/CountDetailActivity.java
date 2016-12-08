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

public class CountDetailActivity extends BaseTokenActivity {
    private Token token;
    private Button mBtnQryDetail,mBtnPreCharge,mBtnActivate,mBtnChargeRevoke;
    private String action="detail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_detail);
        initViews();
    }

    private void initViews() {
        mBtnChargeRevoke= (Button) findViewById(R.id.chargerevoke);
        mBtnChargeRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="revoke";
                new Thread(sendable).start();
            }
        });
        mBtnActivate= (Button) findViewById(R.id.activate);
        mBtnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="activate";
                new Thread(sendable).start();
            }
        });
        mBtnPreCharge= (Button) findViewById(R.id.precharge);
        mBtnPreCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action="precharge";
                new Thread(sendable).start();
            }
        });
        mBtnQryDetail= (Button) findViewById(R.id.qryDetails);
        mBtnQryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                action="detail";
//                new Thread(sendable).start();

            }
        });
    }

    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    if (action.equals("precharge")){
                        PreCharge(HttpUrls.rechargeCallBack4AlipayServerConfirm);
                    }else if (action.equals("activate")){
                        OrderActivate(HttpUrls.rechargeOrderActivate);
                    }else if (action.equals("revoke")){
                        OrderRevoke(HttpUrls.rechargeOrderRevoke);
                    }else if (action.equals("detail")){

                    }
                    break;
            }
        }
    };
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                token=null;
                token=getAccessGenToken(hand);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };



    private void OrderActivate(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
        param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
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
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
        param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie",SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
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


    private void PreCharge(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("ent_mode","00");
        param.put("tr_amt","100");
        param.put("pcode","1008645423131");
        param.put("resToken",token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
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
