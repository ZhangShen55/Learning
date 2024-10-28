package com.zhangshen.mybatisplus_learning.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.zhangshen.mybatisplus_learning.domain.po.User;
import com.zhangshen.mybatisplus_learning.domain.vo.AddressVO;
import com.zhangshen.mybatisplus_learning.domain.vo.UserVO;
import com.zhangshen.mybatisplus_learning.entity.Address;
import com.zhangshen.mybatisplus_learning.mapper.UserMapper;
import com.zhangshen.mybatisplus_learning.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void dedutBalance(int userId, int money) {
        User user = getById(userId);

        if(user == null || user.getStatus() == 2){
            throw new RuntimeException("用户不存在或状态异常！");
        }
        if(user.getBalance() < money){
            throw new RuntimeException("用户余额不足！");
        }
        int resetBalance = user.getBalance() - money;
        //状态 ;balance == 0 状态冻结
        lambdaUpdate().set(User::getBalance,resetBalance)
                .set(resetBalance == 0,User::getStatus,2)
                        .eq(User::getId,userId).eq(User::getBalance,user.getBalance())//乐观锁
                        .update();

//        baseMapper.dedutBalance(userId,money); 用过Mapper定义SQL语句

    }

    @Override
    public List<User> getUserByCondition(String name, Integer status, Integer maxBalance, Integer minBalance) {
        return lambdaQuery().like(name!=null,User::getUsername,name)
                .eq(status!=2,User::getStatus,status)
                .gt(User::getBalance,minBalance)
                .lt(User::getBalance,maxBalance)
                .list();
    }

    @Override
    public UserVO getUserAndAddressById(int userId) {

        User user = getById(userId);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }

        //转user的po为vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //查询用户地址 DB静态方法
        List<Address> addressList = Db.lambdaQuery(Address.class).eq(Address::getUserId, userId).list();

        if (BeanUtil.isNotEmpty(addressList)){
            List<AddressVO> addressVOS = BeanUtil.copyToList(addressList, AddressVO.class);
            userVO.setAddresses(addressVOS);
        }
        return userVO;
    }

    @Override
    public List<UserVO> getUserAndAddressByIds(List<Long> ids) {
        //拿到所有用户
        List<User> users = listByIds(ids);
        if(BeanUtil.isEmpty(users)){
            //users空 返回空集合
            return Collections.emptyList();
        }

        List<Address> addressList = Db.lambdaQuery(Address.class).in(Address::getUserId, ids).list();
        List<AddressVO> addressVOS = BeanUtil.copyToList(addressList, AddressVO.class);
        //判空

        Map<Long, List<AddressVO>> addressVOMap = new HashMap<>(0);
        if(BeanUtil.isNotEmpty(addressList)){
            addressVOMap = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        List<UserVO> userVOS = new ArrayList<>(users.size());

        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setAddresses(addressVOMap.get(userVO.getId()));
            userVOS.add(userVO);
        }
        return userVOS;
    }
}
