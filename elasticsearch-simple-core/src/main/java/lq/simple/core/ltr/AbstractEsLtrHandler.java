package lq.simple.core.ltr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import joptsimple.internal.Strings;
import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.exception.LtrException;
import lq.simple.util.CharacterUtil;
import lq.simple.util.ResultUtil;
import lq.simple.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public abstract class AbstractEsLtrHandler implements EsLtr {

    private static final String TEMPLATE_PRE_FIX = "/_ltr/_featureset/";
    private static final String MODEL_LAST_FIX = "/_createmodel";

    protected RestHighLevelClient client;

    /**
     * init ltr
     *
     * @return {@link Object}
     */
    @Override
    public Object initLtr() {
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat("_ltr"));
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
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
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }

    @Override
    public Object createTemplateModel(String modelName, Object modelParam) {
        HttpEntity entity = new NStringEntity(modelParam.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), TEMPLATE_PRE_FIX.concat(modelName).concat(MODEL_LAST_FIX));
        request.setEntity(entity);
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
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
        String dsl = LtrDsl.LTR_QUERY_DSL
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
