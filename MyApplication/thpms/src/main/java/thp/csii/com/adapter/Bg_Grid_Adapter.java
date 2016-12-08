package thp.csii.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import thp.csii.com.R;

/**
 * Created by Administrator on 2016/9/9.
 */
public class Bg_Grid_Adapter extends BaseAdapter {
    Context mContext;
    String[] arr;


    public Bg_Grid_Adapter(Context mContext, String[] arr) {
        this.mContext = mContext;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return 8;
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_bg_grid,null);
        }

        return convertView;
    }
}
