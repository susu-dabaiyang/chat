package com.zyh.chat.service;

import com.zyh.chat.dao.UsersDao;
import com.zyh.chat.pojo.Friend;
import com.zyh.chat.pojo.Record;
import com.zyh.chat.pojo.Users;
import com.zyh.chat.util.IdWorker;
import com.zyh.chat.util.JwtUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private RecordService recordService;

    @Autowired
    private FriendService friendService;
    /**
     * 增加(注册)
     * @param user
     */
    public void add(Users user) {
        user.setUserid( idWorker.nextId()+"" );
        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersDao.save(user);
        //让注册的用户与管理员成为好友
        Friend friend=new Friend();
        friend.setUserid(user.getUserid());
        friend.setFriends_id("1");
        friendService.add(friend);
        //并发送初始温馨提醒消息
        Record record=new Record();
        record.setUserid("1");
        record.setFriendid(user.getUserid());
        record.setMessage("您好,很高兴认识您,我是前途可期的开发者,这里我代表前途可期-青年择向平台欢迎您的加入!");
        recordService.add(record);
        record.setMessage("本产品可能设计方面还有一些不成熟的地方,如果有一些好建议可以联系我.");
        recordService.add(record);
        record.setMessage("使用方面,温馨提醒您:");
        recordService.add(record);
        record.setMessage("1.如果遇到不文明用户,请保留证据并举报.");
        recordService.add(record);
        record.setMessage("2.选择联系人时请双击.");
        recordService.add(record);
        record.setMessage("3.谨慎透露个人联系方式.");
        recordService.add(record);
        record.setMessage("本账户为前途可期目前唯一管理员账户,您可以直接通过此处向管理员留言,发表建议与投诉.");
        recordService.add(record);
    }


    public void sendSms(String mobile) {
        //生成六位数字随机数
        String checkCode = RandomStringUtils.randomNumeric(6);
        //向缓存中放一份
        redisTemplate.opsForValue().set("checkCode_"+mobile,checkCode,3, TimeUnit.MINUTES);
        //给用户发一份
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);//手机号
        map.put("checkCode",checkCode);//验证码
        rabbitTemplate.convertAndSend("sms",map);// 测试时候为了方便可以先不发到手机上
        //在控制台显示一份-___-仅仅测试用,实际工作开发去掉
        System.out.println("验证码为:  "+checkCode);
    }

    public Users login(Users user) {
        Users user1= usersDao.findByPhonenumber(user.getPhonenumber());
        Users user2= usersDao.findByUsername(user.getPhonenumber());
        if (user1!=null||user2!=null){
            if (user1!=null&&bCryptPasswordEncoder.matches(user.getPassword(),user1.getPassword())){
                return user1;
            }else if (user2!=null&&bCryptPasswordEncoder.matches(user.getPassword(),user2.getPassword())){
                return user2;
            }else {
                return  null;
            }
        }
        return  null;
    }
}
