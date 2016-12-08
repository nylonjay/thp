package thp.csii.com.paysdk.dto;

/**
 * Created by Charis on 2016/8/25.
 */
public class Order {

    /**
     * 订单号
     */
    private String oid;

    /**
     * 交易金额
     */
    private Double amount;

    /**
     * 商品id
     */
    private String pid;

    /**
     * 商品名称
     */
    private String pname;

    /**
     * 商品数量
     */
    private Integer pnum;

    /**
     * 下单时间
     */
    private String buytime;

    /**
     * 订单状态
     */
    private String orderstatus;


    /**
     * 会员账号
     */
    private String accno;

    /**
     * 支付令牌
     */
    private String  pcode;

    /**
     * 商户号
     */
    private String mid;

    /**
     * 回调地址
     * @return
     */
    private String noticeUrl;

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Order() {

    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public String getBuytime() {
        return buytime;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", amount=" + amount +
                ", pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", pnum=" + pnum +
                ", buytime='" + buytime + '\'' +
                ", orderstatus='" + orderstatus + '\'' +
                '}';
    }
}
