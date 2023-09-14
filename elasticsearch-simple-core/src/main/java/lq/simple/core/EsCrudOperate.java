package lq.simple.core;

import java.util.List;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsCrudOperate {

    Object save(String index, String json);
    Object saveOrUpdateBatch(String index, List<String> json);
    Object update(String index, String json);
    Object detele(String index, String id);

    Object deleteBatch(String index, List<String> ids);

}
