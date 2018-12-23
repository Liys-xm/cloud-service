package com.cloud.txtransaction02.service.impl;

import com.cloud.txtransaction02.dao.TxManagerMapper;
import com.cloud.txtransaction02.entity.TxManager;
import com.cloud.txtransaction02.service.TxManagerService;
import com.codingapi.tx.annotation.ITxTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class TxManagerServiceImpl implements TxManagerService, ITxTransaction {


    @Autowired
    private TxManagerMapper txManagerMapper;

    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);

    @Override
    public List<TxManager> list() {
        return txManagerMapper.findAll();
    }

    @Override
    @Transactional
    public int save() {
        int rs1 = txManagerMapper.save("mybatis-hello-2");
        return  rs1;
    }
}
