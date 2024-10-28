package com.zhangshen.mybatisplus_learning.service;

import com.zhangshen.mybatisplus_learning.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserService userService;


    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(20L);
        user.setUsername("haha");
        user.setPassword("1233");
        user.setPhone("18688990012");
        user.setBalance(200);
        user.setInfo("{\"age\": 34, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }


    @Test
    void tesGetUser() {

        List<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        longs.add(3L);
        List<User> users = userService.listByIds(longs);
        for (User user : users) {
            System.out.println(user);
        }


    }


    private User buildUer(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (12312341234L + i));
        user.setInfo("{\"age\":24}");
        user.setBalance(5000);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testSaveOneByOne() {
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUer(i));
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }
    @Test
    void testSaveOneByBatch() {

        List<User> userList = new ArrayList<>(1000);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userList.add(buildUer(i));
            if (i % 1000 == 0) {
                userService.saveBatch(userList);
                userList.clear();//清空
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }
}