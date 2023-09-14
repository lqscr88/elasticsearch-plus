package lq.simple.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {


    /**
     *
     * @param list 需要分割的集合
     * @param divideSize 每个List中含几个元素
     * @return
     */
    public static <T> List<List<T>> divideArrays(List<T> list, int divideSize){
        Integer splitCount=(list.size()+divideSize-1)/divideSize;
        List<List<T>> collect = Stream.iterate(0, n -> n + 1)
                .limit(splitCount)
                .parallel()
                .map(a -> list.parallelStream().skip(a * divideSize).limit(divideSize).collect(Collectors.toList()))
                .filter(b -> !list.isEmpty())
                .collect(Collectors.toList());
        return collect;
    }
}
