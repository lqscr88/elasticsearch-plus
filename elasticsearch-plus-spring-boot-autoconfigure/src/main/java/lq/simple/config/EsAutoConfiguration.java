package lq.simple.config;

import lq.simple.core.EsOperate;
import lq.simple.handler.EsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsAutoConfiguration {

    @Autowired
    private EsProperties esProperties;


    @Bean
    public EsOperate esOperate() {
        EsHandler actuatorHandler=new EsHandler(esProperties.getIp(),esProperties.getPort(), esProperties.getRoot(), esProperties.getPassword());
        return actuatorHandler;
    }

}
