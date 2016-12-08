package thp.csii.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtils {

    private static final String NAME = "User";

    public static boolean save(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        return edit.commit();
    }
    public static String getSession(Context context){
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString("Cookie", null);

    }

    public static boolean save(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        return edit.commit();
    }
    
    public static boolean save(Context context, String key, Long value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        return edit.commit();
    }

    public static boolean save(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public static String getStringValue(Context context, String key) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(key, null);
    }

    public static boolean getBooleanValue(Context context, String key) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(key, false);
    }
    

    public static int getIntValue(Context context, String key) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(key, 0);
    }

    public static boolean contairn(Context context, String key) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).contains(key);
    }

    public static Long getLongValue(Context context, String key) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getLong(key,0);
    }
    
    // 清空所有保存的数据
    public static void cleanUpData(Context ctx) {
    	SharedPreferences sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    	sp.edit().clear().commit();
    }
}
