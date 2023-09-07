package lq.simple.core.ltr;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public class EsLtrHandler extends AbstractEsLtrHandler {

    public EsLtrHandler(RestHighLevelClient client){
        super.client = client;
    }
}
