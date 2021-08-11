package com.zyh.chat.controller;
import java.util.Map;

import com.zyh.chat.entity.PageResult;
import com.zyh.chat.entity.StatusCode;
import com.zyh.chat.service.RecordService;
import com.zyh.chat.service.ReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zyh.chat.pojo.Friend;
import com.zyh.chat.service.FriendService;

import com.zyh.chat.entity.Result;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;
	
	@Autowired
	private RecordService recordService;

	@Autowired
	private ReqService reqService;
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",friendService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",friendService.findById(id));
	}
	/**
	 * 功能描述: 根据用户id找到所有的好友信息
	 * @Param: [userid]
	 * @Return: com.zyh.chat.entity.Result
	 * @Author: Zyh
	 * @Date: 2020/2/1 23:11
	 */
	@RequestMapping(value="/user/{userid}",method= RequestMethod.GET)
	public Result findByUserId(@PathVariable String userid){
		return new Result(true,StatusCode.OK,"查询成功",friendService.findByUserId(userid));
	}
	/**
	 * 功能描述: 根据用户id和好友id,删除好友
	 * @Param: [friend]
	 * @Return: com.zyh.chat.entity.Result
	 * @Author: Zyh
	 * @Date: 2020/2/6 20:14
	 */
	@RequestMapping(value = "/deleteFriend",method = RequestMethod.POST)
	public Result deleteFriend(@RequestBody Friend friend){
		//本次删除要删除好友关系,以及好友聊天记录record
		try{

			//删除好友关系
			friendService.deleteFriend(friend.getUserid(),friend.getFriends_id());
			friendService.deleteFriend(friend.getFriends_id(),friend.getUserid());

			//删除好友聊天记录
			recordService.deleteRecode(friend.getUserid(),friend.getFriends_id());
			recordService.deleteRecode(friend.getFriends_id(),friend.getUserid());
		}catch (Exception e)
		{
		return new Result(true,StatusCode.ERROR,"删除操作出现异常,请刷新后重试");
		}
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Friend> pageList = friendService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Friend>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
			 */
	@RequestMapping(value="/search",method = RequestMethod.POST)
	public Result findSearch( @RequestBody Map<String,String> searchMap){
		System.out.print("搜索好友条件:");
		for (String key:searchMap.keySet()){
			System.out.println(key+":"+searchMap.get(key));
		}
		//是朋友
		if(friendService.friendJudge(searchMap.get("userid"),searchMap.get("friends_id"))){
		return new Result(true,StatusCode.OK,"你们已是好友");
		}else {
		return new Result(true,StatusCode.ERROR,"不存在好友关系");
		}
	}

	/**
	 * 增加,某一方接受好友请求后的行为
	 * @param friend
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Friend friend  ){
		//如果已经存在好友关系,则不需要重新建立关系,避免多次发送请求的情况
		Friend friendRes=friendService.findByUseridAndFriend_id(friend.getUserid(),friend.getFriends_id());
		if (friendRes==null){
			friendService.add(friend);
			return new Result(true,StatusCode.OK,"增加成功");
		}else {
			reqService.updateStatus(friend.getFriends_id(),friend.getUserid());
			return new Result(true,StatusCode.OK,"已经是好友了,无需再次添加");
		}
	}
	
	/**
	 * 修改
	 * @param friend
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Friend friend, @PathVariable String id ){
		friend.setId(id);
		friendService.update(friend);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		friendService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
