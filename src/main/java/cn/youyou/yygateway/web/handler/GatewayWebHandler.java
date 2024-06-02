package cn.youyou.yygateway.web.handler;

import cn.youyou.yygateway.chain.DefaultGatewayPluginChain;
import cn.youyou.yygateway.plugin.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基于WebHandler机制实现的网关接入设计
 */
@Slf4j
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        log.info("===>>>> YY Gateway web handler ...");

        if (plugins == null || plugins.isEmpty()) {
            String mock = """
                    {"result": "no plugin"}
                    """;
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        // 插件链调用
        return new DefaultGatewayPluginChain(plugins).handle(exchange);

    }
}
