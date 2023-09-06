package lq.simple.core;

import lq.simple.bean.req.AggReq;
import lq.simple.bean.req.HighlightReq;
import lq.simple.bean.req.QueryReq;
import lq.simple.bean.resp.RestResp;
import lq.simple.builder.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.List;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsDsl {

    /**
     *
     * matchDsl语句
     * @param queryReq {@link QueryReq}   请求参数实体类
     *@return the {@link RestResp}
     */
    Object matchDsl(QueryReq queryReq);

    /**
     * 匹配短语dsl
     *
     * @param queryReq 查询请求
     * @return {@link Object}
     */
    Object  matchPhraseDsl(QueryReq queryReq);

    /**
     * 查询字符串dsl
     *
     * @param queryReq 查询请求
     * @return {@link Object}
     */
    Object queryStringDsl(QueryReq queryReq);


    /**
     * 聚合dsl
     *
     * @param aggReq 查询请求
     * @return {@link Object}
     */
    Object aggDsl(List<AggReq> aggReq);


    /**
     * 更复杂的聚合dsl使用的是 {@link AggregationBuilders} 构造
     *
     * @param aggregationBuilder 聚合生成器
     * @return {@link Object}
     */
    Object aggDsl(AggregationBuilder aggregationBuilder);

    /**
     * 高亮dsl
     *
     * @param highlightReqs agg请求
     * @return {@link Object}
     */
    Object highlightDsl(List<HighlightReq> highlightReqs);
}
