package cn.youyou.yygateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 网关的入口
 */
@Component
public class GatewayEndPoint {

    @Autowired
    HelloHandler helloHandler;

    /**
     * 基于RouterFunction实现网关的入口
     * @return
     */
    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), helloHandler);
    }


}
