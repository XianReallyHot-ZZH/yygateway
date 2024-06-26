package cn.youyou.yygateway.web.handlerfunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 基于HandlerFunction+RouterFunction机制实现的网关的入口
 */
@Component
public class GatewayEndPoint {

//    @Autowired
//    HelloHandler helloHandler;
//
//    @Autowired
//    GatewayHandler gatewayHandler;
//
//    /**
//     * 基于RouterFunction实现网关的入口
//     * @return
//     */
//    @Bean
//    public RouterFunction<?> helloRouterFunction() {
//        return route(GET("/hello"), helloHandler);
//    }
//
//    @Bean
//    public RouterFunction<?> gatewayRouterFunction() {
//        return route(GET("/gw").or(POST("/gw/**")), gatewayHandler);
//    }


}
