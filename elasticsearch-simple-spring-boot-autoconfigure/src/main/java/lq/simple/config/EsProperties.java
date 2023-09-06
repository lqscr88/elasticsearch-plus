package lq.simple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述：es链接配置
 *
 * @Author lq
 * @Date 2023/8/24 22:08 14:33
 * @Version 1.0.0
 **/
@ConfigurationProperties(prefix = "es")
@Data
public class EsProperties {
    private String ip;
    private Integer port;
    private String root;
    private String password;
    private Boolean isOpenLtr;
}
