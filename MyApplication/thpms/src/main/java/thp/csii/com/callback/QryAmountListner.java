package thp.csii.com.callback;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface QryAmountListner {
    public void OnQryAmountHBYESUceed(Double d);//获取红包余额成功
    public void OnQryAmountHBYEFailed(String e);//获取红包余额失败
}
