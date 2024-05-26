package cn.youyou.yygateway;

import cn.youyou.yyrpc.core.api.RegistryCenter;
import cn.youyou.yyrpc.core.registry.yy.YyRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

@Configuration
public class GatewayConfig {

    @Bean(initMethod = "start")
    public RegistryCenter rc() {
        return new YyRegistryCenter();
    }

    /**
     * 将自己实现的webHandler配置号对应的路径映射关系，并注册到spring容器中使之起效
     * @param context
     * @return
     */
    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            // handlerMapping负责将请求路径映射到对应的handler上
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            // 增加映射关系
            Properties mappings = new Properties();
            mappings.put("/gw/**", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("yyrpc gateway start");
        };
    }

}
