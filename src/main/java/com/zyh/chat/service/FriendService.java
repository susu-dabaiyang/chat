package com.zyh.chat.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.zyh.chat.dao.UsersDao;
import com.zyh.chat.pojo.Users;
import com.zyh.chat.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.zyh.chat.dao.FriendDao;
import com.zyh.chat.pojo.Friend;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class FriendService {

	@Autowired
	private FriendDao friendDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private ReqService reqService;

	@Autowired
	private UsersDao usersDao;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Friend> findAll() {
		return friendDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Friend> findSearch(Map whereMap, int page, int size) {
		Specification<Friend> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return friendDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Friend> findSearch(Map whereMap) {
		Specification<Friend> specification = createSpecification(whereMap);
		return friendDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Friend findById(String id) {
		return friendDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param friend
	 */
	public void add(Friend friend) {
		//添加用户到好友 的好友关系
		friend.setId( idWorker.nextId()+"" );
		friend.setCreatetime(new Date());
		friendDao.save(friend);

		//添加好友到用户 的好友关系
		//在添加好友时候要双方都确定关系
		Friend friend2=new Friend();
		friend2.setCreatetime(new Date());
		friend2.setUserid(friend.getFriends_id());
		friend2.setFriends_id(friend.getUserid());
		friend2.setId(idWorker.nextId()+"");
		friendDao.save(friend2);


		//另外在添加完好友之后,我们应该将原来的请求状态置为1
		reqService.updateStatus(friend.getFriends_id(),friend.getUserid());
	}

	/**
	 * 修改
	 * @param friend
	 */
	public void update(Friend friend) {
		friendDao.save(friend);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		friendDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Friend> createSpecification(Map searchMap) {

		return new Specification<Friend>() {

			@Override
			public Predicate toPredicate(Root<Friend> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 用户id
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 好友id
                if (searchMap.get("friends_id")!=null && !"".equals(searchMap.get("friends_id"))) {
                	predicateList.add(cb.like(root.get("friends_id").as(String.class), "%"+(String)searchMap.get("friends_id")+"%"));
                }
                // 备注
                if (searchMap.get("comments")!=null && !"".equals(searchMap.get("comments"))) {
                	predicateList.add(cb.like(root.get("comments").as(String.class), "%"+(String)searchMap.get("comments")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	public List<Users> findByUserId(String userid) {
		List<Friend> friends = friendDao.findByUserid(userid);
		List<Users> users=new ArrayList<>();
		for (Friend friend: friends){
			Users user = usersDao.findByUserid(friend.getFriends_id());//根据好友id查找到该用户信息
			users.add(user);
		}
		return users;
	}

	public boolean friendJudge(String userid, String friends_id) {
		List<Friend> friends = friendDao.firendJudge(userid, friends_id);
		if (friends==null||friends.size()==0){
			return false; //不是朋友
		}else {
			return true;//是朋友
		}
	}

	public void deleteFriend(String userid, String friends_id) {
		friendDao.deleteFriend(userid,friends_id);
	}

	public Friend findByUseridAndFriend_id(String userid, String friends_id) {
		return  friendDao.findByUseridAndFriends_id(userid,friends_id);
	}
}
