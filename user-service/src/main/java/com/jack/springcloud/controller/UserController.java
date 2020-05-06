package com.jack.springcloud.controller;
import com.jack.springcloud.pojo.Result;
import com.jack.springcloud.pojo.User;
import com.jack.springcloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/insert")
    public Result insert(@RequestBody User user) {
        userService.insert(user);
        return new Result("操作成功", 200);
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable String id) {
        System.out.println("访问咯");
        User user = userService.getUser(id);
        return new Result<User>(user);
    }

    @GetMapping("/listUsersByIds")
    public Result<List<User>> listUsersByIds(@RequestParam List<String> ids) {
        List<User> userList= userService.listUsersByIds(ids);
        LOGGER.info("根据ids获取用户信息，用户列表为：{}",userList);
        return new Result<List<User>>(userList);
    }

    @GetMapping("/getByUsername")
    public Result<User> getByUsername(@RequestParam String username) {
        User user = userService.getByUsername(username);
        return new Result<User>(user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return new Result("操作成功", 200);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        userService.delete(id);
        return new Result("操作成功", 200);
    }

}
