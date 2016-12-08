package thp.csii.com.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class CardBean implements Serializable{
    private static final long serialVersionUID =1241342331654643212L;
    public CardBean() {
    }

    String balAmt;
    String accno;
    String bindDate;


    public String getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(String balAmt) {
        this.balAmt = balAmt;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getBindDate() {
        return bindDate;
    }

    public void setBindDate(String bindDate) {
        this.bindDate = bindDate;
    }
}
