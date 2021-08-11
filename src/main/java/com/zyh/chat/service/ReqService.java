package com.zyh.chat.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.zyh.chat.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.zyh.chat.dao.ReqDao;
import com.zyh.chat.pojo.Req;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class ReqService {

	@Autowired
	private ReqDao reqDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Req> findAll() {
		return reqDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Req> findSearch(Map whereMap, int page, int size) {
		Specification<Req> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return reqDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Req> findSearch(Map whereMap) {
		return reqDao.findSearch((String) whereMap.get("status"),(String) whereMap.get("to_userid"));
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Req findById(String id) {
		return reqDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param req
	 */
	public void add(Req req) {
		req.setId( idWorker.nextId()+"" );
		req.setCreatetime(new Date());
		req.setStatus(0);
		reqDao.save(req);
	}

	/**
	 * 修改
	 * @param req
	 */
	public void update(Req req) {
		reqDao.save(req);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		reqDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Req> createSpecification(Map searchMap) {

		return new Specification<Req>() {

			@Override
			public Predicate toPredicate(Root<Req> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 请求好友用户id
                if (searchMap.get("from_userid")!=null && !"".equals(searchMap.get("from_userid"))) {
                	predicateList.add(cb.like(root.get("from_userid").as(String.class), "%"+(String)searchMap.get("from_userid")+"%"));
                }
                // 被请求好友用户id
                if (searchMap.get("to_userid")!=null && !"".equals(searchMap.get("to_userid"))) {
                	predicateList.add(cb.like(root.get("to_userid").as(String.class), "%"+(String)searchMap.get("to_userid")+"%"));
                }
                // 发送的消息
                if (searchMap.get("message")!=null && !"".equals(searchMap.get("message"))) {
                	predicateList.add(cb.like(root.get("message").as(String.class), "%"+(String)searchMap.get("message")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	public void updateStatus(String from_userid, String to_userid) {
		reqDao.updateStatus(from_userid,to_userid);
	}
}
