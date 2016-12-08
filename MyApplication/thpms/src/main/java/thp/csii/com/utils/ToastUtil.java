package thp.csii.com.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtil {
    private static Toast mToast = null;
    private static boolean show=false;

    /**
     * 弹出自定义短toast框
     *
     * @param context
     * @param message
     */
    public static void shortToast(final Context context, String message) {
       // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        if (mToast == null) {
        	mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
        	mToast.setText(message);
        }
        if (show){
            mToast.show();
        }
    }

    public static void shortNToast(final Context context, String message) {
        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
            mToast.show();
    }

    /**
     * 弹出自定义长toast框
     *
     * @param context
     * @param message
     */
    public static void longToast(final Context context, String message) {
        if (mToast == null) {
        	mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
        	mToast.setText(message);
        }
        mToast.show();
    }
}
