package lq.simple.core.index;

import com.alibaba.fastjson.JSONObject;
import lq.simple.core.cover.EsCoverHandler;
import lq.simple.enums.SearchHttpTypeEnum;
import lq.simple.exception.IndexException;
import lq.simple.plus.annotation.IndexName;
import lq.simple.plus.annotation.IndexSettings;
import lq.simple.plus.annotation.ParticipleType;
import lq.simple.plus.enums.ParticipleTypeEnum;
import lq.simple.util.CharacterUtil;
import lq.simple.util.ResultUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * es抽象处理程序
 *
 * @author lqscr88
 * @date 2023/08/31
 */
public abstract class AbstractEsIndexHandler implements EsIndexOperate {

    protected EsCoverHandler esCoverHandler;

    protected RestHighLevelClient client;


    public void setEsCoverHandler(EsCoverHandler esCoverHandler) {
        this.esCoverHandler = esCoverHandler;
    }

    public void setClient(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object createIndex(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    @Override
    public Object createIndex(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(IndexName.class)) {
            throw new IndexException(IndexException.INDEX_NAME_NULL_ERROR_MESSAGE);
        }
        IndexName annotation = clazz.getAnnotation(IndexName.class);
        //索引名称
        String indexName = annotation.value();
        JSONObject index = new JSONObject();
        //创建settings结构
        if (!clazz.isAnnotationPresent(IndexSettings.class)) {
            IndexSettings indexSettings= clazz.getAnnotation(IndexSettings.class);
            String settingsJson = indexSettings.value()
                    .replace("${shards}", String.valueOf(indexSettings.shardsNumber()))
                    .replace("${replicas}", String.valueOf(indexSettings.replicasNumber()));
            JSONObject settings = JSONObject.parseObject(settingsJson);
            index.put("settings",settings);
        }
        //创建mappings结构
        JSONObject properties = new JSONObject();
        JSONObject fieldsParam = new JSONObject();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取属性的名称、类型和修饰符等信息
            String name = field.getName();
            ParticipleType participleType = field.getAnnotation(ParticipleType.class);
            if (Objects.nonNull(participleType)) {
                JSONObject param = new JSONObject();
                //处理分词类型
                if (participleType.participleType().length != 0) {
                    //即分词又精确
                    if (participleType.participleType().length == 2) {
                        for (ParticipleTypeEnum participleTypeEnum : participleType.participleType()) {
                            if (Objects.equals(ParticipleTypeEnum.TEXT.name(), participleTypeEnum.name())) {
                                param.put("type", participleTypeEnum.name().toLowerCase());
                            } else {
                                JSONObject propertiesFields = new JSONObject();
                                JSONObject keyword = new JSONObject();
                                keyword.put("type", participleTypeEnum.name().toLowerCase());
                                propertiesFields.put(participleTypeEnum.name().toLowerCase(), keyword);
                                param.put("fields", propertiesFields);
                            }
                        }
                    } else {
                        for (ParticipleTypeEnum participleTypeEnum : participleType.participleType()) {
                            if (Objects.equals(ParticipleTypeEnum.TEXT.name(), participleTypeEnum.name())) {
                                param.put("type", participleTypeEnum.name().toLowerCase());
                            }
                        }
                    }

                }
                //处理分词类型
                param.put("analyzer", participleType.participleImplementType().name().toLowerCase());
                fieldsParam.put(name, param);
            } else {
                //没有设置分词器和分词类型按默认类型创建属性
                JSONObject fieldParam = new JSONObject();
                if (field.getGenericType().toString().equals("int") || field.getGenericType().toString().equals("class java.lang.Integer")) {
                    fieldParam.put("type", "integer");
                } else if (field.getGenericType().toString().equals("class java.lang.String")) {
                    fieldParam.put("type", "keyword");
                } else if (field.getGenericType().toString().equals("long") || field.getGenericType().toString().equals("class java.lang.Long")) {
                    fieldParam.put("type", "long");
                }else if (field.getGenericType().toString().equals("class java.util.Date")){
                    fieldParam.put("type", "date");
                    fieldParam.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis||strict_date_optional_time");
                }
                fieldsParam.put(name, fieldParam);
            }
        }
        properties.put("properties",fieldsParam);
        index.put("mappings", properties);
        System.out.println(index.toJSONString());
        return createIndex(indexName,index.toJSONString());
    }

    /**
     * 删除索引
     *
     * @param index 指数
     * @return {@link Object}
     */
    @Override
    public Object deleteIndex(String index) {
        Request request = new Request(SearchHttpTypeEnum.DELETE.name(), CharacterUtil.SLASH.concat(index));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }

    /**
     * 得到索引
     *
     * @param index 索引
     * @return {@link Object}
     */
    @Override
    public Object getIndex(String index) {
        Request request = new Request(SearchHttpTypeEnum.GET.name(), CharacterUtil.SLASH.concat(index));
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }


    /**
     * 设置索引
     *
     * @param index 索引
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object updateMapping(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_mappings"));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }


    /**
     * 设置Settings
     *
     * @param index 指数
     * @param json  json
     * @return {@link Object}
     */
    @Override
    public Object updateSetting(String index, String json) {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Request request = new Request(SearchHttpTypeEnum.PUT.name(), CharacterUtil.SLASH.concat(index).concat(CharacterUtil.SLASH).concat("_settings"));
        request.setEntity(entity);
        return ResultUtils.getResult(ResultUtils.getResponse(request, client));
    }
}
