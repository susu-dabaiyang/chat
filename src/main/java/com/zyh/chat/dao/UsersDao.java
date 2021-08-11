package com.zyh.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zyh.chat.pojo.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UsersDao extends JpaRepository<Users,String>,JpaSpecificationExecutor<Users>{

    Users findByPhonenumber(String phonenumber);

    @Modifying
    @Query(value = "UPDATE users SET password=? WHERE phonenumber=?;",nativeQuery = true)
    void editPassword(String password,String phonenumber);

    Users findByUserid(String id);

    Users findByUsername(String username);

    @Query(value = "select * from users where username=? or phonenumber=?",nativeQuery = true)
    Users findByCondition(String condition1,String condition2);
}
