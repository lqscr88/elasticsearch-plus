//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lq.simple.bean.resp;


import lombok.Builder;
import lombok.Data;

/**
 * agg实体类
 *
 * @author lqscr88
 * @date 2023/09/06
 */
@Data
@Builder
public class AggResp {
    private String field;
    /**
     * 自定义字段
     */
    private String customFields;
    @Builder.Default
    private Integer size = 10;
}
