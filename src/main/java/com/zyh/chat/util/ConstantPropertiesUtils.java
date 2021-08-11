package com.zyh.chat.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//服务器启动的时候读取配置文件的内容
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //如果我们直接@value("a")是把a赋值给endpoint
    //所以我们需要使用spring提供的一个方法"${a}"来获取a对应的值
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyid;

    @Value("${aliyun.oss.file.keysecret}")
    private String keysecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketname;

    //这些值赋值好了但是我们没用办法直接使用的。

    //我们可以设置一些staic静态变量，这样就可以直接用类名.来调用了
    public static String ENDPOINT;
    public static String KEYID;
    public static String KEYSECRET;
    public static String BUCKETNAME;

    //服务器启动就会初始化ConstantYmlUtils并且调用afterPropertiesSet方法读取配置文件的内容
    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT=endpoint;
        KEYID=keyid;
        KEYSECRET=keysecret;
        BUCKETNAME=bucketname;
    }
}
