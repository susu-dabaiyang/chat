package com.zyh.chat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zyh.chat.pojo.Record;
import com.zyh.chat.service.RecordService;


import com.zyh.chat.entity.PageResult;
import com.zyh.chat.entity.Result;
import com.zyh.chat.entity.StatusCode;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", recordService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", recordService.findById(id));
    }

    /**
     * 功能描述: 点击用户头像,加载聊天页面初始化的时候根据用户id和userid查询最近聊天记录
     * 并把消息的has_read全部置为1
     *
     * @Param: [userid, friendid] 点击者id,好友id
     * @Return: com.zyh.chat.entity.Result
     * @Author: Zyh
     * @Date: 2020/2/7 22:03
     */
    @RequestMapping(value = "/{userid}/{friendid}", method = RequestMethod.GET)
    public Result findByUserIdAndFriendId(@PathVariable String userid, @PathVariable String friendid) {
        List<Record> records = recordService.findByUserIdAndFriendId(userid, friendid);
        if (records == null || records.size() == 0) {//两人没有聊天记录
            return new Result(true, StatusCode.ERROR, "两人没有聊天", records);
        } else {
            //将从friendid发向userid的消息置为has_read=1
            recordService.setRead(friendid, userid);
            return new Result(true, StatusCode.OK, "查询聊天记录成功", records);
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
        Page<Record> pageList = recordService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Record>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", recordService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param record
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Record record) {
        recordService.add(record);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param record
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Record record, @PathVariable String id) {
        record.setId(id);
        recordService.update(record);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        recordService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/getunread/{id}", method = RequestMethod.GET)
    public Result getUnreadById(@PathVariable String id) {
        List idlist = recordService.getUnreadById(id);
        return new Result(true, StatusCode.OK, "查询用户的未读消息的发送者id,成功", idlist);
    }

}
