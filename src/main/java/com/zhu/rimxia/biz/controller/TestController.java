package com.zhu.rimxia.biz.controller;

import com.zhu.rimxia.biz.common.MyWebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "测试")
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    private MyWebSocket myWebSocket;
    @ApiOperation(value = "测试")
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name){
        return name;
    }

   // public void commont(int id,)

}
