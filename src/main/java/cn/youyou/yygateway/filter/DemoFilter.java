package cn.youyou.yygateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        log.info(" ===>>> filters: [demoFilter] ...");
        exchange.getRequest().getHeaders().toSingleValueMap().forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }
}
