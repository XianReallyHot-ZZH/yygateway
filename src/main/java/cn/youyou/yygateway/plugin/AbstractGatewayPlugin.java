package cn.youyou.yygateway.plugin;

import cn.youyou.yygateway.chain.GatewayPluginChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * plugin的抽象层，包装常见的，公共的使用逻辑
 */
@Slf4j
public abstract class AbstractGatewayPlugin implements GatewayPlugin {


    @Override
    public void start() {
        // no-op
    }

    @Override
    public void stop() {
        // no-op
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    /**
     * 完成对handle处理的公共逻辑包装
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain) {
        boolean supported = support(exchange);
        log.info(" =====>>>> plugin[{}], support={}", this.getName(), supported);
        // 如果支持处理，那么就调用具体处理逻辑然后在doHandle的最后调用chain往后处理，否则的话就直接调用chain的处理方法往后调用链上的插件了
        return supported ? doHandle(exchange, chain) : chain.handle(exchange);
    }

    /**
     * 判断当前插件是否支持当前这笔请求的具体逻辑
     * @param exchange
     * @return
     */
    public abstract boolean doSupport(ServerWebExchange exchange);

    /**
     * 插件处理请求的具体逻辑
     * @param exchange
     * @param chain
     * @return
     */
    public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain);
}
