package lq.simple.core;

/**
 * es操作
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsIndexOperate {
    /**
     *
     * 创建Index信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object createIndex(String index, String json);

    /**
     * 删除索引
     *
     * @param index 指数
     * @return {@link Object}
     */
    Object deleteIndex(String index);

    /**
     *
     * 获取Index信息
     * @param index 索引名称
     */
    Object getIndex(String index);
    /**
     *
     * 更新Mappings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object updateMapping(String index, String json);
    /**
     *
     * 更新Settings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object updateSetting(String index, String json);
}
