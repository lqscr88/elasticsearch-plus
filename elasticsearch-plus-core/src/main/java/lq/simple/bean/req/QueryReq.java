//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lq.simple.bean.req;


import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class QueryReq {
    private String index;
    private String kw;
    private Map<String,Float> fields;
    private String analyzer;
    @Builder.Default
    private Integer from = 0;
    @Builder.Default
    private Integer size = 10;
    private String field;
    @Builder.Default
    private Float boost = 1.0F;
    private Integer slop;
}
