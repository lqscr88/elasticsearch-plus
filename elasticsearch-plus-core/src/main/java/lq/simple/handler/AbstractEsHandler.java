package lq.simple.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import lq.simple.bean.req.QueryReq;
import lq.simple.bean.resp.RestResp;
import lq.simple.builder.MatchQueryBuilder;
import lq.simple.builder.QueryStringBuilder;
import lq.simple.core.EsOperate;
import lq.simple.core.cover.EsCoverHandler;
import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.exception.SearchException;
import lq.simple.plus.lamda.wrapper.Wrapper;
import lq.simple.result.SearchResult;
import lq.simple.util.CharacterUtils;
import lq.simple.util.ResultUtils;
import lq.simple.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
@Log4j2
public abstract class AbstractEsHandler implements EsOperate {

    public static final String DOC = "_doc";
    public static final String UPDATE = "_update";
    public static final String ID = "id";
    public static final String _INDEX = "_index";
    public static final String _ID = "_id";
    public static final String _TYPE = "_type";
    public static final String LINE_SEPARATOR = "line.separator";
    public static final String _BULK = "_bulk";
    public static final String INDEX = "index";

    protected EsCoverHandler esCoverHandler;
    protected RestHighLevelClient client;


    /**
     * 匹配
     *
     * @param queryReq 查询请求
     * @return {@link RestResp}<{@link SearchResult}>
     */
    @Override
    public RestResp<SearchResult> match(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(queryReq.getField(), queryReq.getKw());
        matchQueryBuilder.boost(queryReq.getBoost());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            matchQueryBuilder.analyzer(queryReq.getAnalyzer());
        }
        return getSearchResultRestResp(queryReq, builder, matchQueryBuilder);
    }

    @Override
    public <T> RestResp<SearchResult> match(Wrapper<T> queryWrapper) {
        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        SearchSourceBuilder builder = getSearchSourceBuilder();
        if (paramNameValuePairs.size() != 1) {
            throw new SearchException(SearchException.MATCH_ONE);
        }
        Map.Entry<String, Object> data = paramNameValuePairs.entrySet().stream().findFirst().get();
        String field = data.getKey();
        float boost = 1f;
        if (data.getKey().contains(Wrapper.BOOST)) {
            field = field.split(Wrapper.BOOST)[0];
            boost = Float.parseFloat(field.split(Wrapper.BOOST)[1]);
        }
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, data.getValue());
        matchQueryBuilder.boost(boost);
        if (StringUtils.isNotEmpty(queryWrapper.getAnalyzer())) {
            matchQueryBuilder.analyzer(queryWrapper.getAnalyzer());
        }
        return getSearchResultRestResp(queryWrapper.getFrom(),queryWrapper.getSize(),queryWrapper.getIndex(), builder, matchQueryBuilder);
    }

    /**
     * 得到搜索来源工厂
     *
     * @return {@link SearchSourceBuilder}
     */
    private SearchSourceBuilder getSearchSourceBuilder() {
        return new SearchSourceBuilder();
    }

    private RestResp<SearchResult> getSearchResultRestResp(QueryReq queryReq, SearchSourceBuilder builder, QueryBuilder matchQueryBuilder) {
        return getSearchResultRestResp(queryReq.getFrom(), queryReq.getSize(), queryReq.getIndex(), builder, matchQueryBuilder);
    }

    private RestResp<SearchResult> getSearchResultRestResp(Integer from, Integer size, String index, SearchSourceBuilder builder, QueryBuilder matchQueryBuilder) {
        SearchSourceBuilder query = builder.from(from)
                .size(size)
                .query(matchQueryBuilder);
        HttpEntity entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH + "_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        log.info("*********************************DSL: {}", query.toString());
        request.setEntity(entity);
        return esCoverHandler.doCover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }


    /**
     * 匹配短语
     *
     * @param queryReq 查询请求
     * @return {@link RestResp}<{@link SearchResult}>
     */
    @Override
    public RestResp<SearchResult> matchPhrase(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(queryReq.getField(), queryReq.getKw());
        matchPhraseQueryBuilder.boost(queryReq.getBoost());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            matchPhraseQueryBuilder.analyzer(queryReq.getAnalyzer());
        }
        return getSearchResultRestResp(queryReq, builder, matchPhraseQueryBuilder);
    }


    /**
     * 查询字符串
     *
     * @param queryReq 查询请求
     * @return {@link RestResp}<{@link SearchResult}>
     */
    @Override
    public RestResp<SearchResult> queryString(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        QueryStringBuilder queryStringBuilder = QueryStringBuilder.queryStringQuery(queryReq.getKw());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            queryStringBuilder.analyzer(queryReq.getAnalyzer());
        }
        if (Objects.nonNull(queryReq.getField())) {
            queryStringBuilder.fields(queryReq.getFields());
        }
        return getSearchResultRestResp(queryReq, builder, queryStringBuilder);
    }


    /**
     * 搜索
     *
     * @param index 指数
     * @param json  json
     * @return {@link RestResp}<{@link SearchResult}>
     */
    @Override
    public RestResp<SearchResult> search(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat("_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        log.info("*********************************DSL: {}", json);
        request.setEntity(entity);
        return esCoverHandler.doCover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }

    @Override
    public RestResp<SearchResult> all(String index, Integer page, Integer size) {
        String json = "{\"from\":0,\"size\":1000,\"query\":{\"match_all\":{}}}";
        if (page != null && size != null) {
            json = json.replace("0", page.toString()).replace("100", size.toString());
        }
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat("_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        log.info("*********************************DSL: {}", json);
        request.setEntity(entity);
        return esCoverHandler.doCover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }

    @Override
    public Object save(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request;
        if (json.contains(ID)) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            String id = jsonObject.getString(ID);
            request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat(DOC).concat(CharacterUtils.SLASH).concat(id));
        } else {
            request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat(DOC));
        }
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }


    @Override
    public Object saveOrUpdateBatch(String index, List<String> json) {
        StringBuilder dsl = new StringBuilder();
        JSONArray objects = JSONArray.parseArray(json.toString());
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            JSONObject indexSetting = new JSONObject();
            Map<String, Object> indexSettingParam = new HashMap<>();
            indexSettingParam.put(_INDEX, index);
            if (Objects.nonNull(next.getString(ID))) {
                indexSettingParam.put(_ID, next.getString(ID));
                next.remove(ID);
            }
            indexSetting.put(INDEX, indexSettingParam);
            dsl.append(indexSetting.toJSONString()).append(System.getProperty(LINE_SEPARATOR)).append(next.toJSONString()).append(System.getProperty(LINE_SEPARATOR));
        }
        HttpEntity entity = new NStringEntity(dsl.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtils.SLASH.concat(_BULK));
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }

    @Override
    public Object update(String index, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String id = jsonObject.getString(ID);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat(DOC).concat(CharacterUtils.SLASH).concat(id).concat(CharacterUtils.SLASH).concat(UPDATE));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object detele(String index, String id) {
        Request request = new Request(SearchHttpTypeEnum.DELETE.name(), CharacterUtils.SLASH.concat(index).concat(CharacterUtils.SLASH).concat(DOC).concat(CharacterUtils.SLASH).concat(id));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object deleteBatch(String index, List<String> ids) {
        StringBuilder dsl = new StringBuilder();
        ids.forEach(id -> {
            JSONObject indexSetting = new JSONObject();
            Map<String, Object> indexSettingParam = new HashMap<>();
            indexSettingParam.put(_INDEX, index);
            indexSettingParam.put(_TYPE, DOC);
            indexSettingParam.put(_ID, id);
            indexSetting.put("delete", indexSettingParam);
            dsl.append(indexSetting.toJSONString()).append(System.getProperty(LINE_SEPARATOR));

        });
        HttpEntity entity = new NStringEntity(dsl.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtils.SLASH.concat(_BULK));
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtils.getResult(ResultUtils.getResponse(request, client)));
    }
}
