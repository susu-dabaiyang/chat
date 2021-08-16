>项目背景:我[大四的毕设设计](https://www.jianshu.com/p/aff9fc2bdac8)的功能之一,也是为了对之前一个不了解的技术点进行扫盲
之前嫌麻烦,**考虑过**是直接花钱用第三方的通讯还是做自己的通讯工具,然而这个想法**几分钟**就得出了答案,**我**TM是**开发**人员啊!!!啥都用人家的,我到公司不会被老板天天**按在地上捶**嘛......

当然还有一个原因不可忽视...**我没钱啊!**
网上的网页通讯动辄几百上千的,吓死个人
**LayIM 600块钱还只是前端**
网易云信 **1800块钱一个月**...
诸如此类,算了吧...我还是自己研究研究算了

____________________

**技术基础**之前都有写过了,直通车看下面连接
**后端**:WebSocket+Netty+SpringBoot+SpringMVC+SpringData+Mysql
**中间件以及第三方服务**:RabbitMQ+Redis+阿里云短信+OSS对象存储系统+Nginx
[Netty简单介绍以及它的模型基础](https://www.jianshu.com/p/c5b66eef9d9e)
[websocket的定位以及其和其他连接的区别](https://www.jianshu.com/p/30a1dc7426a8)
[Netty+Websocket的群发即使通讯demo](https://www.jianshu.com/p/117c400f87a6)
**前端:**
html5+vue+一些UI,链接可以看我之前的前端专题.
专门学了一下vue基础...以后没事了再多学点,以后自己做**小玩具**方便.
____
**目前实现的部分功能:**
- 手机号登录注册以及密码修改
- 添加好友   (包括一些空账号,已是好友的判断)
- 删除好友 (包括清除好友关系以及聊天记录)
- 好友请求审核
- 好友详细信息展示
- 消息未读提醒
- 心跳机制以及读写超时
- 资料修改与头像上传
- 投诉反馈


上面其实只是大概功能,项目里其实为了**优化用户体验**做了很多很多很多**细节方面处理**.比如要求用户删除好友时自己这边列表和对方列表都要直接删除(类似于QQ删除好友的**及时性**),好友请求要求这边发送,对方好友请求列表即时响应,并即时的显示数量等等...
##### 注释十分详细了,希望对大家有所帮助,上一下效果图
![登录注册](https://upload-images.jianshu.io/upload_images/17502375-30d8d53930cfaa04.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![整体效果图](https://upload-images.jianshu.io/upload_images/17502375-18acf33f1fae4a0c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![点击自己头像,有信息展示](https://upload-images.jianshu.io/upload_images/17502375-cb3b20dee950a05b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![点击用户姓名或者头像右侧弹出展示详细信息,以及好友操作](https://upload-images.jianshu.io/upload_images/17502375-03e61749051a480d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![点击导航菜单的拓展功能展示](https://upload-images.jianshu.io/upload_images/17502375-72e35570cc5c227b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![修改个人信息,可以修改的信息比较多,板块较长只显示了部分](https://upload-images.jianshu.io/upload_images/17502375-2ac1983bee51afab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![头像上传](https://upload-images.jianshu.io/upload_images/17502375-8d57c5df2300a441.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![点击好友请求,左侧弹出好友请求展示栏](https://upload-images.jianshu.io/upload_images/17502375-c69da6e78002bb38.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![消息未读提醒](https://upload-images.jianshu.io/upload_images/17502375-e7746c93fea4d118.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
还有一个是全双工的即使聊天,跟我们正常聊天一样即时通讯,这个不好展示,自行脑补吧...或者联系我,我给你测试账号看看.


### 下面说一下关键代码部分设计
#### 服务端
##### 服务器设置
包括主从线程池,服务器端口,回调方法设计(就是供给后面监听器使用的方法)
当然这里还要指定一个通道初始化器,后面有介绍
```

@Component
public class WebSocketServer {
    private EventLoopGroup bossGroup;       // 主线程池
    private EventLoopGroup workerGroup;     // 工作线程池
    private ServerBootstrap server;         // 服务器
    private ChannelFuture future;           // 回调

    public void start() {
        future = server.bind(9090);
        System.out.println("netty server - 启动成功");
    }

    public WebSocketServer() {
        bossGroup = new NioEventLoopGroup(); //主线程池
        workerGroup = new NioEventLoopGroup();//从线程池

        //创建Netty服务器启动对象
        server = new ServerBootstrap();

        server.group(bossGroup, workerGroup)//为netty服务器指定和配备主从线程池
                .channel(NioServerSocketChannel.class)//指定netty通道类型
                //指定通道初始化器用来加载当channel收到消息后
                //如何进行业务处理
                .childHandler(new WebSocketChannelInitializer());
    }


}

```
##### 通道初始化器
```
/**
 * 功能描述: 通道初始化器器
 * 用来加载通道处理器(channelhandler)
 * @Author: Zyh
 * @Date: 2020/1/22 20:31
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    //初始化通道
    //在这个方法中加载对应的ChannelHandler
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /* 固定写法部分*/
        //获取管道,将一个个ChannelHandler添加到管道中
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //可以将channelpipeline理解为拦截器
        //当我们的socketChannel数据进来时候会依次调用我们的ChannelHandler

        //添加一个http的编解码器
        channelPipeline.addLast(new HttpServerCodec());
        //添加大数据流支持
        channelPipeline.addLast(new ChunkedWriteHandler());
        //添加聚合器 ,可以将我们的httpmaessage聚合成Fullhttprequest/respond ---想拿到请求和响应就要添加聚合器
        channelPipeline.addLast(new HttpObjectAggregator(1024*24));//指定缓存大小
        /* 固定写法部分*/

        //指定接收请求的路由
        //指定必须使用ws为结尾的url才能访问
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加自定义的handler进行业务处理
        channelPipeline.addLast(new ChatHandler());


    }
}

```
##### ApplicationListener,使我们的netty在加载完spring容器时候启动
```

@Component
public class NettyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private WebSocketServer websocketServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            try {
                websocketServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

```
另外这里要注意,我们使用Spring和使用SpringMVC之后系统会存在两个上下文,applicatioContext和webApplicatioContext,在web 项目中（spring mvc），系统会存在两个容器，一个是root application context ,另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。

这种情况下，就会造成onApplicationEvent方法被执行两次。为了避免上面提到的问题，我们可以只在root application context初始化完成后调用逻辑代码，其他的容器的初始化完成，则不做任何处理，修改后代码

如下：

     @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            try {
                websocketServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


有可能出现一些情况使用了上述判断还会多次执行(本人的正常,所以也不知道问题出在哪里),也有前辈发现使用以下判断更加准确：
``
event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")
``
>关于监听参考了
[https://www.cnblogs.com/a757956132/p/5039438.html](https://www.cnblogs.com/a757956132/p/5039438.html)
[https://www.iteye.com/blog/zhaoshijie-1974682](https://www.iteye.com/blog/zhaoshijie-1974682)

#### 继续说一下netty的通道初始化器方法
包括添加编解码器,聚合器(拿到请求和响应的),数据流支持
最重要的是**管道(客户端过来之后就有一条从客户端到Netty的管道,可想而知它的重要性所在)的获取以及定义处理管道的方法**
定义接收请求的路由
```

/**
 * 功能描述: 通道初始化器器
 * 用来加载通道处理器(channelhandler)
 * @Author: Zyh
 * @Date: 2020/1/22 20:31
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    //初始化通道
    //在这个方法中加载对应的ChannelHandler
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /* 固定写法部分*/
        //获取管道,将一个个ChannelHandler添加到管道中
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //可以将channelpipeline理解为拦截器
        //当我们的socketChannel数据进来时候会依次调用我们的ChannelHandler

        //添加一个http的编解码器
        channelPipeline.addLast(new HttpServerCodec());
        //添加大数据流支持
        channelPipeline.addLast(new ChunkedWriteHandler());
        //添加聚合器 ,可以将我们的httpmaessage聚合成Fullhttprequest/respond ---想拿到请求和响应就要添加聚合器
        channelPipeline.addLast(new HttpObjectAggregator(1024*24));//指定缓存大小
        /* 固定写法部分*/

        //指定接收请求的路由
        //指定必须使用ws为结尾的url才能访问
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加自定义的handler进行业务处理
        channelPipeline.addLast(new ChatHandler());


    }
}
```
### 通讯协议以及消息代理
```
@Configuration
@EnableWebSocketMessageBroker//@EnableWebSocketMessageBroker注解表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //2.registerStompEndpoints方法表示注册STOMP协议的节点，并指定映射的URL。

        //3.stompEndpointRegistry.addEndpoint("/endpointSang").withSockJS();这一行代码用来注册STOMP协议节点，同时指定使用SockJS协议。
        stompEndpointRegistry.addEndpoint("/ws/endpointChat").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //4.configureMessageBroker方法用来配置消息代理，由于我们是实现推送功能，这里的消息代理是/topic
        registry.enableSimpleBroker("/queue","/topic");
        //这里我并未使用原生的websocket协议，而是使用了websocket的子协议stomp，方便一些。
        // 消息代理既使用了/queue,又使用了/topic，主要是因为我这里既有点对点的单聊(queue)，也有发送系统消息的群聊(topic)。
    }
}

```

### 消息体
首先呢.从我们websocket传过来的消息应该分为很多种,包括单纯建立连接,以及需要转发消息的私信功能以及消息存储功能,还有断开连接等等
```

public class Message {
    //消息类型---0建立连接 1发送消息
    private Integer type;
    private Record record;//聊天消息
    private Object ext;//扩展类型消息

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", record=" + record.toString() +
                ", ext=" + ext +
                '}';
    }
}
```
### 用户和管道之间的映射
包括建立连接映射关系
解除关系
打印映射表
以及条件查找
```
import io.netty.channel.Channel;//neety通道别搞错了哦

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

```

#### 最后就是处理器了
```


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
``` 
#### 另外我们做netty和sprinboot整合的时候需要拿到spring的bean
netty接收到客户端传过来的消息后,我们需要将聊天记录存储入库,但是我们的netty服务器是无法直接拿到我们定义的一些组件的如controller,service,如果都交给spring容器托管也行,但是我代码里有些地方用了new，并没有交给spring IOC托管,所以我这里做了一个工具static成员类,初始化的时候获取spring 上下文对象,并且定义了一些方法来获取bean
```
/**
 * @Description: 提供手动获取被spring管理的bean对象
 */
@Component
public class SpringUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}

```


###### 业务层是springmvc+springdata,代码繁琐,这里就不给大家填麻烦了
![](https://upload-images.jianshu.io/upload_images/17502375-450081df66c63468.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 其实这些都不难,前端的js比较麻烦....
(如果换一个专业的牛人肯定会比我设计的好),我的前端总着来说像是脑袋一热需要这个就去获取一次这个.
聊天界面一次刷新34个请求78ms,![](https://upload-images.jianshu.io/upload_images/17502375-f4f8d949dde26836.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)几乎没有任何等待,消息实时通讯也没有任何迟缓,似乎还不错,不过随着用户的好友数目太多,消息太频繁还有许多优化上的设计要解决,后面我会看看nginx啥玩意的,后端的话,可能涉及一些安全性问题还没有考虑和保护,还有很多进步空间

想想看人家一套源码似乎不贵了,(错觉来了,我又不是买的所有权,只是使用权啊!),果然知识就是力量,来重复三遍知识就是力量,知识就是力量,知识就是力量,好好学习天天向上

##### 说完这么多,其实后端不涉及细节问题
一些处理方面,当前端都是自己做的时候需要考虑非常多非常多,包括每个数据每一个参数的设置和存储的敲定,甚至包括它的生成和失效的时间即生命周期,这里再次感谢一些vue的双向绑定的骚操作,以及清晰生命周期和它的完善的钩子函数,让我这个前端小白也能做一次全栈(蛋疼的全栈)....

