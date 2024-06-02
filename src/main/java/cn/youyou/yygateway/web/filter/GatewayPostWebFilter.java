package cn.youyou.yygateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * webflux框架的filter实现
 */
@Component
public class GatewayPostWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(signalType -> {
            System.out.println("===>>> post filter");
            exchange.getAttributes().forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });
        });
    }
}
