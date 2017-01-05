package thp.csii.com.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.*;

import cn.com.csii.mobile.http.util.LogUtil;
import thp.csii.com.paysdk.dto.Order;
import thp.csii.com.paysdk.dto.PainObj;
import thp.csii.com.paysdk.dto.User;

/**
 * @author Charis Email:335856460@qq.com
 * @version 创建时间：2016/11/3
 */

public class SHA1 {
//    private static final Logger logger = LoggerFactory.getLogger(SHA1.class);
    /**
     * 对数据完成SHA1加密
     * @param str
     * @return
     */
    public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    /**
     * map key转变为array
     * @param map
     * @return
     */
    public static String[] MapKey2ArrayAndSortByASCII(Map map) {
        // 将Map Key 转化为List
        List<String> mapKeyList = new ArrayList<String>(map.keySet());
        String[] arr = new String[mapKeyList.size()];
        String[] descArr = mapKeyList.toArray(arr);
        Arrays.sort(descArr); // 对key按照 字典顺序排序
     //   logger.debug("ASCII排序："+JSONObject.toJSONString(descArr));
        return descArr;
    }


    /**
     * 构建签名原文
     *
     * @param signFields 参数列表
     * @param map 参数map
     * @return
     */
    public static String orgSHA1SignSrc(String[] signFields, Map map) {
        StringBuffer signSrc = new StringBuffer("");
        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;
        for (String field : signFields) {
            signSrc.append(field);
            signSrc.append("=");
            if(field.equals("amount")) {
                String amount = df.format(map.get(field));
                signSrc.append(amount);
            } else {
                signSrc.append(
                        (StringUtil.isEmpty(String.valueOf(map.get(field))) ? "" : map.get(field)));
            }
            // 最后一个元素后面不加&
            if (i < (signFields.length - 1)) {
                signSrc.append("&");
            }
            i++;
        }
        return signSrc.toString() + "&key=k0*Y9t%n!nM8B&r%dF#gAA";
    }

    public static String orgSHA1SignSrc1(String[] signFields, Map map) {
        StringBuffer signSrc = new StringBuffer("");
        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;
        for (String field : signFields) {
            signSrc.append(field);
            signSrc.append("=");
            if(field.equals("amount")) {
                String amount = df.format(map.get(field));
                signSrc.append(amount);
            } else {
                signSrc.append(
                        (StringUtil.isEmpty(String.valueOf(map.get(field))) ? "" : map.get(field)));
            }
            // 最后一个元素后面不加&
            if (i < (signFields.length - 1)) {
                signSrc.append("&");
            }
            i++;
        }
        return signSrc.toString();
    }
    /**
     * 校验SHA1值
     * @param map
     * @return
     */
    public static boolean checkSha1(Map map,String sign) {
        //构建需要校验的原文
        String orgString = orgSHA1SignSrc(MapKey2ArrayAndSortByASCII(map),map);
      // logger.debug("签名原文："+orgString);
        String mySign = getSha1(orgString);
        if(StringUtil.isNotEmpty(mySign)){
            if(mySign.trim().equals(sign)){
         //       logger.debug("SHA1校验通过");
                return true;
            }
        }
        return false;
    }

    /**
     * 生成用户签名
     * @param acno
     * @return
     */
    public static String getUserSign(String acno) {
        Map map = new HashMap();
        map.put("acno",acno);
        String orgString = orgSHA1SignSrc(MapKey2ArrayAndSortByASCII(map),map);
     //   logger.debug("签名原文："+orgString);
        return getSha1(orgString);
    }

    /**
     * 生成订单签名
     * @param order 订单信息
     * @param acno 会员账号
     * @return
     */
    public static String getOrderSign(Order order,String acno) {
        Map map = new HashMap();
        map.put("amount",order.getAmount());
        map.put("acno",order.getAccno());
        map.put("user",acno);
        map.put("mid",order.getMid());
        map.put("oid",order.getOid());
        map.put("noticeUrl",order.getNoticeUrl());
        String orgString = orgSHA1SignSrc(MapKey2ArrayAndSortByASCII(map),map);
      //  logger.debug("签名原文："+orgString);
        return getSha1(orgString);
    }


