package com.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 网关系统
 * <p>
 * Zuul 的过滤器 ZuulFilter 的使用。
 * 注意 EnableZuulProxy 注解能注册到 eureka 服务上，是因为该注解包含了 eureka 客户端的注解，该
 * EnableZuulProxy 是一个复合注解。
 *
 * @author liyaoshi
 * @EnableZuulProxy --> { @EnableCircuitBreaker、@EnableDiscoveryClient } 包含了 eureka 客
 * 户端注解，同时也包含了 Hystrix 断路器模块注解。
 * http://localhost:8150/routes 地址可以查看该zuul微服务网关代理了多少微服务的serviceId。
 */
@EnableFeignClients
@EnableZuulProxy
//@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("【【【【【【 GatewayZuulFilter微服务 】】】】】】已启动.");
    }

}