# elasticsearch-simple

#### 介绍
elasticsearch-simple是一个专为elasticsearch定制的插件，可以进行简易的elasticsearch增删改查，DSL_QUERY语句的生成，LTR的使用
#### 软件架构
|                      模块                      |         介绍          |
| :--------------------------------------------: | :-------------------: |
|           elasticsearch-simple-core            | elasticsearch核心实现 |
|       elasticsearch-simple-dependencies        | dependencies统一管理  |
|    elasticsearch-simple-spring-boot-starter    |  springboot starter   |
| elasticsearch-simple-spring-boot-autoconfigure | springboot自动配置包  |

#### 安装教程

Maven
pom.xml的dependencies中加入以下内容。
最新版本：1.0.0
```
    <!--  单点登录spring-boot-starter依赖  -->
     <dependency>
         <groupId>lq.simple</groupId>
         <artifactId>elasticsearch-simple-spring-boot-starter</artifactId>
         <version>1.0.0</version>
     </dependency>
```

#### 配置说明
**修改服务application.properties配置，根据版本，视情况登录apollo 或者 nacos**   
es.ip=127.0.0.1  
es.port=9200  

带密码的情况：  
es.root=elastic  
es.password=123456  

#### 使用说明

```
 @Resource
 private EsOperate esOperate;
```

#### LTR使用说明

```
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
```



#### 参与贡献

1.  Fork 本仓库
2.  新建 master分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
