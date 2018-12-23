package com.cloud.log.service.impl;

import com.codingapi.tx.netty.service.TxManagerHttpRequestService;
import com.lorne.core.framework.utils.http.HttpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * 分布式事物
 *
 * @author liyaoshi
 */
@Log4j2
@Service
public class TxManagerHttpRequestServiceImpl implements TxManagerHttpRequestService {

    @Override
    public String httpGet(String url) {
        log.info("httpGet-start");
        String res = HttpUtils.get(url);
        log.info("httpGet-end");
        return res;
    }

    @Override
    public String httpPost(String url, String params) {
        log.info("httpPost-start");
        String res = HttpUtils.post(url, params);
        log.info("httpPost-end");
        return res;
    }
}
