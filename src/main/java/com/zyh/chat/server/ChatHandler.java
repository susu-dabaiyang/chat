package com.zyh.chat.server;

import com.alibaba.fastjson.JSON;
import com.zyh.chat.pojo.Record;
import com.zyh.chat.service.RecordService;
import com.zyh.chat.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.SimpleDateFormat;


//extends SimpleChannelInboundHandler<TextWebSocketFrame> 使我们接收到的消息会封装到一个TextWebSocketFrame中
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用来保存所有的客户端连接
    private static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //创建一个时间生成器
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:MM");

    @Override //该方面当接收到数据时候会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text=msg.text();
        System.out.println("接收到的消息体为: "+text);
        RecordService recordService=null;
        try{
            //通过springUtil 工具类获取spring上下文容器,
            recordService = SpringUtil.getBean(RecordService.class);

        }catch (Exception e)
        {
            System.out.println("容器获取异常");
            e.printStackTrace();
        }

        /*//遍历clients(所有客户端,群发)
        for (Channel client:clients){
            //发送消息并刷新通道
            client.writeAndFlush(new TextWebSocketFrame(sdf.format(new Date())+": "+text));
        }*/
        //将传过来的消息转化为一个json对象
        Message message =null;
        try{
            message= JSON.parseObject(text, Message.class);

        }catch (Exception e)
        {
            System.out.println("message获取异常");
            e.printStackTrace();
        }

        switch (message.getType()){
            case 0:
                //建立用户与通道的关联
                String userid=message.getRecord().getUserid();
                //存储用户id和通道之间的映射
                UserChannelMap.put(userid,ctx.channel());
                System.out.println("建立用户id:"+userid+"和通道id:"+ctx.channel().id()+"之间的映射");
                break;
            case 1://type为 收发消息
                //1.将聊天消息存储入库
                Record record= message.getRecord();
                recordService.add(record);
                //2.客户端收发消息
                Channel friendChannel = UserChannelMap.getChannelById(record.getFriendid());
                if (friendChannel!=null){ //如果用户在线,直接发送给好友
                    friendChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString( message)));
                }else {//如果用户不在线,暂时不发送
                    System.out.println("用户"+record.getFriendid()+"不在线");
                }
                break;
            case 2:
                //向好友端发送一个刷新请求列表的指令
                String reqFriendid = message.getRecord().getFriendid();
                Channel reqFriendChanne = UserChannelMap.getChannelById(reqFriendid);
                if (reqFriendChanne!=null){ //如果用户在线,直接发送给好友
                    reqFriendChanne.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString( message)));
                }
                //如果用户不在线则不做处理
                break;
            case 3:
                //通知用户刷新好友列表(本人同意或者删除好友时候 通知对方刷新好友列表)
                String refreshFriendid = message.getRecord().getFriendid();
                Channel refreshFriendChanne = UserChannelMap.getChannelById(refreshFriendid);
                if (refreshFriendChanne!=null){ //如果用户在线,直接发送给好友,刷新好友列表命令
                    refreshFriendChanne.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString( message)));
                }
                //如果用户不在线则不做处理
                break;

        }
    }

    @Override   //当有新的客户端接入到服务器时候会自动调用该方法
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());//将新的连接加入channel中
    }

    /**
     * 功能描述: netty原有方法当出现异常时候被调用
     * 这里我设置当出现异常时候我们关闭通道,并接触map中这对用户id和通道之间的关联
     * @Param: [ctx, cause]
     * @Return: void
     * @Author: Zyh
     * @Date: 2020/2/7 1:45
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
             System.out.println("出现异常"+cause.toString());
             System.out.println("出现异常"+cause.getStackTrace());
             UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
             ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  
             System.out.println("关闭通道");
             UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
             UserChannelMap.printMap();
    }
}