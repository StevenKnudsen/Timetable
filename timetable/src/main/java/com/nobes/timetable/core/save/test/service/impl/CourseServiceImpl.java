package com.nobes.timetable.core.save.test.service.impl;

import com.nobes.timetable.core.save.test.domain.Course;
import com.nobes.timetable.core.save.test.dao.CourseMapper;
import com.nobes.timetable.core.save.test.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiangpen
 * @since 2023-03-03
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
