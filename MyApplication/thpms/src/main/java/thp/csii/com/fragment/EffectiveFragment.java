package thp.csii.com.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.THProgressDialog;
import cn.rainbow.thbase.ui.THToast;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshBase;
import cn.rainbow.thbase.ui.pulltorefresh.PullToRefreshListView;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.activities.DetailMyCardActivity;
import thp.csii.com.activities.MyShoppingCardActivity;
import thp.csii.com.bean.CardBean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EffectiveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EffectiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EffectiveFragment extends Fragment implements PullToRefreshBase.OnRefreshListener,OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String[] vaues=new String[]{"50","111","1123","4334"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    Context mContext;
    private String mParam2;
    private ListView mListView;
    LayoutInflater mLayoutInflater;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    char symbol=165;
    private TextView tv_amount;
    private JSONArray acount;
    private List<CardBean> cbs=new ArrayList<CardBean>();
    private List<CardBean> cardBeens=new ArrayList<CardBean>();
    private PullToRefreshListView mWrapView;
    private THProgressDialog mTHProgressDialog;
    private int index;
    private effeAdapter mAdapter;
    private Button tv_getdata;
    private View RootView;

    public EffectiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EffectiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EffectiveFragment newInstance(Bundle b) {
        EffectiveFragment fragment = new EffectiveFragment();
        Bundle args = b;
        args.putString(ARG_PARAM1, b.getString("acclist"));
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(RootView.getParent() instanceof ViewGroup){
            ((ViewGroup)((ViewGroup) RootView.getParent())).removeView(RootView);
        }
       // RootView.des
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.e(getActivity(),"Onattch");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void RefreshData(String acclist){
        LogUtil.e(TianHongPayMentUtil.CurrentContext,"RefreshData");
           // mParam2 = getArguments().getString(ARG_PARAM2);
            if (null==acclist){
                mWrapView.setState(PullToRefreshListView.STATE_ERROR);
            }else{
                acount= JSON.parseObject(acclist).getJSONArray("account");
                if (null!=JSON.parseArray(acount.toJSONString(),CardBean.class)){
                    cbs=JSON.parseArray(acount.toJSONString(),CardBean.class);
                }
                cardBeens.clear();
               // LogUtil.e(getActivity(),"cbs.length=="+cbs.size());
                for (CardBean cb:cbs){
                    if (!cb.getBalAmt().equals("0.00")&&!cb.getAccno().equals(TianHongPayMentUtil.currentUser.getAcno())){
                        cardBeens.add(cb);
                    }
                }
//                for (int i=0;i<3;i++){
//                    cardBeens.addAll(cardBeens);
//                }
                mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                mWrapView.setNoMoreData(true);
                //开始加载列表
                if (cardBeens.size()>0){
                    mListView.setAdapter(mAdapter);
                }else {
                    mWrapView.setState(PullToRefreshListView.STATE_EMPTY);
                }
                //  }
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutInflater=inflater;
        LogUtil.e(TianHongPayMentUtil.CurrentContext,"oncreateview");
        mContext=getActivity();
        if (null==RootView) {
            RootView = inflater.inflate(R.layout.fragment_effective, container, false);
        }
            initViews(RootView);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
            if (null==mParam1){
                mWrapView.setState(PullToRefreshListView.STATE_ERROR);
            }else{
                acount= JSON.parseObject(mParam1).getJSONArray("account");
                cbs=JSON.parseArray(acount.toJSONString(),CardBean.class);
                cardBeens.clear();
                LogUtil.e(getActivity(),"cbs.length=="+cbs.size());
                for (CardBean cb:cbs){
                    if (!cb.getBalAmt().equals("0.00")&&!cb.getAccno().equals(TianHongPayMentUtil.currentUser.getAcno())){
                        cardBeens.add(cb);
                    }
                }
                mWrapView.setState(PullToRefreshListView.STATE_HAS_DATA);
                mWrapView.setNoMoreData(true);
                //开始加载列表
                if (cardBeens.size()>0){
                    mListView.setAdapter(mAdapter);
                }else {
                    mWrapView.setState(PullToRefreshListView.STATE_EMPTY);
                }
                //  }
            }
        }


        return RootView;
    }



    private void initViews(View v) {
        mWrapView= (PullToRefreshListView) v.findViewById(R.id.effectiveList);
        mWrapView.setPullRefreshEnabled(true);
        mWrapView.setAutoPullUp(true);
        View mErrorView         = v.findViewById(R.id.common_list_error);
        View mEmptyView         = v.findViewById(R.id.common_list_empty);
        mWrapView.setErrorListener(R.id.reload_bu, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MyShoppingCardActivity){
                    ((toGetData)getActivity()).getDT();
                }
            }
        });
        mWrapView.setErrorView(mErrorView);
        mWrapView.setEmptyView(mEmptyView);
        //mWrapView.setErrorListener(R.id.reload_bu, this);
        mListView = mWrapView.getRefreshableView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in=new Intent(getActivity(),DetailMyCardActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("cb",cardBeens.get((int) id));
                in.putExtras(b);
                startActivity(in);
            }
        });
        mAdapter=new effeAdapter();
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public interface toGetData{
        void getDT();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
//        index = 0;
//        cardBeens.clear();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        sendRequest(index);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {

    }

    @Override
    public void onClick(View v) {

    }

    public interface getAffection{
        public void Affection(String s);
    }


    private class effeAdapter extends BaseAdapter{

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
            if (convertView==null){
                vh=new ViewHolder();
                convertView=mLayoutInflater.inflate(R.layout.item_effect_layout,null);
                vh.tv= (TextView) convertView.findViewById(R.id.tv_amount);
                vh.tv_ye= (TextView) convertView.findViewById(R.id.tv_ye);
                vh.tv_ye.setTextColor(Color.parseColor("#323232"));
                convertView.setTag(vh);
            }else{
                vh= (ViewHolder) convertView.getTag();
            }
            Typeface tf=Typeface.createFromAsset(getActivity().getAssets(),"fonts/FZXH1JW.TTF");
            vh.tv.setTypeface(tf);
            vh.tv.setText(String.valueOf(symbol)+cardBeens.get(position).getBalAmt());
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv,tv_ye;
    }

}
