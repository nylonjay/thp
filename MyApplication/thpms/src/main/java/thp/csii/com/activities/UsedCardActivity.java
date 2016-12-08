package thp.csii.com.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.views.StepView;

public class UsedCardActivity extends BaseActivity {


    private StepView mStepView;
    private boolean sended=false;
    private ImageView img_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_card);
        setTitleText(R.string.sp_detail);
        mStepView=(StepView)this.findViewById(R.id.step_view);
        img_head= (ImageView) findViewById(R.id.img_head);
        List<String> titles=new ArrayList<String>();
        titles.add("待领取");
        titles.add("赠送成功");
        mStepView.setStepTitles(titles);
        mStepView.setTotalStep(2);
        mStepView.setSmallRadius(15);
        mStepView.setLargeRadius(15);
        mStepView.setDoneColor(Color.parseColor("#94cf22"));
        mStepView.setUnDoneColor(Color.parseColor("#dadada"));
        //调整矩形的位置可以到stepBar#206行
        //如果是已赠送 将图片变为灰色  stepview.nextstep
        if (sended){
            mStepView.nextStep();
            Bitmap bm =((BitmapDrawable) img_head.getDrawable()).getBitmap();
            Bitmap gbm=grey(bm);
            img_head.setImageBitmap(gbm);
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
