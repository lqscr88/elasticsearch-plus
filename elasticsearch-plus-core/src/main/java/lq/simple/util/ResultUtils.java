package lq.simple.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ResultUtils {

    /**
     * 得到结果
     *
     * @param response 响应
     * @return {@link String}
     */
    public static String getResult(Response response) {
        String result = Strings.EMPTY;
        try {
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到响应
     *
     * @param request 请求
     * @return {@link Response}
     */
    public static Response getResponse(Request request, RestHighLevelClient client) {
        Response response;
        try {
            response = client.getLowLevelClient().performRequest(request);
        } catch (Exception e) {
            JSONObject errorMessage = JSONObject.parseObject(e.getMessage().substring(e.getMessage().indexOf("{")));
            JSONArray rootCause = errorMessage.getJSONObject("error").getJSONArray("root_cause");
            throw new RuntimeException(rootCause.toJSONString());
        }
        return response;
    }
}
