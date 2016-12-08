package thp.csii.com.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;

public class DetailMyCardActivity extends BaseActivity {
    RelativeLayout img_head;
    TextView tv_status,tv_name,tv_words,tv_date_time,tv_way;
    private final String[] labels = {"待赠送", "赠送成功"};
    List<String> labellist=new ArrayList<String>();
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_card);
        setTitleText(R.string.detail_card);
        setBackView(R.drawable.u194);
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
    }
}
