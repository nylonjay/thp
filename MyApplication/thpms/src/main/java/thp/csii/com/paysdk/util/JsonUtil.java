package thp.csii.com.paysdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

//    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 把obj转成jsonstr
     *
     * @param obj
     * @return
     */
    public static String obj2json(Object obj) {
        String jsonstr = JSON.toJSONString(obj);
        //logger.info("obj2json:::" + jsonstr);
        return jsonstr;
    }

    /**
     * 根据clz返回一个bean
     *
     * @param jsonstr
     * @param clz
     * @return
     */
    public static <T> T json2obj(String jsonstr, Class<T> clz) {
        T t = JSON.parseObject(jsonstr, clz);
        //logger.info("json2obj:::" + t);
        return t;
    }

    /**
     * 把jsonstr转成obj
     *
     * @param jsonstr
     * @return
     */
    public static Object json2obj(String jsonstr) {
        Object obj = JSON.parseObject(jsonstr);
        //logger.info("json2obj:::" + obj);
        return obj;
    }

    /**
     * @param jsonstr
     * @return
     */
    public static JSONObject json2jsonobj(String jsonstr) {
        JSONObject obj = JSON.parseObject(jsonstr);
        //logger.info("json2obj:::" + obj);
        return obj;
    }
}
