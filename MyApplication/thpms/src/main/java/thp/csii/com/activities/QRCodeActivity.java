package thp.csii.com.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import cn.com.csii.mobile.http.HttpControl;
import cn.com.csii.mobile.http.ResultInterface;
import cn.com.csii.mobile.http.util.LogUtil;
import cn.rainbow.thbase.ui.THProgressDialog;
import thp.csii.com.BaseActivity;
import thp.csii.com.BaseRedActivity;
import thp.csii.com.R;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.callback.PayOrderListener;
import thp.csii.com.callback.QryAmountListner;
import thp.csii.com.http.Constant;
import thp.csii.com.http.HttpUrls;
import thp.csii.com.paysdk.auth.PayConfig;
import thp.csii.com.paysdk.auth.PaySDK;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.paysdk.entity.Token;
import thp.csii.com.paysdk.entity.TokenImpl;
import thp.csii.com.paysdk.util.ConnectUtil;
import thp.csii.com.utils.SharePreferencesUtils;
import thp.csii.com.utils.ToastUtil;

public class QRCodeActivity extends AppCompatActivity implements View.OnClickListener,PayOrderListener{
    private ImageView img_back, img_setting, img_ewm,img_ywm;
    private int QR_WIDTH = 100;
    private int QR_HEIGHT = 100;
    private LinearLayout ll_back;
    private TianHongPayMentUtil util;
    private TextView tv_amount,tv_rmb;
    private String systrace;
    private String pcode;
    private String acno,chanl;
    private String otid;
    private String oid;
    private double amount;
    private TianHongPayMentUtil tianHongPayMentUtil;
    private final PayConfig payConfig=PayConfig.newInstance();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    boolean continues=false;
    private THProgressDialog mTHProgressDialog;
    private String pinNeed;
    private String action;
    private PayOrderListener mPayOrderListener;
    private boolean locked;
    private String pinTag;
    private NumberFormat nf;
    char symbol=165;
    private String ent_mode;
    private String vipCls,pcodeFlag;
    private TextView card_level;
    JSONObject jsonMap;
    private LinearLayout ll_ok;
    private boolean paused=false;
    private TextView tv_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initViews();
        util=TianHongPayMentUtil.getInstance(QRCodeActivity.this);
//        user.setAcno(TianHongPayMentUtil.currentUser.getAcno());
//        //TianHongPayMentUtil.userSign=TianHongPayMentUtil.userSign;
//        util.setQA(user,qm);
        //s startActivity(new Intent(QRCodeActivity.this,EnterCodeActivity.class));

    }

    private void createTXImage(String nylonlee) {
        String str = nylonlee;
        int size = str.length();
        for (int i = 0; i < size; i++) {
            int c = str.charAt(i);
            if ((19968 <= c && c < 40623)) {
                ToastUtil.shortNToast(QRCodeActivity.this, "生成条形码的时刻不能是中文");
                return;
            }

        }
        Bitmap bmp = null;
        try {
            if (str != null && !"".equals(str)) {
                bmp = CreateOneDCode(str);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bmp != null) {
            img_ywm.setImageBitmap(bmp);
        }


    };

    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private void initViews() {
        tv_code= (TextView) findViewById(R.id.tv_code);
        ll_ok= (LinearLayout) findViewById(R.id.ll_ok);
        ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRCodeActivity.this,Pay_Set_Pre.class));
            }
        });
        card_level= (TextView) findViewById(R.id.tv_goldcard);
        nf=NumberFormat.getInstance();
        tv_rmb= (TextView) findViewById(R.id.tv_rmb);
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        ll_back= (LinearLayout) findViewById(R.id.ll_basetitle_back);
        ll_back.setOnClickListener(this);
        img_ewm = (ImageView) findViewById(R.id.img_ewm);
        img_back = (ImageView) findViewById(R.id.tv_basetitle_back);
        img_setting = (ImageView) findViewById(R.id.tv_basetitle_ok);
        img_ywm= (ImageView) findViewById(R.id.img_yiweima);
        mPayOrderListener=new PayOrderListener() {
            @Override
            public void HandleItMySelf(String msg) {

            }

            @Override
            public void PushItoApp(String msg) {

            }

            @Override
            public void PaySucced(String msg) {

            }

            @Override
            public void PayFailed(String msg) {

            }

            @Override
            public void PayCancled() {

            }

            @Override
            public void OnNetWorkError() {

            }

            @Override
            public void OnAcessLoginFailed() {

            }

            @Override
            public void OnAcessLoginSucced() {

            }
        };
    }
    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //  continues=false;
                    //ToastUtil.shortNToast(QRCodeActivity.this,"请求第"+times+"次");
                    if (!paused){
                        QryOrderState();
                    }
                    break;
                case 2:
                    GetUnderLineCode();
                    break;
                case 3:
                    showDialog(false);
                    Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FZXH1JW.TTF");
                    Bundle b=msg.getData();
                    tv_rmb.setVisibility(View.VISIBLE);
                    tv_rmb.setTypeface(tf);
                    tv_rmb.setText(String.valueOf(symbol));
                    tv_amount.setText(""+String.format("%.2f",b.getDouble("totalamt")));
                    hand.sendEmptyMessage(2);

                    if (null!=b.getString("vipCls")){
                        switch (b.getString("vipCls")){
                            case "1"://微卡
                                card_level.setText("微卡");
                                break;
                            case "2"://银卡
                                card_level.setText("银卡");
                                break;
                            case "3"://金卡
                                card_level.setText("金卡");
                                break;
                            case "4"://铂金
                                card_level.setText("铂金卡");
                                break;
                        }
                    }

                    break;
                case 4:
                    //开始查询账户详情 获取余额信息
                    QryCountDetail(HttpUrls.payFunDetaQry);
                    break;
                case 100:
                    String motid=otid+"03";
                    tv_code.setText(motid.replaceAll("\\d{4}(?!$)", "$0 "));
                    createQRImage(motid);//生成二维码
                    createTXImage(motid);//生成条形码
                    if (hand.hasMessages(1)){
                        hand.removeMessages(1);
                    }
                    hand.sendEmptyMessage(1);//开始查询是否生成订单，每六秒钟查询一次
                    break;
                case 5:
                    //获取交易授权成功
                    if (action.equals("cost")){
                        if (null!=token){
                            LogUtil.e(QRCodeActivity.this,"符合免密条件 开始消费");
                            PayOrders(HttpUrls.oderCounsume);
                        }else{
                            LogUtil.e(QRCodeActivity.this,"未获取到交易token");
                        }
                    }
                    break;
            }
        }
    };

    private void PayOrders(String mUrl) {
        HttpControl httpControl = new HttpControl(QRCodeActivity.this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("entMode", ent_mode);
        param.put("pcode",pcode);
        //param.put("pinTag",pinTag);
        param.put("resToken", token.getUniqueId());
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie",SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                if ("0000".equals(res.getString("status"))) {
                    ToastUtil.shortToast(QRCodeActivity.this, res.getString("msg"));
                    mPayOrderListener.PaySucced(json.toJSONString());
                    // PayConfirmActivity.this.onPaySuccess(res.getString("msg"));
                } else if ("4444".equals(res.getString("status"))) {
                    LogUtil.e(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                    mPayOrderListener.PayFailed(json.toJSONString());
                    if (res.getString("errcode").equals("00005")){//令牌校验失败
                        ToastUtil.shortToast(QRCodeActivity.this,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00013")){//用户会话失效
                        // initSessionOutTime("操作失败"+("00013"));
                        ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("4444")){
                        ToastUtil.shortToast(QRCodeActivity.this,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("A5")){
                        ToastUtil.shortToast(QRCodeActivity.this,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("61")){//一次性交易金额过大
                        ToastUtil.shortNToast(QRCodeActivity.this,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("51")){//余额不足
                        ToastUtil.shortNToast(QRCodeActivity.this,res.getString("errmsg"));
                    }else if (res.getString("errcode").equals("00047")){//验签失败
                        ToastUtil.shortToast(QRCodeActivity.this,res.getString("操作失败"+"("+"errmsg"+")"));
                    }

                    else{//交给APP处理
                        mPayOrderListener.PushItoApp(json.toJSONString());
                    }
                }else{
                    mPayOrderListener.PayFailed(json.toJSONString());
                }
            }
            @Override
            public void onError(Object o) {
                mPayOrderListener.PayFailed(o.toString());
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void QryCountDetail(String mUrl) {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url =Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                Double balamt;
                Double cardamt = 0.0;
                Double acye=0.0;
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");

                try {
                    if (null != res) {
                        if (res.getString("status").equals("0000")) {

                            JSONObject datamap = res.getJSONObject("dataMap");
                            if (null != datamap) {
                                JSONObject rsvc = datamap.getJSONObject("rsvc");
                                vipCls = rsvc.getString("vipCls");
                                pcodeFlag = rsvc.getString("pcodeFlag");
                                LogUtil.e(QRCodeActivity.this,"pcodeflag=="+pcodeFlag);
                                TianHongPayMentUtil.currentTel = rsvc.getString("mobile");
                                // balamt = Double.parseDouble(rsvc.getString("balAmt"));//账户总余额
                                balamt = nf.parse(rsvc.getString("balAmt")).doubleValue();
                                //  nf.format(nf.parse(rsvc.getString("balAmt")).doubleValue());
                                pinTag = rsvc.getString("pinTag");
                                if (null != pcodeFlag && "0".equals(pcodeFlag)) {
                                    LogUtil.e(QRCodeActivity.this,"pintag=="+pinTag);
                                    //未开启付款码支付
                                    if (pinTag.equals("00")) {
                                        TianHongPayMentUtil.CodeSetted = false;
                                        initPayNotSettedDialog(getResources().getString(R.string.code_not_setted), "pset");
                                        return;
                                    } else if (pinTag.equals("01")) {
                                        //未开启付款码支付  去开启
                                        TianHongPayMentUtil.CodeSetted = true;
                                        startActivity(new Intent(QRCodeActivity.this,EnterCodeActivity.class));
                                        //ToastUtil.shortToast(context,"已设置支付密码");
                                    }
                                }else{
                                    if (pinTag.equals("00")) {
                                        TianHongPayMentUtil.CodeSetted = false;
                                        initPayNotSettedDialog(getResources().getString(R.string.code_not_setted), "pset");
                                        return;
                                    } else if (pinTag.equals("01")) {
                                        TianHongPayMentUtil.CodeSetted = true;
                                    }
                                }
                                JSONObject acclist = rsvc.getJSONObject("accList");
                                //    acclistJson=acclist.toJSONString();
                                LogUtil.e(QRCodeActivity.this,acclist.toJSONString());

                                if (null != acclist) {
                                    JSONArray account = acclist.getJSONArray("account");
                                    for (int i = 0; i < account.size(); i++) {
                                        //  cardamt += Double.parseDouble(account.getJSONObject(i).getString("balAmt"));
                                        if (!account.getJSONObject(i).getString("accno").equals(TianHongPayMentUtil.currentUser.getAcno())){

                                            cardamt += nf.parse(account.getJSONObject(i).getString("balAmt")).doubleValue();
                                        }else{
                                            acye+=nf.parse(account.getJSONObject(i).getString("balAmt")).doubleValue();
                                        }
                                    }//将所有卡的余额相加
                                    Message msg = new Message();
                                    Bundle b = new Bundle();
                                    b.putDouble("balamt", (acye));
                                    b.putDouble("cardamt", cardamt);
                                    b.putDouble("totalamt", balamt);
                                    b.putString("vipCls",vipCls);
                                    msg.setData(b);
                                    msg.what = 3;
                                    hand.sendMessage(msg);
                                }
                            }
                            locked=false;
                        } else if ("05".equals(res.getString("errcode"))){
                            tv_rmb.setVisibility(View.GONE);
                            ToastUtil.shortToast(QRCodeActivity.this,res.getString("errmsg"));
                            locked=true;
                            //closeALLActvivities();
                            TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished("支付功能已锁定");
                        }else{
                            tv_rmb.setVisibility(View.GONE);
                            locked=false;
                            ToastUtil.shortNToast(QRCodeActivity.this,res.getString("errmsg"));
//                            if ("00013".equals(res.getString("errcode"))){
//                                //session过期弹出操作失败弹框
//                                //  initSessionOutTime("操作失败"+("00013"));
//                                ToastUtil.shortNToast(TianHongPayMentUtil.CurrentContext,res.getString("errmsg"));
//                                //  TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished.onFinished();
//                            }

                        }
                    }
                }catch (Exception e){
                    ToastUtil.shortNToast(QRCodeActivity.this,res.getString("errmsg"));
                }

            }

            @Override
            public void onError(Object o) {
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_basetitle_back) {
            continues=false;
            hand.removeMessages(1);
            QRCodeActivity.this.finish();
        } else if (i == R.id.tv_basetitle_ok) {
            startActivity(new Intent(QRCodeActivity.this, Pay_SettingActivity.class));
        }
    }

    public void initPayNotSettedDialog(String sum, final String action){
        final AlertDialog dialog=new AlertDialog.Builder(QRCodeActivity.this).create();

        dialog.show();
        dialog.getWindow().setContentView(R.layout.wait_dialog);
        TextView tv= (TextView) dialog.getWindow().findViewById(R.id.text_dialog);
        tv.setText(sum);
        dialog.getWindow().findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                dialog.dismiss();
            }
        });
        dialog.getWindow().findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(context,AuthenticationActivity.class));
                Intent in=new Intent(QRCodeActivity.this, SetPayCode_First_Activity.class);
                in.putExtra("from","set");
                in.putExtra("action",action);
                startActivity(in);
                dialog.dismiss();
            }
        });
    }

    public void createQRImage(String url) {
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            img_ewm.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        hand.removeMessages(1);
        paused=true;
        continues=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        continues=false;
    }

    private void getData() {
        Map<String, Object> ajaxData = new HashMap();
        User user = new User();
        user.setAcno(TianHongPayMentUtil.currentUser.getAcno());
        PainObj painObj = new PainObj(user, null);
        painObj.setUserSign(TianHongPayMentUtil.userSign);
        LogUtil.e(QRCodeActivity.this,"Accno=="+TianHongPayMentUtil.currentUser.getAcno()+
                "userSign==="+TianHongPayMentUtil.userSign);
        //end
        try {
            PaySDK paySDK = new PaySDK();
            String url = paySDK.getAccessLoginURI(painObj);
            System.out.println("redirectUrl = [" + url + "]");
            //  ToastUtil.shortToast(context, SharePreferencesUtils.getStringValue(context,"Cookie"));
            Log.i("err", "Cookie缓存===" + SharePreferencesUtils.getStringValue(QRCodeActivity.this, "Cookie"));
            Log.i("err", "SESSIONID==" + SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
            hand.sendEmptyMessage(4);
        } catch (Exception e) {
            hand.sendEmptyMessage(404);
            System.err.println("授权登录发生错误!" + e.getMessage());
        }
    }


    Runnable thread = new Runnable() {
        @Override
        public void run() {
            getData();

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        //请求付款码
        showDialog(true);
        if (hand.hasMessages(1)){
            hand.removeMessages(1);
        }
        new Thread(thread).start();
        if (!continues){
            continues=true;
        }
        paused=false;
    }
    private void QryOrderState() {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url = Constant.SERVERHOST + Constant.AppName + HttpUrls.QryOrderState;
        param.put("acno",TianHongPayMentUtil.currentUser.getAcno());
        param.put("otid",otid);
//        param.put("systrace","152365");
//        param.put("pcode","45632145");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                LogUtil.e(QRCodeActivity.this,"QRYORDERSTATE"+json.toJSONString());
                if (null!=res){
                    LogUtil.e(QRCodeActivity.this,res.toJSONString());
                    if ("0000".equals(res.getString("status"))){
                        JSONObject dataMap=res.getJSONObject("dataMap");
                        if ("2".equals(dataMap.getString("orderStatus"))){
                            continues=false;
                            ent_mode=dataMap.getString("ent_mode");
                            pcode=dataMap.getString("pcode");
                            oid=dataMap.getString("oid");
                            amount=dataMap.getDouble("amount");
                            if (null!=ent_mode){
                                //开始走支付预判
                                LogUtil.e(QRCodeActivity.this,"开始走支付预判");
                                PayPredict(HttpUrls.payPredict);
                            }
                        }else{
                            continues=true;
                            hand.sendEmptyMessageDelayed(1,6000);
                        }

                    }else{
                        ToastUtil.shortNToast(QRCodeActivity.this,"查询订单状态失败");
//                        continues=true;
//                        hand.sendEmptyMessageDelayed(1,6000);
                        //已经获取到了ent_mode  现在开始支付预判
                    }
                }
            }

            @Override
            public void onError(Object o) {
                showDialog(false);
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }
    private void GetUnderLineCode() {
        HttpControl httpControl = new HttpControl(this);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        String url = Constant.SERVERHOST + Constant.AppName + HttpUrls.GetUnderLineQrCode;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestGet, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                showDialog(false);
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                LogUtil.e(QRCodeActivity.this,""+json.toJSONString());
                if (null!=res&&"0000".equals(res.getString("status"))){
                    LogUtil.e(QRCodeActivity.this,res.toJSONString());
                    jsonMap=res.getJSONObject("dataMap");
                  //  acno=jsonMap.getString("acno");
                    otid=jsonMap.getString("otid");
                    hand.sendEmptyMessage(100);
                }else{
                    ToastUtil.shortNToast(QRCodeActivity.this,"请求付款码失败");
                }
            }
            @Override
            public void onError(Object o) {
                showDialog(false);
                ToastUtil.shortToast(TianHongPayMentUtil.CurrentContext,"网络异常");
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private void PayPredict(String mUrl) {//支付预判
        HttpControl httpControl = new HttpControl(TianHongPayMentUtil.CurrentContext);
        httpControl.TimeOut = 20 * 1000;
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("entMode", ent_mode);
        param.put("pcode",pcode);
        String url =  Constant.SERVERHOST + Constant.AppName + mUrl;
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cookie", SharePreferencesUtils.getSession(TianHongPayMentUtil.CurrentContext));
        httpControl.setHeaders(headers);
        httpControl.HttpExcute(url, HttpControl.RequestPost, param, new ResultInterface() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = JSON.parseObject((String) o);
                JSONObject res = json.getJSONObject("res");
                JSONObject datamap = res.getJSONObject("dataMap");
                if (null!=datamap) {
                    JSONObject rsvc = datamap.getJSONObject("rsvc");
                    if (null != datamap.getString("pinTag")) ;
                    LogUtil.i(TianHongPayMentUtil.CurrentContext, "pinTag==" + rsvc.getString("pinTag"));
                    pinNeed = rsvc.getString("pinTag");
                    if ("01".equals(pinNeed)) {
                        //需要交易密码
                        if (TianHongPayMentUtil.CodeSetted) {
                            //跳转到输入支付密码页面
                            TianHongPayMentUtil.currentOder=null;
                            LogUtil.e(TianHongPayMentUtil.CurrentContext,"需要输入密码，跳转到支付订单页面");
                            Intent in=new Intent(QRCodeActivity.this,PayConfirmActivity.class);
                            in.putExtra("action","cost");
                            in.putExtra("entMode",ent_mode);
                            in.putExtra("pcode",pcode);
                            in.putExtra("chanl",chanl);
                            in.putExtra("oid",oid);
                            in.putExtra("amount",amount);
                            Order order=new Order();
                            order.setOid(oid);
                            order.setAmount(amount);
                            tianHongPayMentUtil= TianHongPayMentUtil.getInstance(TianHongPayMentUtil.CurrentContext);
                            tianHongPayMentUtil.setUO(TianHongPayMentUtil.currentUser,order,QRCodeActivity.this);
                            startActivity(in);
                        } else {
                            //未设置支付密码 跳转到设置支付密码页面和短信验证页面
                            Intent in=new Intent(QRCodeActivity.this,SetPayCode_First_Activity.class);
                            in.putExtra("action","cost");
                            in.putExtra("from","set");
                            startActivity(in);
                            // CurrentContext.startActivity(new Intent(CurrentContext,MessageAuthActivity.class));
                        }
                    } else {
                        //不需要支付密码 直接开始订单消费
                        ToastUtil.shortToast(QRCodeActivity.this, "符合免密条件,开始订单消费");
                        action = "cost";
                        new Thread(sendable).start();

                    }
                }
            }

            @Override
            public void onError(Object o) {
                mPayOrderListener.OnNetWorkError();
                Log.i("res err", "" + o.toString());
            }
        });
    }

    private Token token;
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                token = null;
                token = getAccessGenToken();
                LogUtil.e(QRCodeActivity.this, "新的restoken==" + token.getUniqueId());
                hand.sendEmptyMessage(5);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    protected Token getAccessGenToken() throws Exception {//获取交易Token
        Token token;
        try {
            String requestUrl = payConfig.getAccessGenTokenUrl().replace("APPID", payConfig.getAppId()).replace("SECRET", payConfig.getAppSecret());
            JSONObject json = ConnectUtil.doGet(requestUrl, "utf-8");
            if (null == json) {
                throw new Exception("获取授权登录Token凭证失败!");
            }
            JSONObject res = json.getJSONObject("res");
            if (null == res) {
                throw new Exception("获取授权登录Token凭证失败!");
            }
            String status = res.getString("status");
            if (status.equals("4444")) {
                throw new Exception("获取授权登录Token凭证失败!" + res.getString("errcode") + res.getString("errmsg"));
            } else {
                JSONObject accessToken = res.getJSONObject("dataMap").getJSONObject("resubmitToken");
                token = new TokenImpl(accessToken.getString("uniqueId"), accessToken.getLong("accessDate"), accessToken.getIntValue("delayTime"));

                Log.i("new_token",""+token.getUniqueId());
                //   logger.debug("商城获取到支付系统的Token {}", PayConfig.token);
            }

        } catch (Exception e) {
            //  logger.error("error generateAccessURI!", e);
            e.printStackTrace();
            throw new Exception("获取授权登录Token凭证失败!");
        }

        return token;
    }
    @Override
    protected void onPause() {
        super.onPause();
        hand.removeMessages(1);
        paused=true;
        continues=false;
    }

    protected void showDialog(boolean isShow) {

        if (mTHProgressDialog == null && isShow) {
            mTHProgressDialog = cn.rainbow.thbase.ui.THProgressDialog.createDialog(this);
            mTHProgressDialog.setMessage(R.string.loading);
        }

        if (mTHProgressDialog != null) {

            if (isShow) {
                mTHProgressDialog.show();
            } else {
                mTHProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void HandleItMySelf(String msg) {

    }

    @Override
    public void PushItoApp(String msg) {

    }

    @Override
    public void PaySucced(String msg) {
        ToastUtil.shortNToast(QRCodeActivity.this,msg);
        QRCodeActivity.this.finish();
    }

    @Override
    public void PayFailed(String msg) {

    }

    @Override
    public void PayCancled() {

    }

    @Override
    public void OnNetWorkError() {

    }

    @Override
    public void OnAcessLoginFailed() {

    }

    @Override
    public void OnAcessLoginSucced() {

    }
}
