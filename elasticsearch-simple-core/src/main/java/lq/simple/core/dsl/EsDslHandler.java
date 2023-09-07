package lq.simple.core.dsl;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public class EsDslHandler extends AbstractEsDslHandler {


    @Override
    protected Object complexAggDsl(AggregationBuilder aggregationBuilder) {
        SearchSourceBuilder builder =  getSearchSourceBuilder();
        builder.aggregation(aggregationBuilder);
        return builder.toString();
    }
}
