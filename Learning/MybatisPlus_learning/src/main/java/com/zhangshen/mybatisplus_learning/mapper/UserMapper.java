package com.zhangshen.mybatisplus_learning.mapper;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhangshen.mybatisplus_learning.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);

    void updateByCustomSql(@Param(Constants.WRAPPER) LambdaUpdateWrapper<User> userLambdaUpdateWrapper, @Param("amount") int amount);


    @Update("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void dedutBalance(@Param("id") int userId, @Param("money") int money);
}
