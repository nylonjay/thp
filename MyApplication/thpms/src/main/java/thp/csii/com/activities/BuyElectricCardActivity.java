package thp.csii.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import thp.csii.com.BaseActivity;
import thp.csii.com.MyApp;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

/**
 *BuyElectricCardActivity  购买电子卡
 *@author nylon
 * created at 2016/9/6 17:04
 */

public class BuyElectricCardActivity extends BaseActivity implements View.OnClickListener{
    ListView mListView;
    Integer[] values=new Integer[]{100,50,150,50};
    Button btn_clearing;
    RelativeLayout re_choose_bg,re_invoice;
    int SUM=0;
    private TextView tv_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_electric_card);
        setTitleText(R.string.buy_card);
        setBackView(R.drawable.u194);
        initViews();
        PayTokenRequest(HttpUrls.payTokenRequest);
    }

    private void PayTokenRequest(String mUrl) {
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

    private void initViews() {
        tv_sum= (TextView) findViewById(R.id.tv_sum);
        re_invoice= (RelativeLayout) findViewById(R.id.re_invoice);
        re_invoice.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.cardlist);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  startActivity(new Intent(BuyElectricCardActivity.this,ChooseBgActivity.class));
            }
        });
        CardItemAdapter adapter=new CardItemAdapter();
        mListView.setAdapter(adapter);
        btn_clearing= (Button) findViewById(R.id.btn_clearing);
        btn_clearing.setOnClickListener(this);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyElectricCardActivity.this.finish();
            }
        });
        re_choose_bg= (RelativeLayout) findViewById(R.id.re_choos_bg);
        re_choose_bg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_clearing) {
            initDialog(getResources().getString(R.string.single_card));

        } else if (i == R.id.re_choos_bg) {
            startActivity(new Intent(BuyElectricCardActivity.this, ChooseBgActivity.class));

        } else if (i == R.id.re_invoice) {
            startActivity(new Intent(BuyElectricCardActivity.this, InvoiceInfoActivity.class));

        }
    }



    class CardItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return values.length;
        }

        @Override
        public Object getItem(int position) {
            return values[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_buy_card,null);
            }
            final TextView num= (TextView) convertView.findViewById(R.id.card_num);
            TextView tv= (TextView) convertView.findViewById(R.id.par_value);
            LinearLayout reduce= (LinearLayout) convertView.findViewById(R.id.img_reduce);
            LinearLayout add= (LinearLayout) convertView.findViewById(R.id.img_add);
            tv.setText(values[position]+"");
            reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currnum=Integer.parseInt(num.getText().toString());
                    int a=modify("reduce",currnum);
                    ToastUtil.shortToast(context,"当前的数量为:"+a);
                    num.setText(a+"");
                    // notifyDataSetChanged();
                    if (currnum>=1){
                        SUM-=values[position];
                        tv_sum.setText("￥"+SUM+"");
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currnum=Integer.parseInt(num.getText().toString());
                    int a=modify("add",currnum);
                    num.setText(a+"");
                    //notifyDataSetChanged();
                    SUM+=values[position];
                    tv_sum.setText("￥"+SUM+"");
                }
            });

            return convertView;
        }
    }

    public int modify(String action,int num){
        if (action.equals("reduce")){
            if (num>0)
                return --num;
            else
                return 0;
        }
        else
            return ++num;
    }

}
