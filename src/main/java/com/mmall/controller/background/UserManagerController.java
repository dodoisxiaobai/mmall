package com.mmall.controller.background;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author dodo
 * @date 2018/4/8
 * @description
 */
@Controller
@RequestMapping("/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("/login")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> login = iUserService.login(username, password);
        if (login.isSuccess()) {
            User user = login.getData();
            if (user == null) {
                return ServerResponse.createByErrorMsg("用户不存在");
            }
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return login;
            }

            return ServerResponse.createByErrorMsg("非管理员");
        }
        return login;
    }

}

