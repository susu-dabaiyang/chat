package com.zyh.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zyh.chat.pojo.Req;
import com.zyh.chat.service.ReqService;


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
@RequestMapping("/req")
public class ReqController {

    @Autowired
    private ReqService reqService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", reqService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", reqService.findById(id));
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
        Page<Req> pageList = reqService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Req>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map<String, String> searchMap) {
        System.out.print("初始化页面查找到的好友请求信息为:");
        for (String key : searchMap.keySet()) {
            System.out.print(key + ":" + searchMap.get(key) + "  ");
        }
        return new Result(true, StatusCode.OK, "查询成功", reqService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param req
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Req req) {
        Map<String, String> map = new HashMap<>();
        map.put("from_userid", req.getFrom_userid());
        map.put("to_userid", req.getTo_userid());
        if (reqService.findSearch(map).size() != 0) {//如果之前已经发送了请求,这里直接不处理
            return new Result(true, StatusCode.ERROR, "重复发送请求");
        }
        reqService.add(req);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param req
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Req req, @PathVariable String id) {
        req.setId(id);
        reqService.update(req);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 拒绝好友请求,修改状态status为1,表示处理过了
     *
     * @param req
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public Result updateStatus(@RequestBody Req req) {
        reqService.updateStatus(req.getFrom_userid(), req.getTo_userid());
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        reqService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
