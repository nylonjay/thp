package thp.csii.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;

public class SelectHwmActivity extends BaseActivity {
    private ListView hwmList;
    private String[] hwm1=new String[]{"200","300","500","800","1000","2000"};
    private String[] hwm2=new String[]{"500","1000","2000","3000","4000","5000"};
    private String[] hwm3=new String[]{"1000","5000","8000","10000","15000","20000"};
    List<String[]> slist=new ArrayList<>();
    private String[] cuarr=new String[]{};
    private HwmAdaper mAdapter;
    private int selection=0;
    private LinearLayout ll_back;
    private int  mpostion;
    private String hwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hwm);
        setTitleText(R.string.select_hwm);
        setBackView(R.drawable.u194);
        slist.add(hwm1);slist.add(hwm2);slist.add(hwm3);
        mpostion=getIntent().getIntExtra("postion",0);
        hwm=getIntent().getStringExtra("hwm");
        LogUtil.e(SelectHwmActivity.this,"hwm=="+hwm);
        cuarr=slist.get(mpostion);
        initViews();
    }

    private void initViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectHwmActivity.this.finish();
            }
        });
        hwmList= (ListView) findViewById(R.id.hwm_list);
        hwmList.setDivider(null);
        mAdapter=new HwmAdaper();
        hwmList.setAdapter(mAdapter);
        hwmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.selectHwm((int) id);
                Intent in=new Intent();
                in.putExtra("hwm",cuarr[position]);
                SelectHwmActivity.this.setResult(RESULT_OK,in);
                SelectHwmActivity.this.finish();
            }
        });
        for (int i=0;i<cuarr.length;i++){
            if ((cuarr[i]+".00").equals(hwm)){
                LogUtil.e(SelectHwmActivity.this,"选中的是第"+i+"个");
                mAdapter.selectHwm(i);
                break;
            }
        }
    }

    class HwmAdaper extends BaseAdapter{

        @Override
        public int getCount() {
            return cuarr.length;
        }

        @Override
        public Object getItem(int position) {
            return cuarr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public void selectHwm(int postion){
            selection=postion;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView==null){
                convertView= LayoutInflater.from(SelectHwmActivity.this).inflate(R.layout.hwm_item,null);
                vh=new ViewHolder();
                vh.tv_hwm= (TextView) convertView.findViewById(R.id.tv_hwm);
                vh.img_selected= (ImageView) convertView.findViewById(R.id.img_selected);
                convertView.setTag(vh);
            }else{
                vh= (ViewHolder) convertView.getTag();
            }
            if (position==selection){
                vh.img_selected.setVisibility(View.VISIBLE);
            }else{
                vh.img_selected.setVisibility(View.INVISIBLE);
            }
            vh.tv_hwm.setText(cuarr[position]+"/笔");

            return convertView;
        }
    }
    class ViewHolder{
        TextView tv_hwm;
        ImageView img_selected;
    }
}
