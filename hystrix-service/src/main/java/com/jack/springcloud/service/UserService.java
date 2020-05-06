package com.jack.springcloud.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.jack.springcloud.pojo.Result;
import com.jack.springcloud.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @HystrixCommand(fallbackMethod = "fallbackMethod1")
    public Result getUser(String id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Result.class, id);
    }

    /**
     * 失败返回
     *
     * @param id
     * @return
     */
    public Result fallbackMethod1(@PathVariable String id) {
        return new Result("服务调用失败", 500);
    }

    /**
     * 失败返回
     * @param id
     * @param e
     * @return
     */
    public Result fallbackMethod2(@PathVariable String id, Throwable e) {
        LOGGER.error("id {},throwable class:{}", id, e.getClass());
        return new Result("服务调用失败", 500);
    }

    /**
     * 忽略异常测试
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallbackMethod2", ignoreExceptions = {NullPointerException.class})
    public Result getUserException(String id) {
        if ("1".equals(id)) {
            throw new IndexOutOfBoundsException();
        } else if ("2".equals(id)) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Result.class, id);
    }

    /**
     * 基础参数设置
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallbackMethod1", commandKey = "getUserCommand", groupKey = "getUserGroup", threadPoolKey = "getUserThreadPool")
    public Result getUserCommand(String id) {
        //fallbackMethod：指定服务降级处理方法；
        //ignoreExceptions：忽略某些异常，不发生服务降级；
        //commandKey：命令名称，用于区分不同的命令；
        //groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
        //threadPoolKey：线程池名称，用于划分线程池。
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Result.class, id);
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "fallbackMethod1", commandKey = "getUserCache")
    public Result getUserCache(String id) {
        LOGGER.info("getUserCache id:{}", id);
//       @CacheResult：开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key；
//       @CacheKey：指定缓存的key，可以指定参数或指定参数中的属性值为缓存key，cacheKeyMethod还可以通过返回String类型的方法指定；
//       @CacheRemove：移除缓存，需要指定commandKey。
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Result.class, id);
    }

    /**
     * 为缓存生成key的方法
     *
     * @return
     */
    public String getCacheKey(String id) {
        return String.valueOf(id);
    }

    @HystrixCommand
    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    public Result removeCache(String id) {
        LOGGER.info("removeCache id:{}", id);
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, Result.class, id);
    }

    @HystrixCollapser(batchMethod = "listUsersByIds",collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds",value = "100")
    })
    public Future<User> getUserFuture(String id) {
//        batchMethod：用于设置请求合并的方法；
//        collapserProperties：请求合并属性，用于控制实例属性，有很多；
//        timerDelayInMilliseconds：collapserProperties中的属性，用于控制每隔多少时间合并一次请求；
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                Result result = restTemplate.getForObject(userServiceUrl + "/user/{1}", Result.class, id);
                Map data = (Map) result.getData();
                User user = BeanUtil.mapToBean(data, User.class, true);
                LOGGER.info("getUserById username:{}",user.getUserName());
                return user;
            }
        };
    }

    /**
     * 请求合并方法
     * @param ids
     * @return
     */
    @HystrixCommand
    public List<User> listUsersByIds(List<String> ids) {
        Result result = restTemplate.getForObject(userServiceUrl + "/user/listUsersByIds?ids={1}", Result.class, CollUtil.join(ids, ","));
        return (List<User>)result.getData();
    }


}
