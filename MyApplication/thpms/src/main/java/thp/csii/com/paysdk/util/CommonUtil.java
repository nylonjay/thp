/*
 * Copyright (c) csii.com.cn 2016 zhaojin
 */

package thp.csii.com.paysdk.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 通用工具方法
 *
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-12-21:43
 */

public abstract class CommonUtil {

//    private static final Logger logger;
//
//    static {
//        logger = LoggerFactory.getLogger(CommonUtil.class);
//    }

    public static String generateTimeMillis() {
        return Long.toString(System.currentTimeMillis());
    }

//
//    /**
//     * 获取真实的ip地址
//     *
//     * @param request
//     * @return
//     */
//    public static String getRemoteIPAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        //若经过多次代理则取第一个
//        if (ip.contains(",")) {
//            String[] ips = ip.split(",");
//            ip = ips[0];
//        }
//
//        logger.info("访问ip地址为“{} 真实访问ip地址为：{}", request.getRemoteHost(), ip);
//        return ip;
//    }

    /**
     * 从请求url中获取请求交易名
     *
     * @param servletPath
     * @return
     */
    public static String getTransCode(String servletPath) {
        return servletPath.substring(servletPath.lastIndexOf("/") + 1, servletPath.contains("?") ? servletPath.lastIndexOf("?") : servletPath.length());
    }

    /**
     * 从请求url中获取活动名
     *
     * @param servletPath
     * @return
     */
    public static String getActivityName(String servletPath) {
        String[] array = servletPath.split("/");
        //取倒数第二个参数作为活动名返回
        return array[array.length - 2];
    }

    public static String encodeHexString(byte[] data) {
        return new String(Hex.encodeHex(data));
    }

    public static byte[] decodeHex(String data) throws DecoderException {
        return Hex.decodeHex(data.toCharArray());
    }

    public static String encodeBase64String(byte[] binaryData) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(binaryData, true),"UTF-8");
    }

    public static String decodeBase64Byte(byte[] binaryData) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(binaryData),"UTF-8");
    }
}

