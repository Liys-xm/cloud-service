package com.cloud.notification.service.impl;

import com.codingapi.tx.config.service.TxManagerTxUrlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * 分布式事物
 *
 * @author liyaoshi
 */
@Log4j2
@Service
public class TxManagerTxUrlServiceImpl implements TxManagerTxUrlService {


    @Value("${tm.manager.url}")
    private String url;

    @Override
    public String getTxUrl() {
        log.info("load tm.manager.url：{}", url);
        return url;
    }
}
