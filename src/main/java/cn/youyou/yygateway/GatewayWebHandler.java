package cn.youyou.yygateway;

import cn.youyou.yyrpc.core.api.LoadBalancer;
import cn.youyou.yyrpc.core.api.RegistryCenter;
import cn.youyou.yyrpc.core.cluster.RoundRibonLoadBalancer;
import cn.youyou.yyrpc.core.meta.InstanceMeta;
import cn.youyou.yyrpc.core.meta.ServiceMeta;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer<>();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println("===>>>> YY Gateway web handler ...");

        // 1. 通过请求路径获取服务名
        String service = exchange.getRequest().getPath().value().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .name(service).app("app1").env("dev").namespace("public")
                .build();
        // 2. 通过rc拿到所有活着的服务实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        // 3. 负载均衡，先简化处理，或者第一个实例url
        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        System.out.println(" inst size=" + instanceMetas.size() +  ", inst  " + instanceMeta);
        String url = instanceMeta.toUrl();

        // 4. 拿到请求的报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        // 5. 通过webclient发送post请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6. 通过entity获取响应报文
        Mono<String> resBody = entity.map(ResponseEntity::getBody);
        // 7. 组装响应报文
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("yy.gw.version", "v1.0.0");
        return resBody.flatMap(body -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))));
    }
}
