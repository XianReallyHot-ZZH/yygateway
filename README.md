# yygateway
[yygateway](https://github.com/XianReallyHot-ZZH/yygateway)是一个基于WebFlux构建的网关。

## 使用方式
主要是配合自己实现的其他中间件，使用网关将待请求的服务转发到对应的服务rpcProvider上，进而验证整套自己实现的分布式中间件功能。
* 启动yyregistry
* 启动yyrpc中的provider端
* 启动yygateway

## 当前进展

* 基于RouterFunction实现的网关请求转发
* 基于WebHandler和HandlerMapping实现的网关请求转发