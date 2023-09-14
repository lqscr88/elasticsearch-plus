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
import lq.simple.result.SearchResult;
import lq.simple.util.CharacterUtil;
import lq.simple.util.ResultUtil;
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
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object createIndex(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index));
        request.setEntity(entity);
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }

    /**
     * 删除索引
     *
     * @param index 指数
     * @return {@link Object}
     */
    @Override
    public Object deleteIndex(String index) {
        Request request = new Request(SearchHttpTypeEnum.DELETE.name(), CharacterUtil.SLASH.concat(index));
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
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
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }


    /**
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object updateMapping(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_mappings"));
        request.setEntity(entity);
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }


    /**
     * 设置Settings
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object updateSetting(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_settings"));
        request.setEntity(entity);
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
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
        return esCoverHandler.doCover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
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
        log.info("*********************************DSL: {}", json);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        request.setEntity(entity);
        return esCoverHandler.doCover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
    }

    @Override
    public RestResp<SearchResult> all(String index, Integer page, Integer size) {
        String json = "{\"from\":0,\"size\":1000,\"query\":{\"match_all\":{}}}";
        if (page != null && size != null) {
            json =  json.replace("0",page.toString()).replace("100",size.toString());
        }
        log.info("*********************************DSL: {}", json);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_search"));
        log.info("*********************************URl: {} {}", request.getMethod(), request.getEndpoint());
        request.setEntity(entity);
        return esCoverHandler.doCover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
    }

    @Override
    public Object save(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request;
        if (json.contains(ID)){
            JSONObject jsonObject =JSONObject.parseObject(json);
            String id = jsonObject.getString(ID);
            request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat(DOC).concat(CharacterUtil.SLASH).concat(id));
        }else {
            request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat(DOC));
        }
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
    }


    @Override
    public Object saveOrUpdateBatch(String index, List<String> json) {
        StringBuilder dsl = new StringBuilder();
        JSONArray objects = JSONArray.parseArray(json.toString());
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()){
            JSONObject next = (JSONObject) iterator.next();
            JSONObject indexSetting =new JSONObject();
            Map<String,Object> indexSettingParam = new HashMap<>();
            indexSettingParam.put(_INDEX,index);
            if (Objects.nonNull(next.getString(ID))){
                indexSettingParam.put(_ID,next.getString(ID));
                next.remove(ID);
            }
            indexSetting.put(INDEX,indexSettingParam);
            dsl.append(indexSetting.toJSONString()).append(System.getProperty(LINE_SEPARATOR) ).append(next.toJSONString()).append(System.getProperty(LINE_SEPARATOR));
        }
        HttpEntity entity = new NStringEntity(dsl.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtil.SLASH.concat(_BULK));
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
    }

    @Override
    public Object update(String index, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String id = jsonObject.getString(ID);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request  request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat(DOC).concat(CharacterUtil.SLASH).concat(id).concat(CharacterUtil.SLASH).concat(UPDATE));
        request.setEntity(entity);
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }

    @Override
    public Object detele(String index, String id) {
        Request  request = new Request(SearchHttpTypeEnum.DELETE.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat(DOC).concat(CharacterUtil.SLASH).concat(id));
        return ResultUtil.getResult(ResultUtil.getResponse(request, client));
    }

    @Override
    public Object deleteBatch(String index, List<String> ids) {
        StringBuilder dsl = new StringBuilder();
        ids.forEach(id->{
            JSONObject indexSetting =new JSONObject();
            Map<String,Object> indexSettingParam = new HashMap<>();
            indexSettingParam.put(_INDEX,index);
            indexSettingParam.put(_TYPE,DOC);
            indexSettingParam.put(_ID,id);
            indexSetting.put("delete",indexSettingParam);
            dsl.append(indexSetting.toJSONString()).append(System.getProperty(LINE_SEPARATOR) );

        });
        HttpEntity entity = new NStringEntity(dsl.toString(), ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.POST.name(), CharacterUtil.SLASH.concat(_BULK));
        request.setEntity(entity);
        return esCoverHandler.cover(ResultUtil.getResult(ResultUtil.getResponse(request, client)));
    }
}
