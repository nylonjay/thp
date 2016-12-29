package thp.csii.com.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CardRecord implements Serializable{
    private static final long serialVersionUID =12413455654643212L;
    private String tr_amt;
    private String time;
    private String invoice;
    private String card_num;
    private String voucher;

    public String getTr_amt() {
        return tr_amt;
    }

    public void setTr_amt(String tr_amt) {
        this.tr_amt = tr_amt;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }
}
