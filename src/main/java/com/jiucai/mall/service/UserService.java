package com.jiucai.mall.service;

import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.UserEntity;

public interface UserService {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    UniformResponse<UserEntity> login(String username, String password);

    /**
     * 用户名检查
     * @param username
     * @return
     */
    UniformResponse<UserEntity> userCheck(String username);

    /**
     * 用户注册
     * @param userEntity
     * @return
     */
    UniformResponse<String> register(UserEntity userEntity);

    /**
     * 获取用户信息
     *
     * @return
     */
    UniformResponse<UserEntity> getUserInfo();

    /**
     * 更新用户资料
     * @param email
     * @param phone
     * @param question
     * @param answer
     * @return
     */
    UniformResponse<String> updateUserInfo(String email, String phone, String question, String answer);

    /**
     * 重设密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    UniformResponse<String> resetPassword(String oldPassword, String newPassword);

    /**
     * 忘记密码选项
     *
     * @param username
     * @return
     */
    UniformResponse<String> forgetPassword(String username, String answer);

    /**
     * 通过token重置密码
     * @param username
     * @param newPassword
     * @param token
     * @return
     */
    UniformResponse<String> resetPasswordByToken(String username, String newPassword, String token);

    /**
     * 退出登录
     * @return
     */
    UniformResponse<String> logout();
}
