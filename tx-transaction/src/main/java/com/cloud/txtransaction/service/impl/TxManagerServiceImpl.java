package com.cloud.txtransaction.service.impl;

import com.cloud.txtransaction.client.TxManagerClient;
import com.cloud.txtransaction.dao.TxManagerMapper;
import com.cloud.txtransaction.entity.TxManager;
import com.cloud.txtransaction.service.TxManagerService;
import com.codingapi.tx.annotation.TxTransaction;
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
public class TxManagerServiceImpl implements TxManagerService {


    @Autowired
    private TxManagerClient txManagerClient;


    @Autowired
    private TxManagerMapper txManagerMapper;

    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);

    @Override
    public List<TxManager> list() {
        return txManagerMapper.findAll();
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public int save() {

        int rs1 = txManagerMapper.save("mybatis-hello-1");

        int rs2 = txManagerClient.save();

//        int rs22 = demo2Client.save();

        int v = 100 / 0;

        return rs1 + rs2;
    }
}
