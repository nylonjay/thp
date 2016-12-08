package westore.rainbow.cn.pulltorefreshdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewStub;

import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshRecycleView;

/**
 * Created by LinZaixiong on 2016/10/29.
 */

public class PullTorefreshRecycleViewDemo extends Activity{
    private PullToRefreshRecycleView ptr_Rcv;
    private ViewStub vs;
    private RecyclerView recycler_view;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_recycleview);
    }
}
