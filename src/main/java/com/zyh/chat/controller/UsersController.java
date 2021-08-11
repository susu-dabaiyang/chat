package com.zyh.chat.controller;

import com.zyh.chat.dao.MajorDao;
import com.zyh.chat.entity.PageResult;
import com.zyh.chat.entity.Result;
import com.zyh.chat.entity.StatusCode;
import com.zyh.chat.pojo.Major;
import com.zyh.chat.pojo.Users;
import com.zyh.chat.service.UsersService;
import com.zyh.chat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 后端管理平台
 * 控制器层
 *
 * @author zyh
 */
@RestController
@CrossOrigin
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MajorDao majorDao;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询全部数据成功", usersService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "根据id查询成功", usersService.findById(id));
    }

    @RequestMapping(value = "/search/{phonenumber}", method = RequestMethod.GET)
    public Result findByPhoneNumber(@PathVariable String phonenumber) {
        Users user = usersService.findByPhone(phonenumber);
        if (user != null) {
            return new Result(true, StatusCode.OK, "根据id查询成功", user);
        } else {
            return new Result(true, StatusCode.ERROR, "用户不存在");
        }
    }

    /**
     * 功能描述: 好友社交界面,点击搜索时候先查询该手机号或者账号下是否有用户
     *
     * @Param: [phonenumber]
     * @Return: com.zyh.chat.entity.Result
     * @Author: Zyh
     * @Date: 2020/2/15 16:44
     */
    @RequestMapping(value = "/searchCondition/{condition}", method = RequestMethod.GET)
    public Result findByCondition(@PathVariable String condition) {
        Users user = usersService.findByCondition(condition);
        if (user != null) {
            return new Result(true, StatusCode.OK, "根据用户名或者账号查询成功", user);
        } else {
            return new Result(true, StatusCode.ERROR, "用户不存在");
        }
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Users> pageList = usersService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "系统分页查询成功", new PageResult<Users>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 功能描述: <br>
     * 根据页码和页大小从数据库所有user里查出数据,交给usermanager管理
     *
     * @Param: 页码, 页大小
     * @Return: com.zyh.chat.entity.Result
     * @Author: Zyh
     * @Date: 2020/1/15 13:29
     */
    @RequestMapping(value = "/{page}/{size}", method = RequestMethod.POST)
    public Result findBypage(@PathVariable int page, @PathVariable int size) {
        Page<Users> pageList = usersService.findBypage(page, size);
        return new Result(true, StatusCode.OK, "分页查询成功", new PageResult<Users>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 功能描述: 根据专业查询用户
     *
     * @Param: [major_fk, page, size]
     * @Return: com.zyh.chat.entity.Result
     * @Author: Zyh
     * @Date: 2020/1/15 16:22
     */
    @RequestMapping(value = "/findUserByCondition/{major_fk}/{page}/{size}", method = RequestMethod.GET)
    public Result findUserByPage(@PathVariable String major_fk, @PathVariable int page, @PathVariable int size) {
        Users user = new Users();
        user.setMajor_fk(major_fk + "");
        System.out.println("要查询的专业id 为" + user.getMajor_fk());
        if (page < 1) {
            return new Result(true, StatusCode.OK, "页码不能为0以下");
        }
        Page<Users> pagedata = usersService.findUserByCondition(user, page, size);//这里传入的是一个user,因为后面可能会分装很多user的条件,比如方向
        return new Result(true, StatusCode.OK, "条件查询成功", new PageResult<Users>(pagedata.getTotalElements(), pagedata.getContent()));
    }

    /**
     * 功能描述: 连接内传入专业,post参数体传入users条件,我们这里通常转入的是direction,service封装了两种分页算法
     *
     * @Param: [users, major_fk, page, size]
     * @Return: com.zyh.chat.entity.Result  返回符合条件的总数目和当前的内容的content rows
     * @Author: Zyh
     * @Date: 2020/1/19 20:34
     */
    @RequestMapping(value = "/findUserByCondition/{major_fk}/{page}/{size}", method = RequestMethod.POST)
    public Result findUserByCondition(@RequestBody Users users, @PathVariable String major_fk, @PathVariable int page, @PathVariable int size) {
        Users user = new Users();
        user.setMajor_fk(major_fk);
        user.setDirection(users.getDirection());
        System.out.println("要查询的专业id 为" + user.getMajor_fk());
        if (page < 1) {
            return new Result(true, StatusCode.OK, "页码不能为0以下");
        }
        Page<Users> pagedata = usersService.findUserByCondition(user, page, size);//这里传入的是一个user,因为后面可能会分装很多user的条件,比如方向
        return new Result(true, StatusCode.OK, "条件查询成功", new PageResult<Users>(pagedata.getTotalElements(), pagedata.getContent()));
    }

    @RequestMapping(value = "/checkusername/{username}", method = RequestMethod.GET)
    public Result checkUsername(@PathVariable String username) {
        Users users = usersService.findByusername(username);
        if (users == null) {
            return new Result(true, StatusCode.OK, "");//如果用户唯一用户名可以使用则不对其修改
        } else {
            return new Result(true, StatusCode.ERROR, "该用户名已被注册,请重新输入");
        }
    }


    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", usersService.findSearch(searchMap));
    }

    /**
     * 根据条件查询----修改密码时候首先根据手机号查到用户信息
     *
     * @param phonenumber
     * @return
     */
    @RequestMapping(value = "/findByPhone/{phonenumber}", method = RequestMethod.GET)
    public Result findByPhone(@PathVariable String phonenumber) {
        Users user = usersService.findByPhone(phonenumber);
        if (user == null) {
            return new Result(true, StatusCode.ERROR, "查询失败", null);
        } else {
            return new Result(true, StatusCode.OK, "查询成功", usersService.findByPhone(phonenumber));
        }

    }

    /**
     * 修改密码
     *
     * @param users
     */
    @RequestMapping(value = "editPassword/{code}", method = RequestMethod.POST)
    public Result editPassword(@RequestBody Users users, @PathVariable String code) {
        //得到缓存中的验证码
        String checkCode = (String) redisTemplate.opsForValue().get("checkCode_" + users.getPhonenumber());
        //如果缓存中没用该收集号的验证码
        if (checkCode == null || checkCode == "" || checkCode.isEmpty()) {
            return new Result(true, StatusCode.ERROR, "验证码验证失败或过期,请重新发送");
        }
        //如果有该验证码,但与用户传过来的验证码不匹配
        if (!checkCode.equals(code)) {
            return new Result(true, StatusCode.ERROR, "请输入正确验证码");
        }
        //如果没有以上错误和异常
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        usersService.editPassword(users);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /**
     * 增加
     *
     * @param users
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Users users) {
        usersService.add(users);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改全部信息
     *
     * @param users
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Users users, @PathVariable String id) {
        System.out.println("要求修改后的用户信息为: " + users.toString());
        System.out.println("要求修改后的专业为:" + users.getMajor());
        if (users.getMajor() != null && !"".equals(users.getMajor())) {
            Major major = majorDao.findByMajorname(users.getMajor());
            users.setMajor_fk(major.getMajorid());
        }
        Users user = usersService.findById(id);
        users.setPicture(user.getPicture());
        users.setPassword(user.getPassword());//修改信息界面不准修改密码,需要需要密码需要去登录界面使用验证码修改
        usersService.update(users);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    //解析token
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public Result findUsersByToken(HttpServletRequest request) {
        String header = request.getHeader("token");
        System.out.println("得到的请求头为: " + header);
        Claims claims = jwtUtil.parseJWT(header);
        System.out.println("claims: " + claims.toString());
        String id = claims.getId();
        System.out.println("用户id为 :" + id);
        Users users = usersService.findById(id);
        return new Result(true, StatusCode.OK, "解析token成功", users);
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        usersService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
