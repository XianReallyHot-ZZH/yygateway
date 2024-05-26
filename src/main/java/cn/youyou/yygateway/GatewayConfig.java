package cn.youyou.yygateway;

import cn.youyou.yyrpc.core.api.RegistryCenter;
import cn.youyou.yyrpc.core.registry.yy.YyRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean(initMethod = "start")
    public RegistryCenter rc() {
        return new YyRegistryCenter();
    }

}
