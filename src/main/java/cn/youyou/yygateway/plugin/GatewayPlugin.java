package cn.youyou.yygateway.plugin;

import cn.youyou.yygateway.chain.GatewayPluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 模仿shenyu，网关内使用plugin设计来扩展网关的功能
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    /**
     * 插件名字
     * @return
     */
    String getName();

    /**
     * 插件初始化启动类工作
     */
    void start();

    /**
     * 插件停止类工作
     */
    void stop();

    /**
     * 当前插件是否支持当前这笔请求
     * @param exchange
     * @return
     */
    boolean support(ServerWebExchange exchange);

    /**
     * 插件的处理逻辑，并完成对链的往后调用
     * @param exchange
     * @param chain
     * @return
     */
    Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);

}
