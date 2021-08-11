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


import com.zyh.chat.dao.RecordDao;
import com.zyh.chat.pojo.Record;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class RecordService {

	@Autowired
	private RecordDao recordDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Record> findAll() {
		return recordDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Record> findSearch(Map whereMap, int page, int size) {
		Specification<Record> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return recordDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Record> findSearch(Map whereMap) {
		Specification<Record> specification = createSpecification(whereMap);
		return recordDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Record findById(String id) {
		return recordDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param record
	 */
	public void add(Record record) {
		record.setId( idWorker.nextId()+"" );
		record.setCreatetime(new Date());
		record.setHas_read(0);
		record.setHas_delete(0);
		recordDao.save(record);
	}

	/**
	 * 修改
	 * @param record
	 */
	public void update(Record record) {
		recordDao.save(record);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		recordDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Record> createSpecification(Map searchMap) {

		return new Specification<Record>() {

			@Override
			public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 用户id
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 好友id
                if (searchMap.get("friendid")!=null && !"".equals(searchMap.get("friendid"))) {
                	predicateList.add(cb.like(root.get("friendid").as(String.class), "%"+(String)searchMap.get("friendid")+"%"));
                }
                // 消息
                if (searchMap.get("message")!=null && !"".equals(searchMap.get("message"))) {
                	predicateList.add(cb.like(root.get("message").as(String.class), "%"+(String)searchMap.get("message")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	public void deleteRecode(String userid, String friends_id) {
		recordDao.deleteRecode(userid,friends_id);
		recordDao.deleteRecode(friends_id,userid);
	}

	public List<Record> findByUserIdAndFriendId(String userid, String friendid) {
		return recordDao.findByUserIdAndFriendId(userid,friendid,friendid,userid);
	}

	public void setRead(String friendid, String userid) {
		recordDao.setRead(friendid,userid);
	}

	public List getUnreadById(String id) {
		return recordDao.getUnreadById(id);
	}
}
