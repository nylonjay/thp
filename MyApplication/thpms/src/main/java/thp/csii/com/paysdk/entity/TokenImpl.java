/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.entity;

/**
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-13-12:03
 */

public class TokenImpl extends Token {

    private String uniqueId;
    private long accessDate;
    private int delayTime;

    public TokenImpl(String uniqueId, long accessDate,int delayTime) {
        this.uniqueId = uniqueId;
        this.accessDate = accessDate;
        this.delayTime = delayTime;
    }

    @Override
    public void setAccessDate(long accessDate) {
        this.accessDate = accessDate;
    }

    @Override
    public long getAccessDate() {
        return accessDate;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public int getDelayTime() {
        return delayTime;
    }

    @Override
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "TokenImpl{" +
                "uniqueId='" + uniqueId + '\'' +
                ", accessDate=" + accessDate +
                ", delayTime=" + delayTime +
                '}';
    }
}
