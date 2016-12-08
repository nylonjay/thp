package thp.csii.com.http;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpUrls {
 public static String getSmsToken="/getSmsToken";
 /** GET
  //String wholePath=Constant.SERVERHOST+getSmsToken;
  //txn_id chnl accno systrace acq_id org_code teller
  获取短信验证码
  */
 public static String OpenPayFunQry="/openPayFunQry?";

 //public static String PayFunDetaQry="/payFunDetaQry?";
 /**
  * GET
  * 支付功能开通情况查询
  */
 public static String OpenPayFunConfirm="/openPayFunConfirm";
 /**
  * POST
  * 支付功能开通
  */
 public static String closePayConfirm="/closePayConfirm?";
 /**
  * POST 关闭支付功能
  */
 public static String openPayConfirm="/openPayConfirm?";
 /**
  * POST 开启支付功能
  */

 public static String bindCardConfirm="/bindCardConfirm";
 /**
  * POST 绑定储值卡
  */
 public static String unbindCardConfirm="/unbindCardConfirm?";
 /**
  * POST 解除绑定储值卡
  */
 public static String modifyTrsPwdConfirm="/modifyTrsPwdConfirm?";
 /**
  * POST 交易密码修改
  */
 public static String resetTrsPwdConfirm="/resetTrsPwdConfirm?";
 /**
  * POST 重置/设置密码
  */

 public static String modifyPayFunConfirm="/modifyPayFunConfirm?";
 /**
  * POST 支付账户修改
  */
 public static String payFunDetaQry="/payFunDetaQry?";
 /**
  * GET 支付账户详细情况查询
  */
 public static String trsPwdValidate="/trsPwdValidate?";
 /**
  * POST 交易密码验证
  */
 public static String getUnlineQrCode="/getUnlineQrCode?";
 /**
  * GET 请求付款码
  */

 public static String oderCounsume="/orderConsume?";
 /**
  * POST 订单消费
  */
 public static String orderRevoke="/payOrderRevoke";
 /**
  * GET 支付订单撤销
  */

 public static String payPredict="/payPredict";
 /**
  * POST 支付预判
  */

 public static String rechargeCallBack4AlipayServerConfirm="/rechargeCallBack4AlipayServerConfirm";
 /**
  * POST 红包账户预充值
  */
 public static String rechargeOrderActivate="/rechargeOrderActivate";
 /**
  * GET 充值订单激活
  */

 public static String rechargeOrderRevoke="/rechargeOrderRevoke";
 /**
  * GET 充值订单撤销
  */

 public static String orderTracking="/orderTracking";
 /**
  * GET 订单查询
  */
 public static String payTokenRequest="/payTokenRequest";
 /**
  * GET 支付令牌请求
  */

 public static String QryTradeDetail="/tradeList";

 public static String GetUnderLineQrCode="/getUnlineQrCode";//请求付款码

 public static String QryOrderState="/selectOrderStatus";

 public static String ModifyPayFunConfirm="/modifyPayFunConfirm";//post

 public static String getOrderInfoAPP="/getOrderInfoAPP";//get


}
