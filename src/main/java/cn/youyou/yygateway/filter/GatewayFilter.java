package cn.youyou.yygateway.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义的网关过滤器，不是webFlux的那套
 */
public interface GatewayFilter {

    Mono<Void> filter(ServerWebExchange exchange);

}
