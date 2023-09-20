package lq.simple.core.ltr;

import com.alibaba.fastjson.JSONObject;
import lq.simple.core.cover.EsCover;
import lq.simple.core.cover.EsCoverHandler;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public class EsLtrHandler extends AbstractEsLtrHandler{

    public EsLtrHandler(RestHighLevelClient client){
        super.setClient(client);
        super.setEsCoverHandler(esCoverHandler);
    }

    @Override
    public Object   createTemplate(String templateName, List<Object> param) {
        return esCoverHandler.cover(super.createTemplate(templateName, param).toString());
    }

    @Override
    public Object updateTemplate(String templateName, List<Object> param) {
        return esCoverHandler.cover(super.updateTemplate(templateName, param).toString());
    }

    @Override
    public Object deleteTemplate(String templateName) {
        return esCoverHandler.cover(super.deleteTemplate(templateName).toString());
    }

    @Override
    public Object getTemplate(String templateName) {
        return esCoverHandler.cover(super.getTemplate(templateName).toString());
    }

    @Override
    public Object createTemplateModel(String modelName, Object modelParam) {
        return esCoverHandler.cover(super.createTemplateModel(modelName, modelParam).toString());
    }

    @Override
    public Object deleteTemplateModel(String modelName) {
        return esCoverHandler.cover(super.deleteTemplateModel(modelName).toString());
    }

    @Override
    public Object getTemplateModel(String modelName) {
        return esCoverHandler.cover(super.getTemplateModel(modelName).toString());
    }
}
