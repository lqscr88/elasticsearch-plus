package lq.simple.core;

import lq.simple.bean.req.QueryReq;
import lq.simple.bean.resp.RestResp;
import lq.simple.result.SearchResult;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsOperate extends  EsDsl {

    /**
     *
     * 设置Index信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setIndex(String index, String json);

    /**
     *
     * 获取Index信息
     * @param index 索引名称
     */
    Object getIndex(String index);
    /**
     *
     * 设置Mappings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setMapping(String index, String json);
    /**
     *
     * 设置Settings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setSetting(String index, String json);
    /**
     *
     * match搜索
     * @param queryReq {@link QueryReq}   请求参数实体类
     *@return the {@link RestResp}
     */
    RestResp<SearchResult> match(QueryReq queryReq);


    /**
     * 匹配短语
     *
     * @param queryReq 查询请求
     * @return {@link RestResp}<{@link SearchResult}>
     */
    RestResp<SearchResult> matchPhrase(QueryReq queryReq);

    /**
     * 查询字符串
     *
     * @param queryReq 查询请求
     * @return {@link RestResp}<{@link SearchResult}>
     */
    RestResp<SearchResult> queryString(QueryReq queryReq);

    /**
     * 搜索
     *
     * @param index 指数
     * @param json  json
     * @return {@link RestResp}<{@link SearchResult}>
     */
    RestResp<SearchResult> search(String index, String json);
}
