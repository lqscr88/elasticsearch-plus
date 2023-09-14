package lq.simple.plus.service.impl;

import com.alibaba.fastjson.JSONObject;
import lq.simple.core.EsOperate;
import lq.simple.exception.IndexException;
import lq.simple.handler.AbstractEsHandler;
import lq.simple.handler.EsHandler;
import lq.simple.plus.annotation.IndexName;
import lq.simple.plus.annotation.ParticipleType;
import lq.simple.plus.enums.ParticipleTypeEnum;
import lq.simple.plus.service.PlusService;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

public class PlusServiceImpl implements PlusService {


    @Override
    public Object index(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(IndexName.class)) {
            throw new IndexException(IndexException.INDEX_NAME_NULL_ERROR_MESSAGE);
        }
        IndexName annotation = clazz.getAnnotation(IndexName.class);
        //索引名称
        String indexName = annotation.value();
        //创建mappings结构
        JSONObject mappings = new JSONObject();
        JSONObject properties = new JSONObject();
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
                param.put("analyzer", participleType.participleImplementType());
                properties.put(name, param);
            } else {
                //没有设置分词器和分词类型按默认类型创建属性
                JSONObject fieldParam = new JSONObject();
                if (field.getGenericType().toString().equals("int") || field.getGenericType().toString().equals("class java.lang.Integer")) {
                    System.out.println("Integer type");
                    fieldParam.put("type", "integer");
                } else if (field.getGenericType().toString().equals("class java.lang.String")) {
                    fieldParam.put("type", "keyword");
                } else if (field.getGenericType().toString().equals("long") || field.getGenericType().toString().equals("class java.lang.Long")) {
                    fieldParam.put("type", "long");
                }else if (field.getGenericType().toString().equals("class java.util.Date")){
                    fieldParam.put("type", "date");
                    fieldParam.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis||strict_date_optional_time");
                }
                properties.put(name, fieldParam);
            }
        }
        mappings.put("mappings", properties);

        return null;
    }
}
