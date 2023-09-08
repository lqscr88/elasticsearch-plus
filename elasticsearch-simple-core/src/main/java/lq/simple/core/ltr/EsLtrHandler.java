package lq.simple.core.ltr;

import com.alibaba.fastjson.JSONObject;
import lq.simple.core.cover.EsCover;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public class EsLtrHandler extends AbstractEsLtrHandler implements EsCover {

    public EsLtrHandler(RestHighLevelClient client){
        super.client = client;
    }

    /**
     * 转换
     *
     * @param response 响应
     * @return {@link JSONObject}
     */
    @Override
    public JSONObject cover(String response) {
        return JSONObject.parseObject(response);
    }


    @Override
    public Object createTemplate(String templateName, List<Object> param) {
        return cover(super.createTemplate(templateName, param).toString());
    }

    @Override
    public Object createTemplateModel(String modelName, Object modelParam) {
        return cover(super.createTemplateModel(modelName, modelParam).toString());
    }
}
