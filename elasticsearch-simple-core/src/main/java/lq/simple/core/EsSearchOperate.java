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
public interface EsSearchOperate {
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
