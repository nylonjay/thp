package thp.csii.com.activities;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
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
import cn.rainbow.thbase.ui.THProgressDialog;
import cn.rainbow.thbase.ui.THToast;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.bean.CardBean;
import thp.csii.com.bean.TradeDetail;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class TradeDetailActivity extends BaseActivity implements PullToRefreshListView.OnRefreshListener,View.OnClickListener{
    ListView mListView;
    private List<TradeDetail> tds=new ArrayList<TradeDetail>();
    char symbol=165;
    private  PullToRefreshListView mWrapView;
    private int index=1;
    private TradeAdapter mAdapter;
    private THProgressDialog mTHProgressDialog;
    private LinearLayout ll_back;
    private TextView tv_empty_info;
    protected Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_trade_detail);
        setTitleText(R.string.trade_detail);
        getData();
        initViews();
        tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
    }

    private void getData() {
        showDialog(true);
        QryDetail(HttpUrls.QryTradeDetail);
    }

    private void initViews() {
        imageViewBack.setImageResource(R.drawable.u194);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeDetailActivity.this.finish();
            }
        });
        mWrapView= (PullToRefreshListView) findViewById(R.id.list_trade);
        mWrapView.setPullRefreshEnabled(true);
        mWrapView.setPullLoadEnabled(true);
        mWrapView.setAutoPullUp(true);
        View mErrorView         = findViewById(R.id.common_list_error);
        View mEmptyView         = findViewById(R.id.common_list_empty);
        tv_empty_info= (TextView) mEmptyView.findViewById(R.id.tv_empty_info);
        mWrapView.setErrorView(mErrorView);
        mWrapView.setEmptyView(mEmptyView);
        mWrapView.setErrorListener(R.id.reload_bu,this);
        mListView = mWrapView.getRefreshableView();
        mListView.setDividerHeight(16);

        mWrapView.setOnRefreshListener(this);
        mWrapView.setErrorListener(R.id.reload_bu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //  mListView.setAdapter(new TradeAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TradeDetail td=tds.get(position);
                Intent in=new Intent(TradeDetailActivity.this,TradePaticularsActivity.class);
                in.putExtra("td",td);
                startActivity(in);

            }
        });
    }
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 400:
                    if (index==1){
                        mWrapView.onPullDownRefreshComplete();
                        if (tds.size()>0){
                            mAdapter=new TradeAdapter();
                            mListView.setAdapter(mAdapter);
                            mWrapView.setNoMoreData(false);
                            mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                            if (tds.size()<10){
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
                case 401://请求成功但没有更多数据
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
                    if (tds.size()==0){
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
    List<TradeDetail> mTs=new ArrayList<TradeDetail>();

    private void QryDetail(String mUrl) {
        LogUtil.e(context,"index=="+index);
        mTs.clear();
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        //param.put("currentPage","1");
        String url =Constant.SERVERHOST + Constant.AppName + mUrl+"?"+"currentPage="+index;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        if (null!=TianHongPayMentUtil.CurrentContext){
            headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        }else{
            return;
        }
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");

                try {
                    if (null != res) {
                        if (res.getString("status").equals("0000")) {
                            LogUtil.e(TradeDetailActivity.this,res.toJSONString());
                            JSONArray ts=res.getJSONObject("dataMap").getJSONObject("tradeList").getJSONArray("vprPo");
                            JSONObject tradeList=res.getJSONObject("dataMap").getJSONObject("tradeList");
                            boolean vprPo=tradeList.getBoolean("vprPoSpecified");
                            if (null!=ts){
                                mTs=JSON.parseArray(ts.toJSONString(),TradeDetail.class);
                            }
                            LogUtil.e(TradeDetailActivity.this,"tds.length=="+tds.size());
                            if(vprPo){
                                mWrapView.onPullUpRefreshComplete();
                                mWrapView.onPullDownRefreshComplete();
                                tds.addAll(mTs);
                                mWrapView.setNoMoreData(false);
                                mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);

                                hand.sendEmptyMessage(400);
                                LogUtil.e(context,"获取数据成功:400");
                            }else{
                                mWrapView.onPullUpRefreshComplete();
                                mWrapView.onPullDownRefreshComplete();
                                hand.sendEmptyMessage(401);
                                mWrapView.setNoMoreData(true);
                                // mWrapView.setState(PullToRefreshListView.STA);
                                LogUtil.e(context,"已经是最后一页了");
                            }
                            if(index == 0){
                                showDialog(false);
                            }

                        } else{
                            ToastUtil.shortNToast(context,res.getString("errmsg"));
                            if ("00036".equals(res.getString("errcode"))){
                            Message msg=new Message();
                            msg.what=404;
                            msg.obj=res.getString("errmsg");
                            hand.sendMessage(msg);
                            }
                        }
                    }
                }catch (Exception e){
                    LogUtil.e(context,"exception"+e.toString());
                }finally {LogUtil.e(context,"finnally");
                }

            }

            @Override
            public void onError(Object o) {
                showDialog(false);
                hand.sendEmptyMessage(404);
                dismissDialog();
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }
    protected void showDialog(boolean isShow) {

        if (mTHProgressDialog == null && isShow) {
            mTHProgressDialog = THProgressDialog.createDialog(this);
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


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        LogUtil.e(context,"下拉刷新");
        index=1;
        tds.clear();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        LogUtil.e(context,"上拉加载");
        index++;

        getData();

    }

    class TradeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tds.size();
        }

        @Override
        public Object getItem(int position) {
            return tds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TradeDetail td=tds.get(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.trade_detail_item,null);
                viewHolder.tv_action = (TextView) convertView.findViewById(R.id.action);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.date_time);
                viewHolder.tv_sum = (TextView) convertView.findViewById(R.id.sum);
                viewHolder.tv_sum.setTypeface(tf);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if (td.getTxnId().equals("2")){
                viewHolder.tv_action.setText("消费");
                viewHolder.tv_sum.setTextColor(Color.parseColor("#fe473c"));
                viewHolder.tv_sum.setText("-"+"￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
            }else if (td.getTxnId().equals("3")){
                viewHolder.tv_action.setText("退款");
                viewHolder.tv_sum.setTextColor(Color.parseColor("#63D770"));
                viewHolder.tv_sum.setText("+"+"￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
            }else if (td.getTxnId().equals("4")){
                viewHolder.tv_action.setText("退款");
                viewHolder.tv_sum.setTextColor(Color.parseColor("#63D770"));
                viewHolder.tv_sum.setText("+"+"￥"+String.format("%.2f", Double.valueOf(td.getTxnAmt())));
            }
            viewHolder.tv_date.setText(td.getTxnDate());
            return convertView;
        }
    }
    class ViewHolder{
        TextView tv_action,tv_date,tv_sum;
    }
}
