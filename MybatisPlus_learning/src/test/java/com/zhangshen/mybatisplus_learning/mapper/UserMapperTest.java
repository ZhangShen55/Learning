package com.zhangshen.mybatisplus_learning.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.zhangshen.mybatisplus_learning.service.UserService;
import com.zhangshen.mybatisplus_learning.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(50L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.saveUser(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.queryUserById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {

        List<Long> array = new ArrayList<>();
        array.add(1L);
        array.add(2L);
        array.add(3L);
        array.add(4L);
        List<User> users = userMapper.queryUserByIds(array);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateUser(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteUser(5L);
    }

    @Test
    void testUpdateUserBalanceByid() {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        //update set in
        userUpdateWrapper.setSql("balance= balance - 200").in("id",3L);
        userMapper.update(null,userUpdateWrapper);
    }

    @Test
    void testUpdateByCustomSql() {
        //什么样的情况需要自定义Sql语句：where之外的条件无法满足时
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.in(User::getId,3L);
        userMapper.updateByCustomSql(userLambdaUpdateWrapper,1000);
    }

    @Test
    void testGetAllUser(){
        LambdaQueryChainWrapper<User> in = userService.lambdaQuery().in(User::getId, 1L);
    }
}