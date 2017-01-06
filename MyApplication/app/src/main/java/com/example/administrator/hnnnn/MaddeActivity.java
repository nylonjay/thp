package com.example.administrator.hnnnn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.Crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.MyApp;
import thp.csii.com.TianHongPayMentUtil;
import thp.csii.com.activities.MainActivity;
import thp.csii.com.activities.QRCodeActivity;
import thp.csii.com.activities.UsedCardActivity;
import thp.csii.com.callback.BindCardCallBack;
import thp.csii.com.callback.OnMainActivityFinished;
import thp.csii.com.callback.PayOrderListener;
import thp.csii.com.callback.QryAmountListner;
import thp.csii.com.http.Constant;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.User;
import thp.csii.com.paysdk.util.CommonUtil;
import thp.csii.com.utils.AES;
import thp.csii.com.utils.ToastUtil;

public class MaddeActivity extends AppCompatActivity implements PayOrderListener,QryAmountListner{

    private TianHongPayMentUtil tianHongPayMentUtil;
    private Button btn_pay,btn_main;
    private String Accno="7800100000010129";//7800100000002282林再雄的账号 //7000290210000032307李锐的账号
    //余丹丹的账号 7000290210000054012
    //7000290210040097930
    //7800100000009402  无交易记录的账号
    //private String Oid=String.valueOf(new Random().nextInt(201547994)+222255);
    private String noticeUrl="http://hlj.dev.rainbowcn.net/hongpay/notify";
    private String Oid="m15vvv82vbbv1899426";
    private Double Amount=582.00;
    private String Mid="00195";
    private String consumeSign="ef8b652f5c427f472a29a01c6f1f4595c63181da";// 订单消费这两个都传
    private String userSign="fd3467d948ddacdda07d4ec7f029338ee0ac4143";//进入首页传
    //32307的userSign  4875dbbfd3987427b1dfb5d4c28760e2e5a50f9a
    //无交易记录的userSign:dd7ffbcc76ce695c3b7acff6ed3210c2905d4410
    //余丹丹的usersign a811c7ed7e37d367b1946b859e7a17b20b8b6543
    private Button btn_qry;
    private String userAccno="7800100000010129";
    private BindCardCallBack mBbindCardCallBack;
    private OnMainActivityFinished mOnMainfinished;
    private Button btn_qr,btn_getqr,btn_test;
    private String secrectCode="72c7753fe7005cffa137f44a6581a42385fc5b1c874fa2ee7d1f3b50bc550b390296214738452155b6891ea1522c918b26caa0af37a62be59ab0285e94307cb911324e3967e56ac71d11538a7b6183b83a581a95f521423c376a538d757f8e56ca405bdba489bba5bea6b5bb29e36187f71803c518c0323eb950dbb1b8fe8b77315b4cfa780bbb1fad93626697639976abe8b40dae5abbf20e4438779a77755524d1b13158b949e7b0281525271dc1246685574a87a36749e878a3d1b237b18d5398049b497c6550d91705de624e00001eecdb4aafafca8454ad4fc59f723fad5b8a38d46380f4050474701511d1bd30da67d3583fd83cd18edfee506277b963e84605d3d16b2df101b309532a0201e5";
    static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    private String QRcode="type=TH_PAY&otid=893";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madde);
        // initData();
        Constant.setSERVERHOST("http://192.168.163.20:8080");
        btn_test= (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRcode="type=TH_PAY&otid=7676";
                tianHongPayMentUtil=TianHongPayMentUtil.getInstance(MaddeActivity.this);
                TianHongPayMentUtil.userSign=userSign;
                User u=new User();
                u.setAcno(Accno);
                TianHongPayMentUtil.currentUser=u;
                // tianHongPayMentUtil.GetQRSecrectMessage(QRcode);
                tianHongPayMentUtil.GETQR(QRcode,MaddeActivity.this);
            }
        });
        btn_getqr= (Button) findViewById(R.id.btn_getqr);
        btn_getqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tianHongPayMentUtil=TianHongPayMentUtil.getInstance(MaddeActivity.this);
                TianHongPayMentUtil.userSign=userSign;
                User u=new User();
                u.setAcno(Accno);
                TianHongPayMentUtil.currentUser=u;
               // tianHongPayMentUtil.GetQRSecrectMessage(QRcode);
                tianHongPayMentUtil.GETQR(QRcode,MaddeActivity.this);
            }
        });
        btn_qr= (Button) findViewById(R.id.btn_QR);
        try {
            LogUtil.e(MaddeActivity.this,"解密之后===="+ AES.aesDecrypt(CommonUtil.decodeHex(secrectCode),"njsl15WS5s1f6s2v"));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  initQryData();
                User user = new User();
                user.setAcno(Accno);
                tianHongPayMentUtil=TianHongPayMentUtil.getInstance(MaddeActivity.this);
                TianHongPayMentUtil.userSign=userSign;
                TianHongPayMentUtil.currentUser=user;
               TianHongPayMentUtil.tianHongPayMentUtil.mPayOrderListener=MaddeActivity.this;
                startActivity(new Intent(MaddeActivity.this, QRCodeActivity.class));
            }
        });
        btn_pay= (Button) findViewById(R.id.btn_pay);
        btn_main= (Button) findViewById(R.id.btn_main);
        btn_qry= (Button) findViewById(R.id.btn_qry);
        btn_qry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQryData();
                TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished=mOnMainfinished;
                tianHongPayMentUtil.toQryAcount();
            }
        });
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQryData();
                TianHongPayMentUtil.tianHongPayMentUtil.bindCardCallBack=mBbindCardCallBack;
                TianHongPayMentUtil.tianHongPayMentUtil.onMainActivityFinished=mOnMainfinished;
                tianHongPayMentUtil.JumpTOMainActivity();
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                tianHongPayMentUtil.toPayPredict();
            }
        });
        mBbindCardCallBack=new BindCardCallBack() {
            @Override
            public void onBindSucced() {

            }
        };
        mOnMainfinished=new OnMainActivityFinished() {
            @Override
            public void onFinished(String s) {
            }
        };
        //  startActivity(new Intent(MaddeActivity.this, UsedCardActivity.class));
    }



    private void initData() {
        User user = new User();
        Order order= new Order();
        user.setAcno(userAccno);
        // order.setOid(String.valueOf(new Random().nextInt(201547994)+222255));
        order.setOid(Oid);
        LogUtil.e(MaddeActivity.this,"oid=="+Oid);
        order.setMid(Mid);
        order.setAmount(Amount);
        order.setAccno(Accno);
        order.setNoticeUrl(noticeUrl);
        tianHongPayMentUtil=TianHongPayMentUtil.getInstance(MaddeActivity.this);
        TianHongPayMentUtil.userSign=userSign;
        TianHongPayMentUtil.consumeSign=consumeSign;
        tianHongPayMentUtil.setUO(user,order,MaddeActivity.this);
    }
    private void initQryData() {
        User user = new User();
        user.setAcno(Accno);
        tianHongPayMentUtil=TianHongPayMentUtil.getInstance(MaddeActivity.this);
        TianHongPayMentUtil.userSign=userSign;
        tianHongPayMentUtil.setQA(user,MaddeActivity.this);
    }

    @Override
    public void HandleItMySelf(String msg) {
        ToastUtil.shortToast(MaddeActivity.this,"虹支付自己处理此错误"+msg);
    }

    @Override
    public void PushItoApp(String msg) {
        //  ToastUtil.shortToast(MaddeActivity.this,"红领巾处理此错误:"+msg);
    }

    @Override
    public void PaySucced(String msg) {
        //  ToastUtil.shortToast(MaddeActivity.this,"支付成功:"+msg);
        ToastUtil.shortNToast(MaddeActivity.this,msg);
    }

    @Override
    public void PayFailed(String msg) {
        ToastUtil.shortNToast(MaddeActivity.this,msg);
    }

    @Override
    public void PayCancled() {

    }

    @Override
    public void OnNetWorkError() {

    }

    @Override
    public void OnAcessLoginFailed() {
        //    ToastUtil.shortToast(MaddeActivity.this,"获取登录授权失败");

    }

    @Override
    public void OnAcessLoginSucced() {
        //   ToastUtil.shortToast(MaddeActivity.this,"获取登录授权成功");
    }

    @Override
    public void OnQryAmountHBYESUceed(Double d) {
        LogUtil.e(MaddeActivity.this,"获取到的余额数值为:"+d+"");
    }

    @Override
    public void OnQryAmountHBYEFailed(String d) {

    }
}
