package thp.csii.com.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;

public class Cashier_DeskActivity extends BaseActivity {
    ListView mListView;
    Context mContext;
    String[] arr=new String[]{"微信","白条","支付宝"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier__desk);
        initViews();
        setTitleText(R.string.cashier_desk);
    }

    private void initViews() {
        mContext=Cashier_DeskActivity.this;
        mListView= (ListView) findViewById(R.id.mListView);
        mListView.setAdapter(new CashierAdapter());
    }

    class CashierAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return arr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.cashier_item,null);
            }
            TextView title= (TextView) convertView.findViewById(R.id.tv_title);
            title.setText(arr[position]);

            return convertView;
        }
    }

}
