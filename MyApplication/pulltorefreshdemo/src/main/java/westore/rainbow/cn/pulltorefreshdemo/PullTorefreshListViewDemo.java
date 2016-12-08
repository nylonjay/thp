package westore.rainbow.cn.pulltorefreshdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import cn.rainbow.thbase.ui.THToast;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import westore.rainbow.cn.pulltorefreshdemo.adapter.ListViewAdapter;
import westore.rainbow.cn.pulltorefreshdemo.bean.ItemEntity;
import westore.rainbow.cn.pulltorefreshdemo.utils.DataSource;
import cn.rainbow.thbase.ui.THProgressDialog;

/**
 * Created by LinZaixiong on 2016/10/29.
 */

public class PullTorefreshListViewDemo extends Activity implements PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {

    private PullToRefreshListView mWrapView;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private List<ItemEntity> mList = new ArrayList<ItemEntity>();
    private DataSource source = new DataSource();
    private int index = 0;
    private THProgressDialog mTHProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_listview);
        mWrapView = (PullToRefreshListView)findViewById(R.id.common_list_wrap);
        mWrapView.setPullRefreshEnabled(true);
        mWrapView.setPullLoadEnabled(true);
        mWrapView.setAutoPullUp(true);
        View mErrorView         = findViewById(R.id.common_list_error);
        View mEmptyView         = findViewById(R.id.common_list_empty);
        mWrapView.setErrorView(mErrorView);
        mWrapView.setEmptyView(mEmptyView);
        mWrapView.setErrorListener(R.id.reload_bu, this);
        mListView = mWrapView.getRefreshableView();
        mAdapter = new ListViewAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mWrapView.setOnRefreshListener(this);
        showDialog(true);
        sendRequest(0);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        index = 0;
        mList.clear();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        sendRequest(index);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        index++;
        sendRequest(index);
    }

    private void sendRequest(int index){

        List<ItemEntity> list = source.getDataList(index);

        mWrapView.onPullUpRefreshComplete();
        mWrapView.onPullDownRefreshComplete();

//        mWrapView.setPullRefreshEnabled(true);
//        mWrapView.setPullLoadEnabled(true);

        if(list != null && list.size() > 0){
            mWrapView.setNoMoreData(false);
            mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
            mList.addAll(list);
        }
        else{
            mWrapView.setNoMoreData(true);
            THToast.showRightPromat(this, "已经是最后一页了");
        }

        if(index == 0){
            showDialog(false);
        }


        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
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
    public void onClick(View view) {
        if(view.getId() == R.id.reload_bu){
            index = 0;
            mList.clear();
            sendRequest(0);
        }
    }
}
