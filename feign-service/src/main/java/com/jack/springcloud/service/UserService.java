package com.jack.springcloud.service;

import com.jack.springcloud.pojo.Result;
import com.jack.springcloud.pojo.User;
import com.jack.springcloud.service.impl.UserServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:添加UserService接口完成对user-service服务的接口绑定
 *             修改UserService接口，设置服务降级处理类为UserFallbackService
 *
 */
@FeignClient(value = "user-service", fallback = UserServiceImpl.class)
public interface UserService {

    @PostMapping("/user/insert")
    Result insert(@RequestBody User user);

    @GetMapping("/user/{id}")
    Result<User> getUser(@PathVariable String id);

    @GetMapping("/user/listUsersByIds")
    Result<List<User>> listUsersByIds(@RequestParam List<String> ids);

    @GetMapping("/user/getByUsername")
    Result<User> getByUsername(@RequestParam String username);

    @PostMapping("/user/update")
    Result update(@RequestBody User user);

    @PostMapping("/user/delete/{id}")
    Result delete(@PathVariable String id);

}
