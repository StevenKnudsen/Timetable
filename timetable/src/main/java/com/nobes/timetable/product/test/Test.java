package com.nobes.timetable.product.test;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.product.domain.CourseInfo;
import com.nobes.timetable.product.service.ICourseInfoService;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class Test {

    @Resource
    ICourseInfoService i;


    public List<CourseInfo> test() {

        List<CourseInfo> Lecs = i.list(new LambdaQueryWrapper<CourseInfo>()
                .eq(CourseInfo::getCatalog, "243")
                .eq(CourseInfo::getSubject, "CH E")
                .eq(CourseInfo::getComponent, "LEC"));

        return Lecs;
    }
}
