package thp.csii.com.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.bean.CardBean;
import thp.csii.com.fragment.EffectiveFragment;
import thp.csii.com.fragment.IneffectivFragment;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class MyShoppingCardActivity extends BaseActivity implements View.OnClickListener,EffectiveFragment.getAffection,EffectiveFragment.toGetData,IneffectivFragment.toGetData2{
    private Fragment currentFragment;
    private TextView tv_effect,tv_Ineffct;
    private EffectiveFragment effectiveFragment;
    private IneffectivFragment ineffectivFragment;
    private ViewPager viewPager;
    private FragAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String accJson;
    private LinearLayout ll_back;
    private int index=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetStatusColor();
        setContentView(R.layout.activity_my_shopping_card);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialog(true);
        QryCountDetail(HttpUrls.payFunDetaQry);
    }
    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    showDialog(false);
                    // ecf.RefreshData();
                    if (null==ecf||null==inf){

                        initViewPage();
                    }
                    else {
                        ecf.RefreshData(accJson);
                        inf.RefreshData(accJson);
                    }
                    viewPager.setCurrentItem(index);
                    changeTextColor(index);
                    break;
                case 2:
                    //断网以后授权登录成功
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 404:
                    ToastUtil.shortNToast(context,"重新授权登录失败");
                    break;
            }
        }
    };

    private void QryCountDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                Double balamt;
                Double cardamt = 0.0;
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");

                try {
                    if (null != res) {
                        LogUtil.e(TianHongPayMentUtil.CurrentContext,"res=="+res.toJSONString());
                        if (res.getString("status").equals("0000")) {
                            JSONObject datamap = res.getJSONObject("dataMap");
                            if (null != datamap) {
                                JSONObject rsvc = datamap.getJSONObject("rsvc");
                                JSONObject acclist = rsvc.getJSONObject("accList");
                                // JSONArray account=acclist.getJSONArray("account");
                                accJson=acclist.toJSONString();
                                LogUtil.e(context,"accjson=="+accJson);
                                han.sendEmptyMessage(1);
                            }


                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onError(Object o) {
                showDialog(false);
                han.sendEmptyMessage(1);
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void initViews() {
        setTitleText(R.string.my_sp);
        imageViewBack.setImageResource(R.drawable.u194);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyShoppingCardActivity.this.finish();
            }
        });
        viewPager= (ViewPager) findViewById(R.id.content_layout);
        tv_effect= (TextView) findViewById(R.id.tv_effect);
        tv_Ineffct= (TextView) findViewById(R.id.ineffect);
        tv_Ineffct.setOnClickListener(this);
        tv_effect.setOnClickListener(this);
    }
    EffectiveFragment ecf;
    IneffectivFragment inf;
    List<Fragment> fragments;
    private void initViewPage() {
        Bundle b=new Bundle();
        b.putString("acclist",accJson);
        if (null==fragments){
            fragments = new ArrayList<Fragment>();
        }
        if (ecf==null){
            ecf=EffectiveFragment.newInstance(b);
            fragments.add(ecf);
        }
        if (inf==null){
            inf=IneffectivFragment.newInstance(b);
            fragments.add(inf);
        }
        if (adapter==null){
            adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        }
       if (null==viewPager.getAdapter()){
           viewPager.setAdapter(adapter);
       }else{
           adapter.notifyDataSetChanged();
       }

        viewPager.setOnPageChangeListener(mvl);



    }
    MyVPageChangeListener mvl=new MyVPageChangeListener();

    @Override
    public void Affection(String s) {

    }
    @Override
    public void getDT2() {
        showDialog(true);
        index=1;
        new Thread(thread).start();
    }
    @Override
    public void getDT() {
        showDialog(true);
        index=0;
        new Thread(thread).start();
    }
    Runnable thread = new Runnable() {
        @Override
        public void run() {
            GETLoginToken();

        }
    };
    private static final String STATUS_ERR = "4444";
    private static final String STATUS_OK = "0000";
    private void GETLoginToken() {
        Map<String, Object> ajaxData = new HashMap();
        User user = new User();
        user.setAcno(TianHongPayMentUtil.currentUser.getAcno());
        PainObj painObj = new PainObj(user, null);
        painObj.setUserSign(TianHongPayMentUtil.userSign);
        // LogUtil.e(MainActivity.this,"Accno=="+Accno+"userSign==="+userSign);
        //end
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            //  ToastUtil.shortToast(context, SharePreferencesUtils.getStringValue(context,"Cookie"));
            Log.i("err", "Cookie缓存===" + SharePreferencesUtils.getStringValue(context, "Cookie"));
            Log.i("err", "SESSIONID==" + SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
            ajaxData.put("status", STATUS_OK);
            ajaxData.put("redirectUrl", url);
            han.sendEmptyMessage(2);
        } catch (Exception e) {
            ajaxData.put("status", STATUS_ERR);
            ajaxData.put("errmsg", e.getMessage());
            han.sendEmptyMessage(404);
            System.err.println("授权登录发生错误!" + e.getMessage());
            showDialog(false);
        }
    }




    private class MyVPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int location) {
            changeTextColor(location);
        }

    }
    private void changeTextColor(int location) {
        switch (location) {
            case 0:
                tv_effect.setTextColor(getResources().getColor(R.color.red));
                tv_Ineffct.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                tv_Ineffct.setTextColor(getResources().getColor(R.color.red));
                tv_effect.setTextColor(getResources().getColor(R.color.black));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_effect) {
            viewPager.setCurrentItem(0);
            changeTextColor(0);

        } else if (i == R.id.ineffect) {
            viewPager.setCurrentItem(1);
            changeTextColor(1);

        }

    }
    public class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments ;

        public FragAdapter(FragmentManager fm){
            super(fm);
        }

        public FragAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        /**
         * add the fragment to the special position
         * @param location the position be added to.
         * @param fragment
         */
        public void addFragment(int location,Fragment fragment){
            this.fragments.add(location, fragment);
            this.notifyDataSetChanged();
        }
        /**
         * add the fragment to the default position.the end of the list.
         * @param fragment
         */
        public void addFragment(Fragment fragment){
            this.fragments.add(fragment);
            this.notifyDataSetChanged();
        }
    }
}
