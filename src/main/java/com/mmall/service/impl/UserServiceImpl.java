package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author dodo
 * @date 2018/4/3
 * @description
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //检查用户名是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        String md5password = MD5Util.MD5EncodeUtf8(username);

        //检查密码是否正确
        User user = userMapper.checkLogin(username, md5password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户密码错误");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = checkValid(user.getUsername(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        //md5加密
        String md5Password = MD5Util.MD5EncodeUtf8(user.getPassword());

        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(md5Password);
        int insert = userMapper.insert(user);
        if (insert == 0) {
            return ServerResponse.createByErrorMsg("注册失败！");
        }
        return ServerResponse.createBySuccessMsg("注册成功！");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {

        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int checkCount = userMapper.checkUsername(str);
                if (checkCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int checkCount = userMapper.checkEmail(str);
                if (checkCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMsg("校验失败");
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        //校验用户名是否存在
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String quetion = userMapper.selectQuetionByUsername(username);
        if (StringUtils.isNotBlank(quetion)) {
            return ServerResponse.createBySuccess("查找成功", quetion);
        }

        return ServerResponse.createByErrorMsg("找回密码问题为空");
    }

    @Override
    public ServerResponse<String> selectAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {

            String forgenToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgenToken);
            return ServerResponse.createBySuccess(forgenToken);
        }

        return ServerResponse.createByErrorMsg("问题答案不正确");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {

        //校验token
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("token不存在");
        }
        ServerResponse<String> responseValid = this.checkValid(username, Const.USERNAME);
        if (responseValid.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("token错误");
        }

        if (StringUtils.equals(forgetToken, token)) {
            //修改密码
            //MD5
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultRow = userMapper.forgetPasswordByUserName(username, md5Password);
            if (resultRow > 0) {
                return ServerResponse.createBySuccessMsg("修改密码成功");
            } else {
                return ServerResponse.createByErrorMsg("token有误，请重置token");
            }
        }

        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {

        int countResult = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (countResult == 0) {
            return ServerResponse.createByErrorMsg("用户密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccessMsg("修改密码成功");
        }

        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    @Override
    public ServerResponse<User> getInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
