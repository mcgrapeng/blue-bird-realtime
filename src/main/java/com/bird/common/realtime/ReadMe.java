package com.bird.common.realtime;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/12 11:34
 */
public class ReadMe {

    /**

     FlowException 流量控制
     AuthorityException  黑名单控制
     SystemBlockException 系统自适应限流
     ParamFlowException 热点参数限流
     DegradeException  熔断降级


     以上均继承至BlockException





     1.在 Sentinel 的子项目 sentinel-spring-webmvc-adapter 中，对 SpringMVC 进行适配
     ，通过 SentinelWebInterceptor 拦截器，实现对 SpringMVC 的请求的拦截，使用 Sentinel 进行保护。
     通过 filter.url-patterns 配置项，可以定义该拦截器的拦截请求地址。

     2.在 SentinelWebInterceptor 拦截器中，当请求满足配置的 Sentinel block 的条件时，Sentinel 会抛出 BlockException 异常。
     通过定义 BlockExceptionHandler 接口的实现类，可以实现对 BlockException 的异常处理。




     通过 @ConfigurationProperties 或者 @Value + @RefreshScope 注解，已经能够满足我们绝大多数场景下的自动刷新配置的功能。
     但是，在一些场景下，我们仍然需要实现对配置的监听，执行自定义的逻辑。


     为了减少开发的复杂程度，Sentinel 对大部分的主流框架做了适配，例如 SpringMVC、WebFlux、Dubbo、Spring Cloud、RocketMQ 等等。
     我们只需要引入对应的 sentinel-apache-xxx-adapter 依赖，即可方便地整合 Sentinel。
     不过，Sentinel 并不能适配所有框架，此时我们可以使用 Sentinel 客户端 API，手动进行资源的保护。




     链路追踪：
     Agent ：负责从应用中，收集链路信息，发送给 SkyWalking OAP 服务器。
     SkyWalking Agent 收集 SkyWalking Tracing 数据，传递给服务器。

     SkyWalking OAP ：负责接收 Agent 发送的 Tracing 数据信息，然后进行分析(Analysis Core)
     ，存储到外部存储器( Storage )，最终提供查询( Query )功能。

     Storage ：Tracing 数据存储。目前支持 ES、MySQL、Sharding Sphere、TiDB、H2 多种存储器。
     我们采用mysql，主要是为了简单。
     市面上目前采用的是 ES ，主要考虑是 SkyWalking 开发团队自己的生产环境采用 ES 为主。

     SkyWalking UI ：负责提供控台，查看链路等等。


     主要指标:

     cpm 每分钟请求数  , cpm 全称 call per minutes，是吞吐量(Throughput)指标。 吞吐量CPM，表示每分钟的调用

     表示采集样本中某些值的占比，Skywalking 有 “p50、p75、p90、p95、p99”
     “p99:390” 表示 99% 请求的响应时间在390ms以内。


     应用的服务为 "is-travel-business"，这是在agent 环境变量 SW_AGENT_NAME 中所定义的。

     端点(Endpoint) ：对于特定服务所接收的请求路径, 如 HTTP 的 URI 路径和 gRPC 服务的类名 + 方法签名。

     服务实例(Service Instance) ：上述的一组工作负载中的每一个工作负载称为一个实例。

     1个服务--N个实例


     ApdexScore ： 性能指数，Apdex(Application Performance Index)是一个国际通用标准，Apdex 是用户对应用性能满意度的量化值。
     它提供了一个统一的测量和报告用户体验的方法，把最终用户的体验和应用性能作为一个完整的指标进行统一度量，其中最高为1最低为0；


     ResponseTime：响应时间，即在选定时间内，服务所有请求的平均响应时间(ms)；

     Throughput(CPM): 吞吐量，即在选定时间内，每分钟服务响应的请求量(cpm)


     服务慢端点 Service Slow Endpoint
     服务指标仪表盘会列举出当前服务响应时间最大的端点Top5，如果有端点的响应时间过高，则需要进一步关注其指标（点击可以复制端点名称）去"追踪"里搜索。


     运行中的实例 Running ServiceInstance
     该服务目前所有实例的吞吐量情况，通过此可以推断出实例之间的负载情况。如果发现某个实例吞吐量较低，
     就需要查询实例指标（如查询该实例是不是发生了GC，或则CPU利用率过高）



     */
}
