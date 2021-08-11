package com.zyh.chat.controller;

import com.zyh.chat.entity.Result;
import com.zyh.chat.entity.StatusCode;
import com.zyh.chat.pojo.Users;
import com.zyh.chat.service.UserService;
import com.zyh.chat.service.UsersService;
import com.zyh.chat.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录管理
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private UserService userService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    /*
     * 注册之发送短信验证码
     * */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendSmsForRegister(@PathVariable String mobile) {
        Users hasUsers = usersService.findByPhonenumer(mobile);
        if (hasUsers != null) {
            return new Result(true, StatusCode.ERROR, "该手机号已经被注册了");
        }
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "发送验证码成功");
    }


    /*
     * 修改密码之发送短信验证码
     * */
    @RequestMapping(value = "/sendSmsForEditPassword/{mobile}", method = RequestMethod.GET)
    public Result sendSmsForEditPassword(@PathVariable String mobile) {
        Users hasUsers = usersService.findByPhonenumer(mobile);
        if (hasUsers != null) {
            userService.sendSms(mobile);
            return new Result(true, StatusCode.OK, "发证码成功");
        }
        return new Result(true, StatusCode.ERROR, "该手机号不存在,无法发送验证码");

    }

    /*
     *
     *用户注册*/
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result regist(@PathVariable String code, @RequestBody Users user) {
        //查一下,数据库里有没有这个账号,有的话不进行下面的,报重复错误交给前端处理
        Users hasUsers = usersService.findByPhonenumer(user.getPhonenumber());
        if (hasUsers != null) {
            return new Result(true, StatusCode.ERROR, "该手机号已经被注册了");
        }
        //得到缓存中的验证码
        String checkCode = (String) redisTemplate.opsForValue().get("checkCode_" + user.getPhonenumber());
        //如果缓存中没用该收集号的验证码
        if (checkCode == null || checkCode == "" || checkCode.isEmpty()) {
            return new Result(true, StatusCode.ERROR, "验证码验证失败或过期,请重新发送");
        }
        //如果有该验证码,但与用户传过来的验证码不匹配
        if (!checkCode.equals(code)) {
            return new Result(true, StatusCode.ERROR, "请输入正确验证码");
        }
        //如果没有以上错误和异常,在数据库中添加用户信息
        userService.add(user);
        return new Result(true, StatusCode.ERROR, "注册成功,请登录");
    }

    /*登录*/  //用户名或者手机号均可登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Users user) {

        Users user1 = userService.login(user);//查询到该用户
        if (user1 == null) {
            return new Result(true, StatusCode.ERROR, "登录失败");
        }
        //用户登录后生成令牌,返回给前端
        String token = jwtUtil.createJWT(user1.getUserid(), user1.getPhonenumber(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", "user");
        map.put("userid", user1.getUserid());
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

}
