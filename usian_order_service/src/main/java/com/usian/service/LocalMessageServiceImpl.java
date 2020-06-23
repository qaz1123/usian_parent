package com.usian.service;

import com.usian.mapper.LocalMessageMapper;
import com.usian.pojo.LocalMessage;
import com.usian.pojo.LocalMessageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocalMessageServiceImpl implements LocalMessageService {
    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Override
    public List<LocalMessage> selectlocalMessageByStatus(int status) {
        LocalMessageExample localMessageExample = new LocalMessageExample();
        LocalMessageExample.Criteria criteria = localMessageExample.createCriteria();
        criteria.andStateEqualTo(status);
        return localMessageMapper.selectByExample(localMessageExample);
    }
}
