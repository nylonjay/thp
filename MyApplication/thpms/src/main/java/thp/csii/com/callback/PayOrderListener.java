package thp.csii.com.callback;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface PayOrderListener {
    public void HandleItMySelf(String msg);//自己处理 重新获取Token
    public void PushItoApp(String msg);//交给app处理
    public void PaySucced(String msg);//交易成功的回调
    public void PayFailed(String msg);//交易失败的回调
    public void PayCancled();//放弃交易
    public void OnNetWorkError();//网络错误
    public void OnAcessLoginFailed();//授权登录失败
    public void OnAcessLoginSucced();//授权登录成功
//    public void OnQryAmountHBYESUceed(Double d);//获取红包余额成功


}
