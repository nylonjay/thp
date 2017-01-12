package thp.csii.com.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.bean.CardBean;

public class DetailMyCardActivity extends BaseActivity {
    RelativeLayout img_head;
    TextView tv_status,tv_name,tv_words,tv_date_time,tv_way,tv_amount,tv_accno;
    private final String[] labels = {"待赠送", "赠送成功"};
    List<String> labellist=new ArrayList<String>();
    private LinearLayout ll_back;
    private CardBean cb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_card);
        setTitleText(R.string.detail_card);
        setBackView(R.drawable.u194);
        cb= (CardBean) getIntent().getSerializableExtra("cb");
        LogUtil.e("EFFE",cb.getAccno()+"/"+cb.getBalAmt()+"/"+cb.getBindDate());
        initViews();
        drawStepView();
    }

    private void drawStepView() {
        for (String s:labels){
            labellist.add(s);
        }
//        mStepView.setStepsViewIndicatorComplectingPosition(1)
//                .setStepViewTexts(labellist)
//                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), android.R.color.holo_green_light))//设置StepsViewIndicator完成线的颜色
//                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
//                .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), android.R.color.holo_green_light))//设置StepsView text完成线的颜色
//                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.text_dark_gray))//设置StepsView text未完成线的颜色
//                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.step_circle))//设置StepsViewIndicator CompleteIcon
//                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.step_circle_default))//设置StepsViewIndicator DefaultIcon
//                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.step_circle_default))
//                ;

    }

    private void initViews() {
        //   mStepView= (StepView) findViewById(R.id.my_stepviews);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailMyCardActivity.this.finish();
            }
        });
        img_head= (RelativeLayout) findViewById(R.id.img_head);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_status= (TextView) findViewById(R.id.tv_status);
        tv_date_time= (TextView) findViewById(R.id.tv_date_time);
        tv_words= (TextView) findViewById(R.id.tv_words);
        tv_way= (TextView) findViewById(R.id.tv_way);
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
        tv_amount.setTypeface(tf);
        tv_amount.setText(cb.getBalAmt());
        tv_date_time.setText(cb.getBindDate());
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//        try {
//            Date d=sdf.parse(cb.getBindDate());
//            SimpleDateFormat sd1=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//            String s1=sd1.format(d);
//            tv_date_time.setText(s1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        tv_accno= (TextView) findViewById(R.id.tv_accno);

        String a=cb.getAccno();
        if (a.length()==16){
            String a1=a.substring(0,4);
            String a2=a.substring(4,8);
            String a3=a.substring(8,12);
            String a4=a.substring(12,15);
            String a5=a.substring(15,a.length());
            LogUtil.e(DetailMyCardActivity.this,"accno=="+a);
            tv_accno.setText(a1+" "+a2+" "+a3+" "+a4+""+a5);
        }else if (a.length()==19){
            String a1=a.substring(0,4);
            String a2=a.substring(4,8);
            String a3=a.substring(8,12);
            String a4=a.substring(12,16);
            String a5=a.substring(16,18);
            String a6=a.substring(18,a.length());
            LogUtil.e(DetailMyCardActivity.this,"accno=="+a);
            tv_accno.setText(a1+" "+a2+" "+a3+" "+a4+" "+a5+" "+a6);
        }
    }
}
