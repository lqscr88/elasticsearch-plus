package lq.simple.core.ltr;


import java.util.List;
import java.util.Map;

/**
 * es ltr
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public interface EsLtr{


    /**
     * Ltr详情： https://elasticsearch-learning-to-rank.readthedocs.io/en/latest/index.html
     * ltr插件下载地址：http://es-learn-to-rank.labs.o19s.com
     * init ltr
     * 首先得给elasticsearch安装LTR插件
     *
     * @return {@link Object}
     */
    Object initLtr();

    /**
     * Ltr详情： https://elasticsearch-learning-to-rank.readthedocs.io/en/latest/index.html
     * 创建模板
     * 请求参数param例子：
     * name：自定义名称
     * params：template内的参数
     * template：dsl语句
     *          {
     *                 "name": "abs_query_match_phrase",
     *                 "params": [
     *                     "kw"
     *                 ],
     *                 "template": {
     *                     "match_phrase": {
     *                         "abs": {
     *                             "query":"{{kw}}",
     *                             "analyzer": "ik_smart"
     *                         }
     *                     }
     *                 }
     *             }
     *
     * @param templateName 模板名称
     * @param param 请求参数
     * @return {@link Object}
     */
    Object createTemplate(String templateName, List<Object> param);


    /**
     * 删除模板
     *
     * @param templateName 模板名称
     * @return {@link Object}
     */
    Object deleteTemplate(String templateName);

    /**
     * 更新模板
     *
     * @param templateName 模板名称
     * @param param        参数
     * @return {@link Object}
     */
    Object updateTemplate(String templateName, List<Object> param);

    Object getTemplate(String templateName);


    /**
     * Ltr详情： https://elasticsearch-learning-to-rank.readthedocs.io/en/latest/index.html
     * 创建模型
     * 请求参数param例子：
     * name：自定义名称
     * model：模型详情
     *   type：model/linear 常用线性模型
     *   definition：模型构造参数，abs_query_match_phrase和template的name一致，5.0为权重
     * {
     *     "model": {
     *         "name": "abs-model",
     *         "model": {
     *             "type": "model/linear",
     *             "definition": {
     *                 "abs_query_match_phrase": 5.0
     *             }
     *         }
     *     }
     * }
     *
     * @param modelName 模型名称
     * @param modelParam 模型参数
     * @return {@link Object}
     */
    Object createTemplateModel(String modelName,Object modelParam);


    Object deleteTemplateModel(String modelName);

    Object getTemplateModel(String modelName);



    /**
     * ps :次方法只生成建议的使用语句较为局限，复杂语句可以自定义实现
     * Ltr详情： https://elasticsearch-learning-to-rank.readthedocs.io/en/latest/index.html
     * 使用ltr Dsl语句生成
     * 例子：
     * {
     *     "query": {
     *         "match": {
     *             "abs": "缓震运动鞋"
     *         }
     *     },
     *     "rescore": {
     *         "window_size": 1000,
     *         "query": {
     *             "rescore_query": {
     *                 "sltr": {
     *                     "params": {
     *                         "kw": "缓震运动鞋"
     *                     },
     *                     "model": "cs-1",
     *                     "active_features": [
     *                         "abs_query_match_phrase"
     *                     ]
     *                 }
     *             }
     *         }
     *     }
     * }
     * @param params sltr_params参数
     * @param modelName 使用的模型名称
     * @param templateNames 使用的特征模板名称
     * @return {@link Object}
     */
    Object useLtrDsl(Map<String,String> params,String modelName,List<String> templateNames);

}
