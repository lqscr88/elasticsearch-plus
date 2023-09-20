package lq.simple.util;

import lq.simple.exception.LamdaException;
import lq.simple.plus.lamda.support.ColumnCache;
import lq.simple.plus.lamda.support.SFunction;
import lq.simple.plus.lamda.support.SerializedLambda;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LambdaUtils {

    private static final Map<String, Map<String, ColumnCache>> LAMBDA_CACHE = new ConcurrentHashMap<>();

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();


    /**
     * 解析 lambda 表达式
     *
     * @param func 需要解析的 lambda 对象
     * @param <T>  类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    public static <T> SerializedLambda resolve(SFunction<T, ?> func) {
        Class clazz = func.getClass();
        return Optional.ofNullable(FUNC_CACHE.get(clazz))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    SerializedLambda lambda = SerializedLambda.resolve(func);
                    FUNC_CACHE.put(clazz, new WeakReference<>(lambda));
                    return lambda;
                });
    }

    /**
     * 获取实体对应字段 MAP
     *
     * @param entityClassName 实体类名
     * @return 缓存 map
     */
    public static Map<String, ColumnCache> getColumnMap(String entityClassName) {
        return LAMBDA_CACHE.getOrDefault(entityClassName, Collections.emptyMap());
    }

    /**
     * 保存缓存信息
     *
     * @param className   类名
     * @param property    属性
     * @param columnCache 字段信息
     */
    private static void saveCache(String className, String property, ColumnCache columnCache) {
        Map<String, ColumnCache> cacheMap = LAMBDA_CACHE.getOrDefault(className, new HashMap<>());
        cacheMap.put(property, columnCache);
        LAMBDA_CACHE.put(className, cacheMap);
    }


}
