package lq.simple.core;

import lq.simple.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsCrudOperate {

    /**
     * 保存数据
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    Object save(String index, String json);

    /**
     * 批量保存或更新数据
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    Object saveOrUpdateBatch(String index, List<String> json);

    /**
     * 按批次保存或更新数据
     *
     * @param index 指数
     * @param json  json
     * @param szie  大小
     * @return {@link Object}
     */
    default Object saveOrUpdateBatch(String index, List<String> json,Integer szie){
        List<Object> result = new ArrayList<>();
        ListUtil.divideArrays(json,szie).forEach(data-> result.add(saveOrUpdateBatch(index,data)));
        return result;
    }

    /**
     * 更新数据
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    Object update(String index, String json);

    /**
     * 删除数据
     *
     * @param index 指数
     * @param id    id
     * @return {@link Object}
     */
    Object detele(String index, String id);

    /**
     * 批量删除数据
     *
     * @param index 指数
     * @param ids   ids
     * @return {@link Object}
     */
    Object deleteBatch(String index, List<String> ids);

    /**
     * 按批次删除数据
     *
     * @param index 指数
     * @param ids   ids
     * @param szie  大小
     * @return {@link Object}
     */
    default Object deleteBatch(String index, List<String> ids,Integer szie){
        List<Object> result = new ArrayList<>();
        ListUtil.divideArrays(ids,szie).forEach(data-> result.add(deleteBatch(index,data)));
        return result;
    }

}
