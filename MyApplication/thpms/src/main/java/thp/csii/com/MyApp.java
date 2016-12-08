package thp.csii.com;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.User;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyApp extends Application {
    public static MyApp instance=null;
    private  static List<Activity> activities=new ArrayList<Activity>();
    private static final String PLAIN = "plain";
    private static final String STATUS_ERR = "4444";
    private static final String STATUS_OK = "0000";
    private static final String HTTP_CONTENT_TYPE_JSON_UTF = "application/json;charset=utf-8";
    public static String SESSIsONID="0";
   // public static boolean CodeSetted=false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

    }



    public static Application getInstancea(){
        return instance;
    }
    public static void exitThPay(){
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities=null;
        System.exit(0);
    }
    public static void addActivities(Activity a){
        activities.add(a);
    }

}
