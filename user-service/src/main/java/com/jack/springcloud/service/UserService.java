package com.jack.springcloud.service;

import com.jack.springcloud.pojo.User;

import java.util.List;

public interface UserService {
    void insert(User user);

    User getUser(String id);

    void update(User user);

    void delete(String  id);

    User getByUsername(String username);

    List<User> listUsersByIds(List<String> ids);
}
