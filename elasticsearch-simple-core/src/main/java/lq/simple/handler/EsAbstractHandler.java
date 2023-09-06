package lq.simple.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import lq.simple.bean.req.AggReq;
import lq.simple.bean.req.HighlightReq;
import lq.simple.bean.req.QueryReq;
import lq.simple.bean.resp.RestResp;
import lq.simple.builder.AggregationBuilders;
import lq.simple.builder.MatchQueryBuilder;
import lq.simple.builder.QueryStringBuilder;
import lq.simple.core.EsLtr;
import lq.simple.core.EsOperate;
import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.exception.AggException;
import lq.simple.result.SearchResult;
import lq.simple.util.CharacterUtil;
import lq.simple.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
@Log4j2
public abstract class EsAbstractHandler implements EsOperate, EsLtr {


    protected EsCoverHandler esCoverHandler;

    protected RestHighLevelClient client;

    /**
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object setIndex(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index));
        request.setEntity(entity);
        return getResult(getResponse(request));
    }

    /**
     * 得到索引
     *
     * @param index 索引
     * @return {@link Object}
     */
    @Override
    public Object getIndex(String index) {
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(index));
        return getResult(getResponse(request));
    }

    /**
     * 得到结果
     *
     * @param response 响应
     * @return {@link String}
     */
    private String getResult(Response response) {
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
    private Response getResponse(Request request) {
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

    /**
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object setMapping(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_mappings"));
        request.setEntity(entity);
        return getResult(getResponse(request));
    }


    /**
     * 设置Settings
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object setSetting(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_settings"));
        request.setEntity(entity);
        return getResult(getResponse(request));
    }

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

    /**
     * 得到搜索来源工厂
     *
     * @return {@link SearchSourceBuilder}
     */
    private SearchSourceBuilder getSearchSourceBuilder() {
        return new SearchSourceBuilder();
    }

    private RestResp<SearchResult> getSearchResultRestResp(QueryReq queryReq, SearchSourceBuilder builder, QueryBuilder matchQueryBuilder) {
        SearchSourceBuilder query = builder.from(queryReq.getFrom())
                .size(queryReq.getSize())
                .query(matchQueryBuilder);
        log.info("*********************************DSL: {}", query.toString());
        HttpEntity entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(queryReq.getIndex()).concat(CharacterUtil.SLASH + "_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        request.setEntity(entity);
        return esCoverHandler.cover(getResult(getResponse(request)));
    }

    /**
     * 匹配dsl
     *
     * @param queryReq 查询请求
     * @return {@link Object}
     */
    @Override
    public Object matchDsl(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(queryReq.getField(), queryReq.getKw());
        matchQueryBuilder.boost(queryReq.getBoost());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            matchQueryBuilder.analyzer(queryReq.getAnalyzer());
        }
        SearchSourceBuilder query = builder.from(queryReq.getFrom())
                .size(queryReq.getSize())
                .query(matchQueryBuilder);
        return query.toString();
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
     * 匹配短语dsl
     *
     * @param queryReq 查询请求
     * @return {@link Object}
     */
    @Override
    public Object matchPhraseDsl(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(queryReq.getField(), queryReq.getKw());
        matchPhraseQueryBuilder.boost(queryReq.getBoost());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            matchPhraseQueryBuilder.analyzer(queryReq.getAnalyzer());
        }
        SearchSourceBuilder query = builder
                .from(queryReq.getFrom())
                .size(queryReq.getSize())
                .query(matchPhraseQueryBuilder);
        return query.toString();
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
     * 查询字符串dsl
     *
     * @param queryReq 查询请求
     * @return {@link Object}
     */
    @Override
    public Object queryStringDsl(QueryReq queryReq) {
        SearchSourceBuilder builder = getSearchSourceBuilder();
        QueryStringBuilder queryStringBuilder = QueryStringBuilder.queryStringQuery(queryReq.getKw());
        if (StringUtils.isNotEmpty(queryReq.getAnalyzer())) {
            queryStringBuilder.analyzer(queryReq.getAnalyzer());
        }
        if (Objects.nonNull(queryReq.getField())) {
            queryStringBuilder.fields(queryReq.getFields());
        }
        SearchSourceBuilder query = builder.from(queryReq.getFrom())
                .size(queryReq.getSize())
                .query(queryStringBuilder);
        return query.toString();
    }

    /**
     * 聚合dsl
     *
     * @param aggReq agg请求
     * @return {@link Object}
     */
    @Override
    public Object aggDsl(List<AggReq> aggReq) {
        if (Objects.isNull(aggReq)) {
            throw new AggException(AggException.AGG_ERROR_MESSAGE);
        }
        SearchSourceBuilder builder =  getSearchSourceBuilder();
        builder.aggregation(AggregationBuilders.terms(aggReq.get(0).getCustomFields()).field(aggReq.get(0).getField()).size(aggReq.get(0).getSize()));
        aggReq
                .stream()
                .skip(0)
                .map(agg -> AggregationBuilders.terms(agg.getCustomFields()).field(agg.getField()).size(agg.getSize()))
                .forEach(agg->builder.aggregations().addAggregator(agg));
        return builder.toString();
    }

    /**
     * 高亮dsl
     *
     * @param highlightReqs 突出显示请求
     * @return {@link Object}
     */
    @Override
    public Object highlightDsl(List<HighlightReq> highlightReqs) {
        if (Objects.isNull(highlightReqs)) {
            throw new AggException(AggException.AGG_ERROR_MESSAGE);
        }
        SearchSourceBuilder builder =  getSearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightReqs.stream().map(highlight->new HighlightBuilder.Field(highlight.getField())).forEach(highlightBuilder::field);
        builder.highlighter(highlightBuilder);
        return builder.toString();
    }


    @Override
    public Object aggDsl(AggregationBuilder aggregationBuilder) {
        if (Objects.isNull(aggregationBuilder)) {
            throw new AggException(AggException.AGG_ERROR_MESSAGE);
        }
        SearchSourceBuilder builder =  getSearchSourceBuilder();
        builder.aggregation(aggregationBuilder);
        return builder.toString();
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
        log.info("*********************************DSL: {}", json);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        request.setEntity(entity);
        return esCoverHandler.cover(getResult(getResponse(request)));
    }


    /**
     * init ltr
     *
     * @return {@link Object}
     */
    @Override
    public Object initLtr() {
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat("_ltr"));
        return getResult(getResponse(request));
    }


}
