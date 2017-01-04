package thp.csii.com.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.THProgressDialog;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.activities.DetailMyCardActivity;
import thp.csii.com.activities.DetailUsedCardActivity;
import thp.csii.com.activities.MyShoppingCardActivity;
import thp.csii.com.bean.CardBean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IneffectivFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IneffectivFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IneffectivFragment extends Fragment implements PullToRefreshListView.OnRefreshListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    LayoutInflater mLayoutInflater;

    private Context mContext;
    private String[] vaues=new String[]{"50","111","1123","4334"};
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JSONArray acount;
    private List<CardBean> cbs=new ArrayList<CardBean>();
    private List<CardBean> cardBeens=new ArrayList<CardBean>();
    private PullToRefreshListView mWrapView;
    private effeAdapter mAdapter;
    private THProgressDialog mTHProgressDialog;
    char symbol=165;
    private View RootView;
    Typeface tf;
    public IneffectivFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void RefreshData(String acclist){
            if (null==acclist){
                mWrapView.setState(PullToRefreshListView.STATE_ERROR);
            }else{
                acount = JSON.parseObject(acclist).getJSONArray("account");
                if (null!=JSON.parseArray(acount.toJSONString(), CardBean.class)){
                    cbs = JSON.parseArray(acount.toJSONString(), CardBean.class);
                }
                cardBeens.clear();
                LogUtil.e(getActivity(), "cbs.length==" + cbs.size());
                for (CardBean cb : cbs) {
                    if (cb.getBalAmt().equals("0.00") && !cb.getAccno().equals(TianHongPayMentUtil.currentUser.getAcno())) {
                        cardBeens.add(cb);
                    }
                }
                if (cardBeens.size() > 0) {

                    mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                    mWrapView.setNoMoreData(true);
                    mListView.setAdapter(mAdapter);
                } else {

                    mWrapView.setState(PullToRefreshListView.STATE_EMPTY);
                    //   mWrapView.setNoMoreData(true);
                }

                //开始加载列表

                //
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tf=Typeface.createFromAsset(getActivity().getAssets(),"fonts/FZXH1JW.TTF");
        mLayoutInflater=inflater;
        mContext=getActivity();
        RootView=inflater.inflate(R.layout.fragment_effective, container, false);
        initViews(RootView);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            if (null==mParam1){
                mWrapView.setState(PullToRefreshListView.STATE_ERROR);
            }else{
                acount = JSON.parseObject(mParam1).getJSONArray("account");
                cbs = JSON.parseArray(acount.toJSONString(), CardBean.class);
                cardBeens.clear();
                LogUtil.e(getActivity(), "cbs.length==" + cbs.size());
                for (CardBean cb : cbs) {
                    if (cb.getBalAmt().equals("0.00") && !cb.getAccno().equals(TianHongPayMentUtil.currentUser.getAcno())) {
                        cardBeens.add(cb);
                    }
                }
                if (cardBeens.size() > 0) {

                    mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                    mWrapView.setNoMoreData(true);
                    mListView.setAdapter(mAdapter);
                } else {

                    mWrapView.setState(PullToRefreshListView.STATE_EMPTY);
                    //   mWrapView.setNoMoreData(true);
                }

                //开始加载列表

                //
            }
        }
        return RootView;
    }
    public static IneffectivFragment newInstance(Bundle b) {
        IneffectivFragment fragment = new IneffectivFragment();
        Bundle args =b;
        args.putString(ARG_PARAM1, b.getString("acclist"));
      //  args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(View v) {
        mWrapView= (PullToRefreshListView) v.findViewById(R.id.effectiveList);
        mWrapView.setPullRefreshEnabled(true);
        mWrapView.setPullLoadEnabled(false);
        mWrapView.setAutoPullUp(true);
        View mErrorView         = v.findViewById(R.id.common_list_error);
        View mEmptyView         = v.findViewById(R.id.common_list_empty);
        mWrapView.setErrorView(mErrorView);
        mWrapView.setEmptyView(mEmptyView);
        mWrapView.setErrorListener(R.id.reload_bu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()instanceof MyShoppingCardActivity){
                    ((MyShoppingCardActivity)getActivity()).getDT2();
                }
            }
        });
        mListView = mWrapView.getRefreshableView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardBean cb=cardBeens.get(position);
                Intent in=new Intent(getActivity(),DetailUsedCardActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("cb",cb);
                in.putExtras(b);
                startActivity(in);
            }
        });
        mListView.setDividerHeight(16);
        mAdapter=new effeAdapter();
//        mListView.setAdapter(mAdapter);
        mWrapView.setOnRefreshListener(this);
        showDialog(true);
        sendRequest(0);
    }

    protected void showDialog(boolean isShow) {

        if (mTHProgressDialog == null && isShow) {
            mTHProgressDialog = THProgressDialog.createDialog(getActivity());
            mTHProgressDialog.setMessage(R.string.loading);
        }

        if (mTHProgressDialog != null) {

            if (isShow) {
                mTHProgressDialog.show();
            } else {
                mTHProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
    int index=0;
    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        sendRequest(index);
    }
    private void sendRequest(int index){

        //List<ca> list = source.getDataList(index);
        //  List<CardBean> mlist=

        mWrapView.onPullUpRefreshComplete();
        mWrapView.onPullDownRefreshComplete();

//        mWrapView.setPullRefreshEnabled(true);
//        mWrapView.setPullLoadEnabled(true);

//        if(list != null && list.size() > 0){
//            mWrapView.setNoMoreData(false);
//            mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
//            cardBeens.addAll(list);
//        }
//        else{
//            mWrapView.setNoMoreData(true);
//            THToast.showRightPromat(getActivity(), "已经是最后一页了");
//        }

        if(index == 0){
            showDialog(false);
        }


        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface toGetData2{
        void getDT2();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {

    }

    private class effeAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return cardBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return cardBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (null==convertView){
                vh=new ViewHolder();
                convertView=mLayoutInflater.inflate(R.layout.item_effect_layout,null);
                vh.tv_amount= (TextView) convertView.findViewById(R.id.tv_amount);
                vh.tv_explain= (TextView) convertView.findViewById(R.id.tv_explain);
                vh.tv_ye= (TextView) convertView.findViewById(R.id.tv_ye);
                ImageView img= (ImageView) convertView.findViewById(R.id.sp_bg);
                Bitmap bm =((BitmapDrawable) img.getDrawable()).getBitmap();
                Bitmap gbm=grey(bm);
                img.setImageBitmap(gbm);
                convertView.setTag(vh);
            }else{
                vh= (ViewHolder) convertView.getTag();
            }
            vh.tv_ye.setTextColor(Color.parseColor("#dadada"));
            vh.tv_explain.setVisibility(View.GONE);
            vh.tv_amount.setTextColor(Color.parseColor("#dadada"));
            vh.tv_amount.setTypeface(tf);
            vh.tv_amount.setText(String.valueOf(symbol)+cardBeens.get(position).getBalAmt());
            return convertView;
        }
    }
    class ViewHolder{
        TextView tv_amount;
        TextView tv_explain;
        TextView tv_ye;
    }


    public static final Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

}
