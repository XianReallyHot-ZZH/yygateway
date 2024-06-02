package cn.youyou.yygateway.chain;

import cn.youyou.yygateway.plugin.GatewayPlugin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * chain的默认实现
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain {

    /**
     * 链路上排好序的所有插件
     */
    List<GatewayPlugin> plugins;

    /**
     * 调用指向的索引
     */
    int index = 0;

    public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
        this.plugins = plugins;
    }

    /**
     * chain的调用逻辑实现
     * @param exchange
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        // 插件的调用总是在这里完成，那么具体的插件在完成自己的handle逻辑后，只需要调用chain的handle方法即可，即能实现链上插件的流转调用
        return Mono.defer(() -> {
            if (index < plugins.size()) {
                // 还有插件,那么调用插件的handle，依赖插件进行链式调用
                return plugins.get(index++).handle(exchange, this);
            }
            // 插件调用完毕，那么返回空Mono
            return Mono.empty();
        });
    }
}
