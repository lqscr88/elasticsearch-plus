package lq.simple.core;

import java.util.List;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsSaveOrUpdateOperate {

    Object save(String index, String json);

    Object saveById(String index, String json);


    Object saveBatch(String index, List<String> json);
    Object updateById(String index, String json);
    Object updateBatch(String index, List<String> json);

}
