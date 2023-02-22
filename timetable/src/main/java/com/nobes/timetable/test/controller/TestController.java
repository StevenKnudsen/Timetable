package com.nobes.timetable.test.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nobes.timetable.core.entity.ResultBody;
import com.nobes.timetable.test.domain.Test;
import com.nobes.timetable.test.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiangpen
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/test/test")
public class TestController {

    @Resource
    ITestService iTestService;

    @GetMapping("/test")
    public ResultBody test() throws Exception {
        Test one = iTestService.getOne(new LambdaQueryWrapper<Test>().eq(Test::getId, 1));
        return ResultBody.success(one);
    }

}
