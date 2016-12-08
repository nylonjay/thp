package thp.csii.com.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

/*

 */
public class ForgetPayCodeActivity extends BaseActivity implements View.OnClickListener {
    Button btn_next_step, btn_sms;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //rivate GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pay_code);
        setTitleText(R.string.inputcode);
        initViews();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
     //   client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initViews() {
        btn_next_step = (Button) findViewById(R.id.btn_next_step);
        btn_next_step.setOnClickListener(this);
        btn_sms = (Button) findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_next_step) {
            startActivity(new Intent(ForgetPayCodeActivity.this, ModifyPayCodeActivity.class));

        } else if (i == R.id.btn_sms) {
            GetSMSConfirm();

        }

    }

    private void GetSMSConfirm() {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();


        String url =  Constant.SERVERHOST + Constant.AppName + HttpUrls.getSmsToken;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "text/xml,application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getStringValue(context,"Cookie"));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res=json.getJSONObject("res");
                if (res.get("status").equals("0000")) {
                    //JSONObject res = json.getJSONObject("res");
                    JSONObject datamap=res.getJSONObject("dataMap");
                    String rc=datamap.getString("rc");
                    String rc_detail=datamap.getString("rc_detail");
                    Log.i("res rc",""+rc+"++++说明:"+rc_detail);

                }
            }

            @Override
            public void onError(Object o) {
            Log.i("res err",""+ o.toString());
            }


        });
    }

}
