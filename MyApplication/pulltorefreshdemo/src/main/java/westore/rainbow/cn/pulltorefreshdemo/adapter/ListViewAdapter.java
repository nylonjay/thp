package westore.rainbow.cn.pulltorefreshdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.rainbow.thbase.ui.THToast;
import westore.rainbow.cn.pulltorefreshdemo.R;
import westore.rainbow.cn.pulltorefreshdemo.bean.ItemEntity;

/**
 * Created by LinZaixiong on 2016/10/29.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<ItemEntity> mList;
    private Context mContext;

    public ListViewAdapter(Context context, List<ItemEntity> list){

        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mList != null ? mList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.content_item,null);
            holder = new ViewHolder();
            holder.tv_text = (TextView)view.findViewById(R.id.tv_text);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        holder.tv_text.setText("index:" + i);

        return view;
    }

    public class ViewHolder{
        TextView tv_text;

    }
}
