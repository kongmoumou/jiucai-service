package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.UserEntity;
import com.jiucai.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(Constant.userRouting.USER)
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping(Constant.userRouting.LOGIN)
    @ResponseBody
    public Object login(String username, String password, HttpSession session){
        UniformResponse<UserEntity> response = userService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Constant.SESSION_LOGIN_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户名检查
     * @param username
     * @return
     */
    @GetMapping(Constant.userRouting.CHECK)
    @ResponseBody
    public Object userCheck(String username){
        UniformResponse<UserEntity> response = userService.userCheck(username);
        return response;
    }

    /**
     * 用户注册
     * @param userEntity
     * @return
     */
    @PostMapping(Constant.userRouting.REGISTER)
    @ResponseBody
    public Object register(UserEntity userEntity){
        //todo 检查用户名是否合规
        UniformResponse<String> response = userService.register(userEntity);
        return response;
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping(Constant.userRouting.GET_USER_INFO)
    @ResponseBody
    public Object getUserInfo() {
        UniformResponse<UserEntity> response = userService.getUserInfo();
        return response;
    }

    /**
     * 更新用户资料
     * @param email
     * @param phone
     * @param question
     * @param answer
     * @return
     */
    @PostMapping(Constant.userRouting.UPDATE_USER_INFO)
    @ResponseBody
    public Object updateUserInfo(String email,String phone,String question,String answer) {
        UniformResponse<String> response = userService.updateUserInfo(email, phone, question, answer);
        return response;
    }


    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping(Constant.userRouting.RESET_PASSWORD)
    @ResponseBody
    public Object resetPassword(String oldPassword,String newPassword){
        //todo 检查新旧密码是否相同,新密码是否符合规则
        UniformResponse<String> response = userService.resetPassword(oldPassword, newPassword);
        return response;
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @GetMapping(Constant.userRouting.FORGET_PASSWORD)
    @ResponseBody
    public Object forgetPassword(String username){
        UniformResponse<String> response = userService.forgetPassword(username, "");
        return response;
    }

    /**
     * 验证答案，获取重置密码的Token
     *
     * @param username
     * @param answer
     * @return
     */
    @PostMapping(Constant.userRouting.FORGET_PASSWORD)
    @ResponseBody
    public Object forgetPasswordGetToken(@RequestParam(name = "username", required = true) String username,
                                         @RequestParam(name = "answer", required = false) String answer) {
        UniformResponse<String> response = userService.forgetPassword(username, answer);
        return response;
    }

    /**
     * 通过Token重置密码
     * @param username
     * @param newPassword
     * @param token
     * @return
     */
    @PostMapping(Constant.userRouting.RESET_PASSWORD_BY_TOKEN)
    @ResponseBody
    public Object resetPasswordByToken(String username, String newPassword, String token) {
        UniformResponse<String> response = userService.resetPasswordByToken(username, newPassword, token);
        return response;
    }

    @GetMapping(Constant.userRouting.LOGOUT)
    @ResponseBody
    public Object logout(){
        UniformResponse<String> response = userService.logout();
        return response;
    }

}
