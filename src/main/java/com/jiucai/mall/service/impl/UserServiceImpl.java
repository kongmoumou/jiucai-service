package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.entity.UserEntity;
import com.jiucai.mall.utils.JwtTokenUtil;
import com.jiucai.mall.utils.MD5Util;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import com.jiucai.mall.common.ResponseStatusCode;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.UserMapper;
import com.jiucai.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpSession session;

//    private EntitySetting entitySetting;

    /**
     * login
     * @param username
     * @param password
     * @return
     */
    @Override
    public UniformResponse<UserEntity> login(String username, String password){
        if (this.check(username) == ResponseStatusCode.FAIL.getCode()){
            String msg = Constant.userMsg.LOGIN.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
        /*MD5加密*/
        password = MD5Util.MD5Encrypt(password);
        UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>query().eq("username", username).eq("password", password.toLowerCase()));
        if (userEntity == null){
            String msg = Constant.userMsg.LOGIN.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }
        userEntity.setPassword(StringUtils.EMPTY);
        userEntity.setAnswer(StringUtils.EMPTY);
        String msg = Constant.userMsg.LOGIN.getSUCCESS();
        return UniformResponse.ResponseForSuccess(msg, userEntity);

    }

    /**
     * userCheck
     * @param username
     * @return
     */
    @Override
    public UniformResponse<UserEntity> userCheck(String username){
        if (check(username) == ResponseStatusCode.FAIL.getCode()){
            String msg = Constant.userMsg.CHECK.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        }
        String msg = Constant.userMsg.CHECK.getFAIL();
        return UniformResponse.ResponseForFail(msg);
    }

    private int check(String username){
        int row = userMapper.selectCount(Wrappers.<UserEntity>query().eq("username",username));
        if (row == 0){
            return ResponseStatusCode.FAIL.getCode();
        }
        else {
            return ResponseStatusCode.SUCCESS.getCode();
        }
    }

    /**
     * user register
     * @param userEntity
     * @return
     */
    @Override
    public UniformResponse<String> register(UserEntity userEntity){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userEntity.getUsername());
        int rows = userMapper.selectCount(queryWrapper);
        if (rows > 0) {
            return UniformResponse.ResponseForFail(Constant.userMsg.CHECK.getFAIL());
        }
        /*MD5加密*/
        userEntity.setPassword(MD5Util.MD5Encrypt(userEntity.getPassword()));
        userEntity.setRole(Constant.roleStatus.CUSTOMER);
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());
        rows = userMapper.insert(userEntity);
        if (rows == 0) {
            return UniformResponse.ResponseForError(Constant.userMsg.REGISTER.getFAIL());
        } else {
            return UniformResponse.ResponseForSuccess(Constant.userMsg.REGISTER.getSUCCESS());
        }

    }

    /**
     * get user information
     * @return
     */
    @Override
    public UniformResponse<UserEntity> getUserInfo() {
        UserEntity userEntity1 = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (userEntity1 == null) {
            return UniformResponse.ResponseForFail(Constant.userMsg.GetUserInfo.getFAIL());
        } else {
            UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>query().eq("username", userEntity1.getUsername()));
            userEntity.setPassword(StringUtils.EMPTY);
            userEntity.setAnswer(StringUtils.EMPTY);
            String msg = Constant.userMsg.GetUserInfo.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, userEntity);
        }
    }

    /**
     * update user information
     * @param email
     * @param phone
     * @param question
     * @param answer
     * @return
     */
    @Override
    public UniformResponse<String> updateUserInfo(String email,String phone,String question,String answer) {
        UserEntity userEntity1 = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (userEntity1 == null) {
            return UniformResponse.ResponseForFail(Constant.userMsg.GetUserInfo.getFAIL());
        } else {
            UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>query().eq("username", userEntity1.getUsername()));
            UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", userEntity.getId());
            String updateColumn = "";

            if (!email.isEmpty() && !userEntity.getEmail().equals(email)) {
                updateWrapper.set("email", email);
                updateColumn += "邮件、";
            }
            if (!phone.isEmpty() && !userEntity.getPhone().equals(phone)) {
                updateWrapper.set("phone", phone);
                updateColumn += "手机号码、";
            }
            if (!question.isEmpty() && !userEntity.getQuestion().equals(question)) {
                updateWrapper.set("question", question);
                updateColumn += "问题、";
            }
            if (!answer.isEmpty() && !userEntity.getAnswer().equals(answer)) {
                updateWrapper.set("answer", answer);
                updateColumn += "答案、";
            }
            if (updateColumn.isEmpty()) {
                String msg = Constant.userMsg.UpdateUserInfo.getFAIL();
                return UniformResponse.ResponseForFail(msg);
            } else {
                updateWrapper.set("update_time", LocalDateTime.now());
                userMapper.update(null, updateWrapper);
                String msg = updateColumn.substring(0,updateColumn.length()-1) + Constant.userMsg.UpdateUserInfo.getSUCCESS();
                return UniformResponse.ResponseForSuccess(msg);
            }

        }

    }

    /**
     * reset password
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public UniformResponse<String> resetPassword(String oldPassword,String newPassword) {
        /*MD5加密*/
        oldPassword = MD5Util.MD5Encrypt(oldPassword);
        newPassword = MD5Util.MD5Encrypt(newPassword);
        UserEntity userEntity1 = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (userEntity1 == null) {
            return UniformResponse.ResponseForFail(Constant.userMsg.GetUserInfo.getFAIL());
        } else {
            UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>query().eq("username", userEntity1.getUsername()));
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userEntity.getId());
            UserEntity user = userMapper.selectOne(queryWrapper);
            if (!user.getPassword().equals(oldPassword)) {
                String msg = Constant.userMsg.ResetPassword.getFAIL();
                return UniformResponse.ResponseForError(msg);
            } else {
                UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", userEntity.getId()).set("password", newPassword);
                userMapper.update(null, updateWrapper);
                String msg = Constant.userMsg.ResetPassword.getSUCCESS();
                return UniformResponse.ResponseForSuccess(msg);
            }
        }
    }

    /**
     * forget password
     * @param username
     * @param answer   当answer为空时，则返回问题；answer不为空时，则对answer进行验证
     * @return
     */
    @Override
    public UniformResponse<String> forgetPassword( String username, String answer) {
        if (check(username) == ResponseStatusCode.FAIL.getCode()) {
            String msg = Constant.userMsg.CHECK.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        }
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        Map<String, String> map = new HashMap<>();
        if (userEntity.getAnswer().isEmpty()) {
            /*用户未设置答案*/
            String msg = Constant.userMsg.ForgetPassword.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        } else if (answer.isEmpty()) {
            /*仅查询用户的答案*/
            map.put("username", username);
            map.put("question", userEntity.getQuestion());
            String msg = Constant.userMsg.ForgetPassword.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, map);
        } else {
            if (!userEntity.getAnswer().equals(answer)) {
                /*用户提交的答案为空*/
                String msg = Constant.userMsg.CheckAnswer.getFAIL();
                return UniformResponse.ResponseForFail(msg);
            }
            /*用户提交的答案不为空，则生成token*/
            String token = JwtTokenUtil.createJWT(username);
            map.put("username", username);
            map.put("token", token);
            String msg = Constant.userMsg.CheckAnswer.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg, map);
        }
    }

    /**
     * reset password by token
     * @param username
     * @param newPassword
     * @param token
     * @return
     */
    @Override
    public UniformResponse<String> resetPasswordByToken(String username, String newPassword, String token) {
        if (check(username) == ResponseStatusCode.FAIL.getCode()) {
            String msg = Constant.userMsg.CHECK.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        }
        Claims claims = JwtTokenUtil.parseJWT(token);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (now.after(claims.getExpiration())){
            String msg = Constant.userMsg.ResetPasswordByToken.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }else if(StringUtils.equals(username,claims.getSubject())){
            UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
            /*MD5加密*/
            newPassword = MD5Util.MD5Encrypt(newPassword);
            updateWrapper.eq("username",username).set("password",newPassword);
            userMapper.update(null,updateWrapper);
            String msg = Constant.userMsg.ResetPasswordByToken.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        }else {
            String msg = "重置密码失败，未请稍后重试！";
            return UniformResponse.ResponseForError(msg);
        }
    }

    /**
     * logoout
     * @return
     */
    @Override
    public UniformResponse<String> logout() {
        session.invalidate();
        UserEntity userEntity = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if (userEntity == null) {
            String msg = Constant.userMsg.LOGOUT.getSUCCESS();
            return UniformResponse.ResponseForSuccess(msg);
        } else {
            String msg = Constant.userMsg.LOGOUT.getFAIL();
            return UniformResponse.ResponseForFail(msg);
        }

    }
}
