package com.zhangshen.mybatisplus_learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangshen.mybatisplus_learning.domain.po.User;
import com.zhangshen.mybatisplus_learning.domain.vo.UserVO;

import java.util.List;


public interface UserService extends IService<User> {
    void dedutBalance(int userId, int money);

    List<User> getUserByCondition(String name, Integer status, Integer maxBalance, Integer minBalance);

    UserVO getUserAndAddressById(int userId);

    List<UserVO> getUserAndAddressByIds(List<Long> ids);
}
