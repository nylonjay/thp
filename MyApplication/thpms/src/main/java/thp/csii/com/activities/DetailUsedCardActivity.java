package thp.csii.com.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.bean.CardBean;

public class DetailUsedCardActivity extends BaseActivity {
    private LinearLayout ll_back;
    private ImageView img_head;
    private TextView tv_bind_date,tv_yx_date,tv_amount,tv_accno;
    CardBean cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_used_card);
        cb= (CardBean) getIntent().getSerializableExtra("cb");
        setTitleText(R.string.detail_card);
        setBackView(R.drawable.u194);
        initViews();
    }

    private void initViews() {
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailUsedCardActivity.this.finish();
            }
        });
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
        tv_amount.setTextColor(Color.parseColor("#999999"));
        tv_amount.setTypeface(tf);
        tv_amount.setText(cb.getBalAmt());
        tv_bind_date= (TextView) findViewById(R.id.tv_bind_date);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date d=sdf.parse(cb.getBindDate());
            SimpleDateFormat sd1=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String s1=sd1.format(d);
            tv_bind_date.setText(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // tv_bind_date.setText(cb.getBindDate());
        tv_yx_date= (TextView) findViewById(R.id.tv_yx_date);
        img_head= (ImageView) findViewById(R.id.img_head);
        Bitmap bm =((BitmapDrawable) img_head.getDrawable()).getBitmap();
        Bitmap gbm=grey(bm);
        img_head.setImageBitmap(gbm);

        tv_accno= (TextView) findViewById(R.id.tv_accno);
        String a=cb.getAccno();
        if (a.length()==16){
            String a1=a.substring(0,4);
            String a2=a.substring(4,8);
            String a3=a.substring(8,12);
            String a4=a.substring(12,15);
            String a5=a.substring(15,a.length());
            LogUtil.e(DetailUsedCardActivity.this,"accno=="+a);
            tv_accno.setText(a1+" "+a2+" "+a3+" "+a4+""+a5);
        }else if (a.length()==19){
            String a1=a.substring(0,4);
            String a2=a.substring(4,8);
            String a3=a.substring(8,12);
            String a4=a.substring(12,16);
            String a5=a.substring(16,18);
            String a6=a.substring(18,a.length());
            LogUtil.e(DetailUsedCardActivity.this,"accno=="+a);
            tv_accno.setText(a1+" "+a2+" "+a3+" "+a4+" "+a5+" "+a6);
        }
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
