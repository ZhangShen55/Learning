package com.zhangshen.mybatisplus_learning.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangshen.mybatisplus_learning.domain.dto.UserFormDTO;
import com.zhangshen.mybatisplus_learning.domain.po.User;
import com.zhangshen.mybatisplus_learning.domain.query.UserQuery;
import com.zhangshen.mybatisplus_learning.domain.vo.UserVO;
import com.zhangshen.mybatisplus_learning.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理控制层")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/saveUser")
    @ApiOperation("添加用户接口")
    private boolean saveUser(@RequestBody UserFormDTO userFormDTO){

        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        return userService.save(user);

    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户接口")
    private boolean deleteUserById(@ApiParam("用户id") @PathVariable("id")int userId){
        return userService.removeById(userId);
    }

    @GetMapping({"/{id}"})
    @ApiOperation("获取单个用户接口")
    private UserVO getUserById(@ApiParam("用户id") @PathVariable("id")int userId){
        return userService.getUserAndAddressById(userId);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户列表接口")
    private List<UserVO> getUserByIds(@ApiParam("用户id集合")@RequestParam List<Long> ids){
        return userService.getUserAndAddressByIds(ids);
    }

    @PutMapping("/{id}/deduction/{money}")
    @ApiOperation("增减薪接口")
    private void deductionUserBalanceByid(@PathVariable("id")int userId,@PathVariable("money")int money){
        User user = userService.getById(userId);
        Integer balance = user.getBalance();
        user.setBalance(balance - money);

        userService.dedutBalance(userId,money);
    }

    @GetMapping()
    @ApiOperation("复杂条件查询Us")
    private List<UserVO> getUserByCondition(@ApiParam("用户查询包装类")UserQuery userQuery){
        List<User> users = userService.getUserByCondition(userQuery.getName(),userQuery.getStatus(),userQuery.getMaxBalance(),userQuery.getMinBalance());
        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);
        return userVOS;
    }
}
