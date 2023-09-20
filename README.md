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
    <!--  spring-boot-starter依赖  -->
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

#### DSL相关使用方法：

```
EsDsl esDsl = esOperate.dslOps();
esDsl.aggDsl(AggReq);  //生成agg聚合DSL
esDsl.highlightDsl(HighlightReq); //生成highlight高亮DSL
esDsl.matchDsl(QueryReq); //生成match DSL
esDsl.matchPhraseDsl(QueryReq); //生成match_phrase DSL
esDsl.queryStringDsl(QueryReq); //生成query_string DSL
```

#### LTR相关使用方法：

```
EsLtr esLtr = esOperate.ltrOps();
esLtr.initLtr(); //初始化LTR插件
esLtr.createTemplate(templateName,param); //创建Template特征模板
esLtr.deleteTemplate(templateName); //删除Template特征模板
esLtr.createTemplateModel(modelName,param); //创建Template特征模板的模型model
esLtr.deleteTemplateModel(modelName,param); //删除Template特征模板的模型model
esLtr.useLtrDsl(params,modelName,templateNames) //简单使用LTR进行数据精排
```

#### PLUS相关使用方法：

##### 创建索引实体类：

```
@IndexName(value = "ltr_14") //索引名称
@IndexSettings(replicasNumber = 1,shardsNumber = 1) //settings配置，配置了三种模型（默认settings，去除特殊符号settings,去除单字settings），replicasNumber副本数量，shardsNumber分片数量，自定义settings写入value即可
@Data
public class LtrCs {
     // participleImplementType分词器选择，内置IK_MAX_WORD，IK_SMART,可自定义
     // participleType分词选择，ParticipleTypeEnum.TEXT仅分词，既分词又精确ParticipleTypeEnum.TEXT和ParticipleTypeEnum.KEYWORD一起配置
    @ParticipleType(participleImplementType = ParticipleImplementTypeEnum.IK_MAX_WORD,
            participleType = {ParticipleTypeEnum.TEXT,ParticipleTypeEnum.KEYWORD})
    private String title;
    private Integer number;
    private String abs; //无注解仅按类型进行创建字段
}
```

##### 使用实体类创建索引：

```
esOperate.indexOps().createIndex(LtrCs.class);
```

#### lamda：

##### 使用实体类进行搜索（）：

```
 RestResp<SearchResult> match = esOperate.match(EsWrappers.lambdaQuery(new LtrCs())
                .match(LtrCs::getTitle, "运动"));
```

#### 参与贡献

1.  Fork 本仓库
2.  新建 master分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
