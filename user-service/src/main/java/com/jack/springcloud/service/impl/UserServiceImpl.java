package com.jack.springcloud.service.impl;

import com.jack.springcloud.pojo.User;
import com.jack.springcloud.service.UserService;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private List<User> userList;

    @PostConstruct
    public void initData() {
        userList = new ArrayList<>();
        userList.add(new User("1", "Jack", "123456"));
        userList.add(new User("2", "Andy", "123456"));
        userList.add(new User("3", "Mark", "123456"));
    }

    @Override
    public void insert(User user) {
        userList.add(user);
    }

    @Override
    public User getUser(String id) {
        for(User user : userList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void update(User user) {
        userList.stream().filter(userItem -> userItem.getId().equals(user.getId())).forEach(userItem -> {
            userItem.setUserName(user.getUserName());
            userItem.setPassword(user.getPassword());
        });
    }

    @Override
    public void delete(String id) {
        User user = getUser(id);
        if (user != null) {
            userList.remove(user);
        }
    }

    @Override
    public User getByUsername(String userName) {
        List<User> list = userList.stream().filter(userItem -> userItem.getUserName().equals(userName)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<User> listUsersByIds(List<String> ids) {
       List<User> returnList=new ArrayList<>();
       for (String id :ids){
           for(User user:userList){
               if(user.getId().equals(id)){
                   returnList.add(user);
               }
           }
       }
       return returnList;
    }


}
