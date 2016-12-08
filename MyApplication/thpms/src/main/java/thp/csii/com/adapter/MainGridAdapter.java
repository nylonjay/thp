package thp.csii.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thp.csii.com.R;
import thp.csii.com.bean.MainBean;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MainGridAdapter extends BaseAdapter {
    Context context;
    List<MainBean> beeans;

    public MainGridAdapter(Context Context,List<MainBean> beans) {
       context =Context;
        beeans=beans;
    }

    @Override
    public int getCount() {
        return beeans.size();
    }

    @Override
    public Object getItem(int position) {
        return beeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.grid_main_item,null);
        }
        ImageView img= (ImageView) convertView.findViewById(R.id.image_grid);
        switch (position){
            case 0:
               // img.setImageResource(R.drawable.icon_my_card);
                img.setImageResource(R.drawable.icon_my_card);

                break;
            case 1:
                //img.setImageResource(R.drawable.icon_buy_card);
                img.setImageResource(R.drawable.icon_bind_card);
                break;
            case 2:
                img.setImageResource(R.drawable.icon_trade_detail);
                break;
            case 3:
                img.setImageResource(R.drawable.icon_help_center);
                break;
//            case 4:
//                img.setImageResource(R.drawable.icon_buy_card_record);
//                break;
//            case 5:
//                img.setImageResource(R.drawable.icon_help_center);
//                break;
        }
        TextView tv= (TextView) convertView.findViewById(R.id.names);
        tv.setText(beeans.get(position).getName());
        return convertView;
    }

}
