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
    public static <T> SerializedLambda resolveByField(SFunction<T, ?> func) {
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

    /**
     * 通过反序列化转换 lambda 表达式，该方法只能序列化 lambda 表达式，不能序列化接口实现或者正常非 lambda 写法的对象
     *
     * @param lambda lambda对象
     * @return 返回解析后的 SerializedLambda
     */
    public static SerializedLambda resolve(SFunction lambda) {
        if (!lambda.getClass().isSynthetic()) {
            throw new LamdaException("该方法仅能传入 lambda 表达式产生的合成类");
        }
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serialize(lambda))) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                Class<?> clazz = super.resolveClass(objectStreamClass);
                return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
            }
        }) {
            return (SerializedLambda) objIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new LamdaException("This is impossible to happen");
        }
    }

    /**
     * Serialize the given object to a byte array.
     * @param object the object to serialize
     * @return an array of bytes representing the object in a portable fashion
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
        return baos.toByteArray();
    }


}
