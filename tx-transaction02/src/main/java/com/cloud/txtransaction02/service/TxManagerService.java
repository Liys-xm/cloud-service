package com.cloud.txtransaction02.service;

import com.cloud.txtransaction02.entity.TxManager;

import java.util.List;

public interface TxManagerService {

    List<TxManager> list();

    int save();

}
