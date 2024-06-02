package cn.youyou.yygateway.chain;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * plugin链的接口,模拟spring filter链的设计方式
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
