package lq.simple.core.index;

import lq.simple.core.cover.EsCoverHandler;
import lq.simple.core.dsl.AbstractEsDslHandler;
import lq.simple.exception.IndexException;
import lq.simple.util.JsonCheckUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public class EsIndexHandler extends AbstractEsIndexHandler {


    public EsIndexHandler(RestHighLevelClient client) {
        super.setClient(client);
        super.setEsCoverHandler(esCoverHandler);
    }

    @Override
    public Object createIndex(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.INDEX_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.createIndex(index, json).toString());
    }


    @Override
    public Object deleteIndex(String index) {
        return esCoverHandler.cover(super.deleteIndex(index).toString());
    }

    @Override
    public Object updateMapping(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.MAPPINGS_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.updateMapping(index, json).toString());
    }

    @Override
    public Object updateSetting(String index, String json) {
        if (JsonCheckUtils.checkJson(json)) {
            throw new IndexException(IndexException.SETTINGS_ERROR_MESSAGE);
        }
        return esCoverHandler.cover(super.updateSetting(index, json).toString());
    }

    @Override
    public Object getIndex(String index) {
        return esCoverHandler.cover(super.getIndex(index).toString());
    }

}
