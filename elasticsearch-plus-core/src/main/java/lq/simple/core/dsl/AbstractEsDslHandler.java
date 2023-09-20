package lq.simple.core.dsl;

import lq.simple.bean.req.AggReq;
import lq.simple.bean.req.HighlightReq;
import lq.simple.bean.req.QueryReq;
import lq.simple.builder.AggregationBuilders;
import lq.simple.builder.MatchQueryBuilder;
import lq.simple.builder.QueryStringBuilder;
import lq.simple.exception.AggException;
import lq.simple.util.StringUtils;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import java.util.List;
import java.util.Objects;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public abstract class AbstractEsDslHandler implements EsDsl {


    /**
     * 得到搜索来源工厂
     *
     * @return {@link SearchSourceBuilder}
     */
    protected SearchSourceBuilder getSearchSourceBuilder() {
        return new SearchSourceBuilder();
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

    protected abstract Object complexAggDsl(AggregationBuilder aggregationBuilder);

    @Override
    public Object aggDsl(AggregationBuilder aggregationBuilder) {
        if (Objects.isNull(aggregationBuilder)) {
            throw new AggException(AggException.AGG_ERROR_MESSAGE);
        }
        return complexAggDsl(aggregationBuilder);
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


}