    /**
     * 校验SHA1值(订单)（旧版）
     * @param painObj
     * @param sign
     * @return
     */
    public static boolean checkSha1Order(PainObj painObj, String sign) {
        Map map = new HashMap();
        User user = painObj.getUser();
        Order order = painObj.getOrder();
        map.put("amount",order.getAmount());
        map.put("acno",order.getAccno());
        map.put("user",user.getAcno());
        map.put("mid",order.getMid());
        map.put("oid",order.getOid());
        return checkSha1(map,sign);
    }
    /**
     * 校验SHA1值(订单)（新版）
     * @param painObj
     * @param sign
     * @return
     */
    public static boolean checkSha1OrderNew(PainObj painObj, String sign) {
        Map map = new HashMap();
        User user = painObj.getUser();
        Order order = painObj.getOrder();
        map.put("amount",order.getAmount());
        map.put("acno",order.getAccno());
        map.put("user",user.getAcno());
        map.put("mid",order.getMid());
        map.put("oid",order.getOid());
        map.put("noticeUrl",order.getNoticeUrl());
        return checkSha1(map,sign);
    }
    /**
     * 校验SHA1值(用户)
     * @param painObj
     * @param sign
     * @return
     */
    public static boolean checkSha1User(PainObj painObj, String sign) {
        Map map = new HashMap();
        User user = painObj.getUser();
        Order order = painObj.getOrder();
        map.put("acno",user.getAcno());
        return checkSha1(map,sign);
    }

    /**
     * 校验SHA1值（订单）-IOS 旧版
     * @param user
     * @param order
     * @param sign
     * @return
     */
    public static boolean checkSha1OrderIOS(User user, Order order, String sign) {
        Map map = new HashMap();
        map.put("amount",order.getAmount());
        map.put("acno",order.getAccno());
        map.put("user",user.getAcno());
        map.put("mid",order.getMid());
        map.put("oid",order.getOid());
        return checkSha1(map,sign);
    }
    /**
     * 校验SHA1值（订单）-IOS 新版
     * @param user
     * @param order
     * @param sign
     * @return
     */
    public static boolean checkSha1OrderIOSNew(User user, Order order, String sign) {
        Map map = new HashMap();
        map.put("amount",order.getAmount());
        map.put("acno",order.getAccno());
        map.put("user",user.getAcno());
        map.put("mid",order.getMid());
        map.put("oid",order.getOid());
        return checkSha1(map,sign);
    }
    /**
     * 校验SHA1值(用户)-IOS
     * @param user
     * @param sign
     * @return
     */
    public static boolean checkSha1UserIOS(User user,String sign) {
        Map map = new HashMap();
        map.put("acno",user.getAcno());
        return checkSha1(map,sign);
    }




    public static void main(String[] args) {
//        String pian2 = "acno=175&amount=888&mid=00195&oid=222&user=1213&key=k0*Y9t%n!nM8B&r%dF#gAA";
//        //密文：3ee13abb821b6966f6b407e877a0b2bf117a0cf8

//        //logger.debug("密文："+getSha1(pian2));
//
        String sign = "6281d1a1ea5d04145c94fa7a12871ac248b6459d";
        Map map = new HashMap();
//        map.put("amount",582.00);
        map.put("acno","7800100000010129");
//        map.put("user","7800100000010129");
//        map.put("mid","00195");
//        map.put("oid","m15vvv82vbbv1899426");
//        map.put("noticeUrl","http://hlj.dev.rainbowcn.net/hongpay/notify");
//7800100000002282&amount=0.02&mid=00195&oid=m517361601000045&user=7800100000002282
//        logger.debug(checkSha1(map,sign));
//        logger.debug(StringUtil.isNotEmpty("123"));
        String orgString = orgSHA1SignSrc(MapKey2ArrayAndSortByASCII(map),map);
        System.out.println("orgstring=="+getSha1(orgString));
       // LogUtil.e();
//        logger.debug(orgString);
//        logger.debug(getSha1(orgString));
//        Double a = 888.00;
//        double d1 = 3.1415926;
//        double   myNumber=321.4321;
//        DecimalFormat df = new DecimalFormat("0.00");
//        String CNY = df.format(a);
//       logger.debug(CNY);
    }


}
