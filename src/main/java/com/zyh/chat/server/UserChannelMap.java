package com.zyh.chat.server;

import io.netty.channel.Channel;//neety通道别搞错了哦
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 建立用户与Channel之间的映射
 * @Param: 
 * @Return: 
 * @Author: Zyh
 * @Date: 2020/2/4 22:22
 */
public class UserChannelMap {
    //用户id和Channel之间的映射
    private static Map<String, Channel> userchannelMap;

    //静态代码块初始化映射表
    static {
        userchannelMap=new HashMap<String, Channel>();
    }
    
    /**
     * 功能描述: 添加用户id和channel之间的关联
     * @Param: [userid, channel]
     * @Return: void
     * @Author: Zyh
     * @Date: 2020/2/4 22:27
     */
    public  static void put(String userid,Channel channel){
        userchannelMap.put(userid,channel);
    }
    /**
     * 功能描述: 移除用户与通道之间的关联
     * @Param: [userid]
     * @Return: void
     * @Author: Zyh
     * @Date: 2020/2/4 22:28
     */
    public static void  remove(String userid){
        userchannelMap.remove(userid);
    }

    /**
     * 功能描述: 打印映射表---用户id和通道id的映射表
     * @Param: []
     * @Return: void
     * @Author: Zyh
     * @Date: 2020/2/4 22:31
     */
    public  static void printMap(){
        System.out.println("当前存活用户与通道的映射为:");
         for (String userid: userchannelMap.keySet()){
            System.out.println("用户id: "+userid+" 通道: "+userchannelMap.get(userid).id());
         }
    }

    /**
     * 功能描述: 根据channel id解除用户和通道之间的关联
     * @Param: [channelid]
     * @Return: void
     * @Author: Zyh
     * @Date: 2020/2/7 1:35
     */
    public static void removeByChannelId(String channelid){
        //预判断拦截空channel,防止后面nullexp
        if (!StringUtils.isNotBlank(channelid)){
            return;
        }

        for (String userid: userchannelMap.keySet()){
           if (channelid.equals(userchannelMap.get(userid).id().asLongText())){
               System.out.println("客户端连接断开,取消用户:"+userid+"与通道:"+userchannelMap.get(userid).id()+"之间的关联关系");
               userchannelMap.remove(userid);
               break;
           }
        }
    }


    /**
     * 功能描述: 根据id来获取通道Channel
     * @Param: [friendid]
     * @Return: io.netty.channel.Channel
     * @Author: Zyh
     * @Date: 2020/2/7 18:57
     */
    public static Channel getChannelById(String friendid) {
        Channel channel = userchannelMap.get(friendid);
        return channel;
    }
}
