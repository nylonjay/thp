package thp.csii.com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/11/14.
 */
public class ReadImage {
    public static void toGrayImage(String source, String dest)
    {
        try
        {
            Bitmap bitmap = BitmapFactory.decodeFile(source);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap grayImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //
            Canvas canvas = new Canvas(grayImg);

            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                    colorMatrix);
            paint.setColorFilter(colorMatrixFilter);
            canvas.drawBitmap(bitmap, 0, 0, paint);
//            // canvas.
//            File file = new File(dest);
//            boolean success = file.createNewFile();
//            FileOutputStream stream = new FileOutputStream(file);
//            grayImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            stream.flush();
//            stream.close();

            bitmap.recycle();
            grayImg.recycle();

        }
        catch (Exception e)
        {
            @SuppressWarnings("unused")
            String msg = e.getMessage();
        }

    }
}
