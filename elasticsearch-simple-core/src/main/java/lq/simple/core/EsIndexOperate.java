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
     * 设置Index信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setIndex(String index, String json);

    /**
     *
     * 获取Index信息
     * @param index 索引名称
     */
    Object getIndex(String index);
    /**
     *
     * 设置Mappings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setMapping(String index, String json);
    /**
     *
     * 设置Settings信息
     * @param index 索引名称
     * @param json 索引配置信息
     */
    Object setSetting(String index, String json);
}
