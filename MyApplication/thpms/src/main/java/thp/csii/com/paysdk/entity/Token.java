/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.entity;

/**
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-13-12:02
 */

public abstract class Token {

    public abstract void setAccessDate(long accessDate);

    public abstract long getAccessDate();

    public abstract void setUniqueId(String uniqueId);

    public abstract String getUniqueId();

    public abstract int getDelayTime();

    public abstract void setDelayTime(int delayTime);


}
