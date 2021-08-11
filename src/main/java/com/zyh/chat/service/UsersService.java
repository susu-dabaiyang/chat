package com.zyh.chat.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.zyh.chat.util.IdWorker;

import com.zyh.chat.dao.UsersDao;
import com.zyh.chat.pojo.Users;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后端管理平台服务层
 * 
 * @author zyh
 *
 */
@Service
@Transactional
public class UsersService {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Users> findAll() {
		return usersDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Users> findSearch(Map whereMap, int page, int size) {
		Specification<Users> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return usersDao.findAll(specification, pageRequest);
	}


	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Users> findSearch(Map whereMap) {
		Specification<Users> specification = createSpecification(whereMap);
		return usersDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Users findById(String id) {
		return usersDao.findByUserid(id);
	}

	/**
	 * 增加
	 * @param users
	 */
	public void add(Users users) {
		users.setUserid(idWorker.nextId()+"");
		usersDao.save(users);
	}

	/**
	 * 修改
	 * @param users
	 */
	public void update(Users users) {
		usersDao.save(users);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		usersDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Users> createSpecification(Map searchMap) {

		return new Specification<Users>() {

			@Override
			public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("username")!=null && !"".equals(searchMap.get("username"))) {
                	predicateList.add(cb.like(root.get("username").as(String.class), "%"+(String)searchMap.get("username")+"%"));
                }
                // 
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 
                if (searchMap.get("major")!=null && !"".equals(searchMap.get("major"))) {
                	predicateList.add(cb.like(root.get("major").as(String.class), "%"+(String)searchMap.get("major")+"%"));
                }
                // 
                if (searchMap.get("experience")!=null && !"".equals(searchMap.get("experience"))) {
                	predicateList.add(cb.like(root.get("experience").as(String.class), "%"+(String)searchMap.get("experience")+"%"));
                }
                // 
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 
                if (searchMap.get("current_state")!=null && !"".equals(searchMap.get("current_state"))) {
                	predicateList.add(cb.like(root.get("current_state").as(String.class), "%"+(String)searchMap.get("current_state")+"%"));
                }
                // 
                if (searchMap.get("UNdergraduateschool")!=null && !"".equals(searchMap.get("UNdergraduateschool"))) {
                	predicateList.add(cb.like(root.get("UNdergraduateschool").as(String.class), "%"+(String)searchMap.get("UNdergraduateschool")+"%"));
                }
                // 
                if (searchMap.get("picture")!=null && !"".equals(searchMap.get("picture"))) {
                	predicateList.add(cb.like(root.get("picture").as(String.class), "%"+(String)searchMap.get("picture")+"%"));
                }
                // 
                if (searchMap.get("Email")!=null && !"".equals(searchMap.get("Email"))) {
                	predicateList.add(cb.like(root.get("Email").as(String.class), "%"+(String)searchMap.get("Email")+"%"));
                }
                // 
                if (searchMap.get("direction")!=null && !"".equals(searchMap.get("direction"))) {
                	predicateList.add(cb.like(root.get("direction").as(String.class), "%"+(String)searchMap.get("direction")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	public Page<Users> findBypage( int page, int size) {
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return usersDao.findAll(pageRequest);
	}

	public Page<Users> findUserByCondition(Users user, int page, int size) {
		Pageable pageable=PageRequest.of(page-1,size);
		return usersDao.findAll(new Specification<Users>() {
			@Override
			public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list=new LinkedList<>();
				System.out.println("条件为===================");
				System.out.println(user.getMajor_fk()+"				"+user.getDirection());
				System.out.println("条件为===================");
				if (user.getMajor_fk()!=null&&!"".equals(user.getMajor_fk())){//如果存在专业限制条件
					Predicate major = cb.equal(root.get("major_fk").as(String.class), user.getMajor_fk());//条件之专业
					list.add(major);
				}
				if (user.getDirection()!=null&&!"".equals(user.getDirection())){//如果存在限制毕业后去向条件
					Predicate direction = cb.equal(root.get("direction").as(String.class), user.getDirection());//条件之方向
					list.add(direction);
				}

				Predicate direction = cb.equal(root.get("isshow").as(String.class), "1");//条件之显示用户
				list.add(direction);
				//cd.and()封装多个条件时候要求传入一个数组
				//但是直接用数据的话,这里又不清楚到底传入了多少条件,所以这里用list先存储,用list大小来代替数组长度
				//最后再转化
				Predicate[] predicates=new Predicate[list.size()];
				list.toArray(predicates);//list里数据复制到数组中
				return cb.and(predicates);
			}
		},pageable);

	}
	public Users findByPhonenumer(String phonenumber) {
		return usersDao.findByPhonenumber(phonenumber);
	}

	public Users findByPhone(String phone) {
		return  usersDao.findByPhonenumber(phone);
	}

	public void editPassword(Users users) {
		 usersDao.editPassword(users.getPassword(),users.getPhonenumber());
	}


	public Users findByusername(String username) {

		return usersDao.findByUsername(username);
	}

	//根据传过来的用户名或者手机号查询是否存在该用户
	public Users findByCondition(String condition) {
		return usersDao.findByCondition(condition,condition);
	}
}
