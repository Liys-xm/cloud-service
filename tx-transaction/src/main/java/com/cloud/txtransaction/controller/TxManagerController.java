package com.cloud.txtransaction.controller;

import com.cloud.txtransaction.entity.TxManager;
import com.cloud.txtransaction.service.TxManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TxManagerController {

    @Autowired
    private TxManagerService txManagerService;


    @GetMapping("/list")
    @ResponseBody
    public List<TxManager> list() {
        return txManagerService.list();
    }


    @PostMapping("/save")
    @ResponseBody
    public int save() {
        return txManagerService.save();
    }
}
