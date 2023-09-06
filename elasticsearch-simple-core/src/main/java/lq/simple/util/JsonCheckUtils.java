package lq.simple.util;

import com.alibaba.fastjson.JSONObject;

public class JsonCheckUtils {

    public static Boolean checkJson(String json){
        Boolean check = Boolean.FALSE;
        try{
            JSONObject.parseObject(json);
        }catch (Exception e){
            e.printStackTrace();
            check =  Boolean.TRUE;
        }
        return check;
    }
}
