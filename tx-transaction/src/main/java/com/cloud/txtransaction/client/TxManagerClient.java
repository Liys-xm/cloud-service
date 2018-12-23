package com.cloud.txtransaction.client;

import com.cloud.txtransaction.entity.TxManager;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 *
 */
@FeignClient(value = "tx-transaction02")
public interface TxManagerClient {


    @GetMapping(value = "/tx-anon02/list")
    List<TxManager> list();


    @PostMapping(value = "/tx-anon02/save")
    int save();
}
