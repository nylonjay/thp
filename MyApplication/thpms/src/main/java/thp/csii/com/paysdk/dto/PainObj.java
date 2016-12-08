package thp.csii.com.paysdk.dto;

/**
 * Created by Charis on 2016/8/25.
 */
public class PainObj {

    /**
     * 授权用户对象
     */
    private User user;

    /**
     * 支付订单信息
     */
    private Order order;

    private String transcode;

    private String consumeSign;

    /**
     * 用来保存订单支付交易的sha1加密数据
     * acno会员账号
     * 对其进行asii码排序和Key一起进行sha1加密的密文
     *
     */
    private String userSign;

    public PainObj(User user, Order order, String transcode) {
        this.user = user;
        this.order = order;
        this.transcode = transcode;
    }

    public String getTranscode() {
        return transcode;
    }

    public void setTranscode(String transcode) {
        this.transcode = transcode;
    }

    public PainObj(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PainObj{" +
                "user=" + user +
                ", order=" + order +
                '}';
    }

    public String getConsumeSign() {
        return consumeSign;
    }

    public void setConsumeSign(String consumeSign) {
        this.consumeSign = consumeSign;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }
}
