package com.nobes.timetable.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nobes.timetable.product.dao.ProgramIdMapper;
import com.nobes.timetable.product.domain.ProgramId;
import com.nobes.timetable.product.service.IProgramIdService;
import org.springframework.stereotype.Service;

@Service
public class ProgramIdServiceImpl extends ServiceImpl<ProgramIdMapper, ProgramId>
        implements IProgramIdService {

}
