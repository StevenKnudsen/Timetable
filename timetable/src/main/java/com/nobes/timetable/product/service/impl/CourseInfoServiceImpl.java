package com.nobes.timetable.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nobes.timetable.product.dao.CourseInfoMapper;
import com.nobes.timetable.product.domain.CourseInfo;
import com.nobes.timetable.product.service.ICourseInfoService;
import org.springframework.stereotype.Service;

@Service
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo>
        implements ICourseInfoService {

}
