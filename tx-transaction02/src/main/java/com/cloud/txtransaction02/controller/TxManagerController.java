package com.cloud.txtransaction02.controller;

import com.cloud.txtransaction02.entity.TxManager;
import com.cloud.txtransaction02.service.TxManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TxManagerController {

    @Autowired
    private TxManagerService txManagerService;


    @GetMapping("/tx-anon02/list")
    @ResponseBody
    public List<TxManager> list() {
        return txManagerService.list();
    }


    @PostMapping("/tx-anon02/save")
    @ResponseBody
    public int save() {
        return txManagerService.save();
    }
}
