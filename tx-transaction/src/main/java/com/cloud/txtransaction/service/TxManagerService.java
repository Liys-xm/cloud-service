package com.cloud.txtransaction.service;

import com.cloud.txtransaction.entity.TxManager;

import java.util.List;

public interface TxManagerService {

    List<TxManager> list();

    int save();

}
