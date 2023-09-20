package lq.simple.core.ltr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lq.simple.core.cover.EsCoverHandler;
import lq.simple.core.ltr.constant.LtrDslConstant;
import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.exception.LtrException;
import lq.simple.util.CharacterUtils;
import lq.simple.util.ResultUtils;
import lq.simple.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public abstract class AbstractEsLtrHandler implements EsLtr {

    private static final String TEMPLATE_PRE_FIX = "/_ltr/_featureset/";
    private static final String TEMPLATE_ADD_PRE_FIX = "/_addfeatures";
    private static final String MODEL_LAST_FIX = "/_createmodel";
    private static final String MODEL_PRE_FIX = "_ltr/_model/";
    protected RestHighLevelClient client;
    protected EsCoverHandler esCoverHandler;


    public void setEsCoverHandler(EsCoverHandler esCoverHandler) {
        this.esCoverHandler = esCoverHandler;
    }

    public void setClient(RestHighLevelClient client) {
        this.client = client;
    }


    /**
     * init ltr
     *
     * @return {@link Object}
     */
    @Override
    public Object initLtr() {
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtils.SLASH.concat("_ltr"));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object createTemplate(String templateName, List<Object> param) {
        JSONObject featureset = new JSONObject();
        JSONArray features = new JSONArray();
        features.add(param);
        featureset.put("featureset", features);
        HttpEntity entity = new NStringEntity(featureset.toJSONString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), TEMPLATE_PRE_FIX.concat(templateName));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object updateTemplate(String templateName, List<Object> param) {
        JSONObject featureset = new JSONObject();
        JSONArray features = new JSONArray();
        features.add(param);
        featureset.put("featureset", features);
        HttpEntity entity = new NStringEntity(featureset.toJSONString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), TEMPLATE_PRE_FIX.concat(templateName).concat(TEMPLATE_ADD_PRE_FIX));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object getTemplate(String templateName) {
        Request request = new Request(SearchHttpTypeEnum.GET.name(), TEMPLATE_PRE_FIX.concat(templateName));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object deleteTemplate(String templateName) {
        Request request = new Request(SearchHttpTypeEnum.DELETE.name(), TEMPLATE_PRE_FIX.concat(templateName));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object createTemplateModel(String modelName, Object modelParam) {
        HttpEntity entity = new NStringEntity(modelParam.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), TEMPLATE_PRE_FIX.concat(modelName).concat(MODEL_LAST_FIX));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object deleteTemplateModel(String modelName) {
        Request request = new Request(SearchHttpTypeEnum.DELETE.name(), MODEL_PRE_FIX.concat(modelName));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object getTemplateModel(String modelName) {
        Request request = new Request(SearchHttpTypeEnum.GET.name(), MODEL_PRE_FIX.concat(modelName));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object useLtrDsl(Map<String, String> params, String modelName, List<String> templateNames) {
        if (Objects.isNull(params)) {
            throw new LtrException(LtrException.PARAMS_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(modelName)) {
            throw new LtrException(LtrException.MODEL_NAME_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(modelName)) {
            throw new LtrException(LtrException.TEMPLATE_NAMES_ERROR_MESSAGE);
        }
        Map.Entry<String, String> param = params.entrySet().stream().findFirst().get();
        String dsl = LtrDslConstant.LTR_QUERY_DSL
                .replace("${kw}", param.getKey())
                .replace("${value}", param.getValue())
                .replace("${modelName}", modelName);
        if (templateNames.size() == 1) {
            dsl = dsl.replace("${TemplateName}", templateNames.get(0));
        } else {
            dsl = dsl.replace("\"${TemplateName}\"", templateNames.stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(",")));
        }
        return dsl;
    }
}
