package com.zyh.chat.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zyh.chat.entity.Result;
import com.zyh.chat.entity.StatusCode;
import com.zyh.chat.pojo.Users;
import com.zyh.chat.service.UsersService;
import com.zyh.chat.util.ConstantPropertiesUtils;
import com.zyh.chat.util.IdWorker;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


/**
 * 功能描述: 上传文件到阿里云oss
 *
 * @Param:
 * @Return:
 * @Author: Zyh
 * @Date: 2020/2/2 23:19
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/fileupload")
public class FileUploadConteoller {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UsersService usersService;

    /**
     * 功能描述: 上传用户头像
     *
     * @Param: [file]
     * @Return: com.zyh.chat.entity.Result 返回头像上传后在阿里云oss上的路径
     * @Author: Zyh
     * @Date: 2020/2/3 0:19
     */
    @RequestMapping(value = "/file/{userid}", method = RequestMethod.POST)
    public Result fileUpload(@RequestParam("file") MultipartFile file, @PathVariable String userid) {
        /*上传文件到阿里云oss*/
        // 地域结点Endpoint以北京为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.ENDPOINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，
        String accessKeyId = ConstantPropertiesUtils.KEYID;
        String accessKeySecret = ConstantPropertiesUtils.KEYSECRET;
        String bucketName = ConstantPropertiesUtils.BUCKETNAME;
        /*上传文件到阿里云oss*/
        //1.使用@RequestParam("file") MultipartFile file可以接收到文件
        //使用MultiparatFile 接收文件需要指定一个参数名,这个值应该是我们前端指定的input文件标签名
        //2.得到上传文件的名称获取上传输入流
        String filename = file.getOriginalFilename();
        System.out.print("所要上传的头像名称为:" + filename);
        System.out.print("  接收的用户id为:" + userid);

        String dicpath = new DateTime().toString("yyyy/MM/dd");
        //以日期为路径
        //另外创建一个唯一id作为文件名字,并覆盖之前的文件名字，之前的文件名字这里只显示到console中
        filename = dicpath + "/" + idWorker.nextId() + filename.substring(filename.lastIndexOf("."));

        //以我手动上传的文件为例http://zyh-chat.oss-cn-beijing.aliyuncs.com/IMG_6407.JPG
        //前面都是固定的域https://zyh-chat.oss-cn-beijing.aliyuncs.com/
        /*自己拼出来在之后阿里云上的文件路径用作返回值*/
        String path = "http://" + bucketName + "." + endpoint + "/" + filename;
        //将用户头像路径存储入库
        Users user = usersService.findById(userid);
        user.setPicture(path);
        usersService.update(user);
        //将用户头像路径存储入库
        try {

            InputStream inputStream = file.getInputStream();
            /*上传文件到阿里云oss*/
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。//参数分别为仓库名称,文件名称,文件流
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            /*上传文件到阿里云oss*/

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "上传失败");
        }
        //返回头像在阿里云oss上的路径
        return new Result(true, StatusCode.OK, "上传成功", path);

    }

}
