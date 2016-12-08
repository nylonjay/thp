package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.bean.CardBean;
import thp.csii.com.utils.AppUtil;

/**
*ChargeSucceedActivity 充值成功页面
*@author nylon
 * created at 2016/9/6 10:02
*/

public class ChargeSucceedActivity extends BaseActivity implements View.OnClickListener{
    ListView mListView;
    CardBean cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_succeed);
        setTitle(R.string.charge_success);
        initView();
    }

    private void initView() {
        setOkVisibity(true);
        setOkView(R.drawable.u194);
        tvBasetitleOK.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.cardlist);
        CardAdapter cadapter=new CardAdapter();
        AppUtil.setListViewHeightBasedOnChildren(mListView);
//        cb=new CardBean();
//        cb.setBalance(50);
//        cb.setCardNum("31313213213213132");
//        cb.setCode("135135135");
        mListView.setAdapter(cadapter);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_basetitle_ok) {
            Intent in1 = new Intent(ChargeSucceedActivity.this, MyShoppingCardActivity.class);
            startActivity(in1);

        }
    }


    class CardAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return cb;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_charge_succed,null);
            }

            return convertView;
        }
    }

}
