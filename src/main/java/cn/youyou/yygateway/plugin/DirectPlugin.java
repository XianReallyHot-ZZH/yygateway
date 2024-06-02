package cn.youyou.yygateway.plugin;

import cn.youyou.yygateway.chain.GatewayPluginChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.MediaSize;

/**
 * DirectPlugin：根据路径参数实现直接转发请求至相应的后端
 */
@Slf4j
@Component("directPlugin")
public class DirectPlugin extends AbstractGatewayPlugin{

    public static final String NAME = "direct";

    // 网关请求的路径前缀
    private String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
        log.info("======>>>>>> [YYRpc-Plugin] ...");

        // 根据输入的请求参数，获取后端服务地址
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");

        // 获取输入的请求报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("yy.gw.version", "v1.0.0");
        exchange.getResponse().getHeaders().add("yy.gw.plugin", getName());

        // 如果没有配置后端服务地址，那么就将body的内容原样返回，然后调用插件handle方法继续调用后续的插件
        if (backend == null || backend.isEmpty()) {
            return requestBody.flatMap(dataBuffer -> exchange.getResponse().writeWith(Mono.just(dataBuffer)))
                    .then(chain.handle(exchange));
        }

        // 配置了后端服务地址，那么就转发请求至后端服务，然后调用插件handle方法继续调用后续的插件
        WebClient client = WebClient.create(backend);
        // TODO: 其实这一块有点问题，需要进一步优化：转发的请求有可能是POST，GET，PUT，DELETE等，当前是不支持除了POST外的请求类型的，这一块是需要设计的，要实现根据原始请求，解析后，再进行特定类型的转发
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        Mono<String> body = entity.map(ResponseEntity::getBody);
        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
