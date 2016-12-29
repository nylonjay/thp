package thp.csii.com.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */

public class RecordDetail{
    String tradeNo;
    String ctime;
    String trs_amt;
    List<RecordBean> cardList;

    public List<RecordBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<RecordBean> cardList) {
        this.cardList = cardList;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTrs_amt() {
        return trs_amt;
    }

    public void setTrs_amt(String trs_amt) {
        this.trs_amt = trs_amt;
    }
}
