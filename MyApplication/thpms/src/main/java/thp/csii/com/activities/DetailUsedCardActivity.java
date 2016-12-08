package thp.csii.com.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.bean.CardBean;

public class DetailUsedCardActivity extends BaseActivity {
    private LinearLayout ll_back;
    private ImageView img_head;
    private TextView tv_bind_date,tv_yx_date;
    CardBean cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_used_card);
        cb= (CardBean) getIntent().getSerializableExtra("cb");
        if (null!=cb){
            LogUtil.e(DetailUsedCardActivity.this,"bind_date"+cb.getBindDate());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd ");
           // sdf.format(new Date(cb.getBindDate()));
           // LogUtil.e(DetailUsedCardActivity.this,"sdf=="+sdf.format(new Date(cb.getBindDate())));
            LogUtil.e(DetailUsedCardActivity.this,"bind_amount"+cb.getBalAmt());
        }
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
        tv_bind_date= (TextView) findViewById(R.id.tv_bind_date);
        tv_yx_date= (TextView) findViewById(R.id.tv_yx_date);
        img_head= (ImageView) findViewById(R.id.img_head);
        Bitmap bm =((BitmapDrawable) img_head.getDrawable()).getBitmap();
        Bitmap gbm=grey(bm);
        img_head.setImageBitmap(gbm);
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
