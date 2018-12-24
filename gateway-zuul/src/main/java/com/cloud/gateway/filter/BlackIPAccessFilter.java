package com.cloud.gateway.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cloud.gateway.feign.BackendClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul 的过滤器
 * <p>
 * 黑名单IP拦截<br>
 * 黑名单ip变化不会太频繁，<br>
 * 考虑到性能，我们不实时掉接口从别的服务获取了，<br>
 * 而是定时把黑名单ip列表同步到网关层,
 *
 * @author liyaoshi
 */
@Component
public class BlackIPAccessFilter extends ZuulFilter {

    /**
     * 黑名单列表
     */
    private Set<String> blackIPs = new HashSet<>();

    /**
     * 是否执行该过滤器。
     * true：说明需要过滤；
     * false：说明不要过滤；
     */
    @Override
    public boolean shouldFilter() {
        if (blackIPs.isEmpty()) {
            return false;
        }

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String ip = getIpAddress(request);

        return blackIPs.contains(ip);// 判断ip是否在黑名单列表里
    }

    /**
     * 过滤器的具体逻辑
     */
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        requestContext.setResponseBody("black ip");
        requestContext.setSendZuulResponse(false);
        return null;
    }

    /**
     * 过滤的优先级，数字越大，优先级越低。
     */
    @Override
    public int filterOrder() {
        return 0;
    }


    /**
     * 前置过滤器。
     * 但是在 zuul 中定义了四种不同生命周期的过滤器类型：
     * 1、pre：请求在路由之前被调用,如:身份验证；
     * 2、route：请求在路由时被调用；
     * 3、post：路由到微服务之后执行(在route和error过滤器之后被调用)；
     * 4、error：处理请求发生错误时被调用；
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Autowired
    private BackendClient backendClient;

    /**
     * 定时同步黑名单IP
     */
    @Scheduled(cron = "${cron.black-ip}")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void syncBlackIPList() {
        try {
            Set<String> list = backendClient.findAllBlackIPs(Collections.emptyMap());
            blackIPs = list;
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * 获取请求的真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
