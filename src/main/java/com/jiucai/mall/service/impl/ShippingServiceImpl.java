package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.ResponseStatusCode;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.ShippingMapper;
import com.jiucai.mall.entity.ShippingEntity;
import com.jiucai.mall.entity.UserEntity;
import com.jiucai.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class ShippingServiceImpl extends ServiceImpl<ShippingMapper, ShippingEntity> implements ShippingService {
    @Autowired
    private HttpSession session;
    @Autowired
    ShippingMapper shippingMapper;

    /**
     * 获取所有邮寄地址信息
     *
     * @return
     */
    @Override
    public UniformResponse<List<ShippingEntity>> getShippingList() {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (user == null) {
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(), ResponseStatusCode.NEED_LOGIN.getDescription());
        }else{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id",user.getId());
            List list = shippingMapper.selectList(queryWrapper);
            return UniformResponse.ResponseForSuccess(list);
        }

    }

    /**
     * 更新邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    @Override
    public UniformResponse<String> updateShipping(ShippingEntity shippingEntity) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (user == null) {
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(), ResponseStatusCode.NEED_LOGIN.getDescription());
        }else{
            shippingEntity.setUserId(user.getId());
            int rows = shippingMapper.updateById(shippingEntity);
            if(rows>0){
                return UniformResponse.ResponseForSuccess("修改收货地址成功!");
            }else{
                return UniformResponse.ResponseForSuccess("修改收货地址失败!");
            }
        }
    }

    /**
     * 创建邮寄地址信息
     *
     * @param shippingEntity
     * @return
     */
    @Override
    public UniformResponse<String> createShipping(ShippingEntity shippingEntity) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (user == null) {
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(), ResponseStatusCode.NEED_LOGIN.getDescription());
        }else{
            shippingEntity.setUserId(user.getId());
            int rows = shippingMapper.insert(shippingEntity);
            if (rows > 0) {
                return UniformResponse.ResponseForSuccess("添加收货地址成功!");
            } else {
                return UniformResponse.ResponseForSuccess("添加收货地址失败!");
            }
        }
    }

    /**
     * 删除某一邮寄地址信息
     *
     * @param shippingId
     * @return
     */
    @Override
    public UniformResponse<String> deleteShipping(Integer shippingId){
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (user == null) {
            return UniformResponse.ResponseErrorCodeMessage(ResponseStatusCode.NEED_LOGIN.getCode(), ResponseStatusCode.NEED_LOGIN.getDescription());
        }else{
            int rows = shippingMapper.deleteById(shippingId);
            if(rows > 0){
                return UniformResponse.ResponseForSuccess("删除收货地址成功!");
            }else {
                return UniformResponse.ResponseForSuccess("删除收货地址失败!");
            }
        }
    }

}
