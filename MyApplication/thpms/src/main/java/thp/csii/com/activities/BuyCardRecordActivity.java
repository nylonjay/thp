package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;

public class BuyCardRecordActivity extends BaseTokenActivity implements SwipeRefreshLayout.OnRefreshListener{
    String[] cards=new String[]{"25","50","50"};
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_record);
        setTitleText(R.id.buy_card_record);
        initviews();
      //  new Thread(sendable).start();
        hand.sendEmptyMessage(5);


    }

    private void initviews() {

        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        mSwipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mListView= (ListView) findViewById(R.id.mListView);
        mListView.setAdapter(new BuyCardRecordAdaper());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(BuyCardRecordActivity.this,OrderDetailActivity.class));
            }
        });
    }

    private void OrderTracking(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
//        param.put("pin_data","100867878");
//        param.put("sms_code","1008645423131");
      //  param.put("resToken",token.getUniqueId());
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
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    OrderTracking(HttpUrls.orderTracking);
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

    @Override
    public void onRefresh() {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    },5000);
    }

    class BuyCardRecordAdaper extends BaseAdapter{

        @Override
        public int getCount() {
            return cards.length;
        }

        @Override
        public Object getItem(int position) {
            return cards[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_buycard_record,null);
            }

            return convertView;
        }
    }
}
