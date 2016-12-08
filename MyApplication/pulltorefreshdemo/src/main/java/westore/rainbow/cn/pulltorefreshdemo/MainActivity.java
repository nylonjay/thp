package westore.rainbow.cn.pulltorefreshdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPullToRefreshListViewDemo(View v){
        Intent intent = new Intent(this, PullTorefreshListViewDemo.class);
        this.startActivity(intent);

    }

    public void onPullToRefreshRecycleViewDemo(View v){
        Intent intent = new Intent(this, PullTorefreshRecycleViewDemo.class);
        this.startActivity(intent);
    }

    public void onPullToRefreshScrollViewDemo(View v){
        Intent intent = new Intent(this, PullTorefreshScrollViewDemo.class);
        this.startActivity(intent);
    }
}
