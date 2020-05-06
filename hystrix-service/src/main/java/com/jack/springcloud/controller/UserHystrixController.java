package com.jack.springcloud.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.jack.springcloud.pojo.Result;
import com.jack.springcloud.pojo.User;
import com.jack.springcloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/user")
public class UserHystrixController {

    @Autowired
    private UserService userService;

    /**
     * 基本调用
     * @param id
     * @return
     */
    @GetMapping("/testFallback/{id}")
    public Result testFallback(@PathVariable String id) {
        return userService.getUser(id);
    }

    /**
     * 异常处理
     * @param id
     * @return
     */
    @GetMapping("/testException/{id}")
    public Result testException(@PathVariable String id) {
        return userService.getUserException(id);
    }


    @GetMapping("/testCommand/{id}")
    public Result getUserCommand(@PathVariable String id) {
        return userService.getUserCommand(id);
    }

    /**
     * 缓存  如果被缓存，则直接使用缓存数据而不去请求服务提供者，
     * @param id
     * @return
     */
    @GetMapping("/testCache/{id}")
    public Result testCache(@PathVariable String id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return new Result("操作成功", 200);
    }

    /**
     * 移出缓存
     * @param id
     * @return
     */
    @GetMapping("/testRemoveCache/{id}")
    public Result testRemoveCache(@PathVariable String id) {
        userService.getUserCache(id);
        userService.removeCache(id);
        userService.getUserCache(id);
        return new Result("操作成功", 200);
    }

    @GetMapping("/testCollapser")
    public Result testCollapser() throws ExecutionException, InterruptedException {

        Future<User> future1 = userService.getUserFuture("1");
        Future<User> future2 = userService.getUserFuture("1");
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<User> future3 = userService.getUserFuture("3");
        future3.get();
        return new Result("操作成功", 200);
    }


}
