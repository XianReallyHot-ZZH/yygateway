package cn.youyou.yygateway.web.handlerfunction;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 基于HandlerFunction机制实现的简单demo
 */
@Component
public class HelloHandler implements HandlerFunction<ServerResponse> {

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {

        String url = "http://localhost:8081/yyrpc";
        // 文本块
        String requestJson = """
                {
                    "service":"cn.youyou.yyrpc.demo.api.UserService",
                    "methodSign":"findById@1_int",
                    "args":[100]
                }
                """;

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJson)
                .retrieve()
                .toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.subscribe(source -> System.out.println("response:" + source));
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("yy.gw.version", "v1.0.0")
                .body(body, String.class);
    }

}
