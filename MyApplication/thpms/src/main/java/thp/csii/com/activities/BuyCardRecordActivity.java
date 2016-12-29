package thp.csii.com.activities;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseTokenActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.bean.CardRecord;
import thp.csii.com.bean.TradeDetail;
import thp.csii.com.fragment.EffectiveFragment;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class BuyCardRecordActivity extends BaseTokenActivity implements PullToRefreshBase.OnRefreshListener{
    //String[] cards=new String[]{"25","50","50"};
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Token token;
    private PullToRefreshListView mWrapView;
    private BuyCardRecordAdaper mAdapter;
    private int CurrentPage=1;
    private List<CardRecord> crs=new ArrayList<CardRecord>();
    private TextView tv_empty_info;
    private int pageCount;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_record);
        setTitleText(R.id.buy_card_record);
        setBackView(R.drawable.u194);
        initviews();
        //  new Thread(sendable).start();
        hand.sendEmptyMessage(5);


    }

    private void initviews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyCardRecordActivity.this.finish();
            }
        });
        mWrapView= (PullToRefreshListView) findViewById(R.id.mListView);
        mWrapView.setPullRefreshEnabled(true);
        mWrapView.setAutoPullUp(true);
        View mErrorView         = findViewById(R.id.common_list_error);
        View mEmptyView         = findViewById(R.id.common_list_empty);
        tv_empty_info= (TextView) mEmptyView.findViewById(R.id.tv_empty_info);
        mWrapView.setErrorListener(R.id.reload_bu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mWrapView.setErrorView(mErrorView);
        mWrapView.setEmptyView(mEmptyView);
        //mWrapView.setErrorListener(R.id.reload_bu, this);
        mListView = mWrapView.getRefreshableView();
        mListView.setDividerHeight(16);
        mListView.setClipToPadding(false);
        mListView.setPadding(0,15,0,0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in=new Intent(BuyCardRecordActivity.this,OrderDetailActivity.class);
                    in.putExtra("voucher",crs.get((int)id).getVoucher());
                    startActivity(in);
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent in=new Intent(getActivity(),DetailMyCardActivity.class);
//                in.putExtra("cb",cardBeens.get(position));
//                startActivity(new Intent(getActivity(),DetailMyCardActivity.class));
//            }
//        });
        mAdapter=new BuyCardRecordAdaper();
//        mListView.setAdapter(mAdapter);
        mWrapView.setOnRefreshListener(BuyCardRecordActivity.this);
        showDialog(true);
        // sendRequest(0);
    }

    private void OrderTracking(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl+"?"+"currentPage="+CurrentPage;
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
                LogUtil.e(BuyCardRecordActivity.this,json.toJSONString());
                JSONObject res=json.getJSONObject("res");
                if (res.getString("status").equals("0000")){
                    JSONObject dataMap=res.getJSONObject("dataMap");
                    String cp=dataMap.getString("currentPage");
                    pageCount=dataMap.getInteger("countPage");
                    //String pagecount=dataMap.getString("countPage")+"";
                    JSONArray cards=dataMap.getJSONArray("vlist");
                    List<CardRecord> crr=JSON.parseArray(cards.toJSONString(),CardRecord.class);
                    if (Integer.parseInt(cp)<=pageCount){
                        mWrapView.onPullUpRefreshComplete();
                        mWrapView.onPullDownRefreshComplete();
                        mWrapView.setNoMoreData(false);
                        mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                        crs.addAll(crr);
                        hand.sendEmptyMessage(400);
                    }else{
                        mWrapView.onPullUpRefreshComplete();
                        mWrapView.onPullDownRefreshComplete();
                        hand.sendEmptyMessage(401);
                        mWrapView.setNoMoreData(true);
                        // mWrapView.setState(PullToRefreshListView.STA);
                        LogUtil.e(context,"已经是最后一页了");
                    }
                }else{
                    ToastUtil.shortNToast(context,res.getString("errmsg"));
                    if ("00036".equals(res.getString("errcode"))){
                        Message msg=new Message();
                        msg.what=404;
                        msg.obj=res.getString("errmsg");
                        hand.sendMessage(msg);
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
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    OrderTracking(HttpUrls.GetBuyCardRecords);
                    break;
                case 400:
                    if (CurrentPage==1){
                        mWrapView.onPullDownRefreshComplete();
                        mWrapView.onPullUpRefreshComplete();
                        if (crs.size()>0){
                            mAdapter=new BuyCardRecordAdaper();
                            mListView.setAdapter(mAdapter);
                            mWrapView.setNoMoreData(false);
                            mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                            if (CurrentPage==pageCount){
                                mWrapView.setNoMoreData(true);
                            }
                        }


                    }else{
                        if (null!=mAdapter){
                            mWrapView.onPullUpRefreshComplete();
                            mWrapView.setNoMoreData(false);
                            mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                    break;
                case 401:
                    if (null!=msg.obj){
                        String info=msg.obj.toString();
                        if (null!=info){
                            tv_empty_info.setText(info);
                        }else{
                            tv_empty_info.setText("暂无交易明细哦~");
                        }
                    }
                    mWrapView.onPullUpRefreshComplete();
                    mWrapView.setNoMoreData(true);
                    if (crs.size()==0){
                        mWrapView.setState(PullToRefreshListView.STATE_EMPTY);
                    }
                    break;
                case 404:
                    mWrapView.setState(PullToRefreshListView.STATE_ERROR);
                    mWrapView.onPullUpRefreshComplete();
                    mWrapView.onPullDownRefreshComplete();
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
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        CurrentPage=1;
        crs.clear();
        hand.sendEmptyMessage(5);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        CurrentPage++;
        hand.sendEmptyMessage(5);

    }

class BuyCardRecordAdaper extends BaseAdapter{

    @Override
    public int getCount() {
        return crs.size();
    }

    @Override
    public Object getItem(int position) {
        return crs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoldler vh=null;
        if (convertView==null){
            vh=new ViewHoldler();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_buycard_record,null);
            vh.time= (TextView) convertView.findViewById(R.id.date_time);
            vh.price= (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(vh);
        }else{
            vh= (ViewHoldler) convertView.getTag();
        }
        vh.time.setText(crs.get(position).getTime());
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
        vh.price.setTypeface(tf);
        vh.price.setText("￥"+crs.get(position).getTr_amt());
        return convertView;
    }
}

class ViewHoldler{
    TextView time,price;
}

}
