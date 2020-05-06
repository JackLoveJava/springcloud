package com.jack.springcloud.controller;

import com.jack.springcloud.pojo.Result;
import com.jack.springcloud.pojo.User;
import com.jack.springcloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @PostMapping("/insert")
    public Result insert(@RequestBody User user) {
        return userServiceImpl.insert(user);
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable String id) {
        return userServiceImpl.getUser(id);
    }

    @GetMapping("/listUsersByIds")
    public Result<List<User>> listUsersByIds(@RequestParam List<String> ids) {
        return userServiceImpl.listUsersByIds(ids);
    }

    @GetMapping("/getByUsername")
    public Result<User> getByUsername(@RequestParam String username) {
        return userServiceImpl.getByUsername(username);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userServiceImpl.update(user);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        return userServiceImpl.delete(id);
    }

}
