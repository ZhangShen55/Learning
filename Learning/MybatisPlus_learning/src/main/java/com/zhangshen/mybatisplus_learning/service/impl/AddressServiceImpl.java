package com.zhangshen.mybatisplus_learning.service.impl;

import com.zhangshen.mybatisplus_learning.entity.Address;
import com.zhangshen.mybatisplus_learning.mapper.AddressMapper;
import com.zhangshen.mybatisplus_learning.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangshen
 * @since 2024-10-23
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
