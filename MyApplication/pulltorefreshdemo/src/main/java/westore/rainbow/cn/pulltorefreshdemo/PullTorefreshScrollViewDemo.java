package westore.rainbow.cn.pulltorefreshdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.rainbow.thbase.ui.pulltorefresh.PullScrollView;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshScrollView;

/**
 * Created by LinZaixiong on 2016/10/29.
 */

public class PullTorefreshScrollViewDemo extends Activity implements PullToRefreshBase.OnRefreshListener<PullScrollView>, View.OnClickListener {

    private PullToRefreshScrollView mScrollView;
    private TextView tv_text;
    private int mCount = 0;
    private int showErrorWhenRefresh = 0; //
    private final int ERROR_REFRESH_TIME = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scrollview);

        mScrollView             = (PullToRefreshScrollView)this.findViewById(R.id.sv_list);

        mScrollView.setPullLoadEnabled(false);
        mScrollView.setOnRefreshListener(this);
        View mErrorView         = findViewById(R.id.common_list_error);

        // important
        View content            = LayoutInflater.from(this).inflate(R.layout.content_main, null, false);

        mScrollView.getRefreshableView().addView(content);
        tv_text                 = (TextView)content.findViewById(R.id.tv_text) ;
        mScrollView.setErrorView(mErrorView);
        mScrollView.setErrorListener(R.id.reload_bu, this);

        tv_text.setText("onPullDownToRefresh#" + mCount);
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<PullScrollView> refreshView) {
        mScrollView.onPullDownRefreshComplete();
        mScrollView.onPullUpRefreshComplete();
        mCount++;
        showErrorWhenRefresh++;
        if(showErrorWhenRefresh == ERROR_REFRESH_TIME){
            mCount = 0;
            mScrollView.setState(PullToRefreshScrollView.STATE_ERROR);
        }
        else{
            tv_text.setText("onPullDownToRefresh#" + mCount);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<PullScrollView> refreshView) {
        // no need
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.reload_bu){
            sendRequest();
        }
    }

    private void sendRequest(){
        mCount = 0;
        tv_text.setText("onPullDownToRefresh#" + mCount);

        // callback function must reset listview state
        callBack();
    }

    private void callBack(){
        mScrollView.setState(PullToRefreshScrollView.STATE_HAS_DATA);
//        mScrollView.setState(PullToRefreshScrollView.STATE_ERROR);
//        mScrollView.setState(PullToRefreshScrollView.STATE_EMPTY);
    }
}
