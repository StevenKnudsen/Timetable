package com.nobes.timetable.test.service.impl;

import com.nobes.timetable.test.domain.Test;
import com.nobes.timetable.test.dao.TestMapper;
import com.nobes.timetable.test.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiangpen
 * @since 2023-02-21
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
