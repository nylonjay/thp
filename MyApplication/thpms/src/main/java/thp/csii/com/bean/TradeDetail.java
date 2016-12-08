package thp.csii.com.bean;

/**
 * Created by Administrator on 2016/9/2.
 */

import java.io.Serializable;

/**
*TradeDetail 交易明细
*@author nylon
 * created at 2016/9/2 15:10
*/
public class TradeDetail implements Serializable{
    private static final long serialVersionUID = 15313131654643212L;
    String voucher;//订单号
    String txnId;//行为

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    String mid;//商户号
    String txnDate;//2016-11-10 15:24:00格式的时间
    String txnAmt;//订单金额
  //  String sum;//金额

    public TradeDetail() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }
}
