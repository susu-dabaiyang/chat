package com.zyh.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zyh.chat.pojo.Req;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ReqDao extends JpaRepository<Req,String>,JpaSpecificationExecutor<Req>{
    @Modifying
    @Query(value = "update friend_req set status=1 where from_userid=? and to_userid=?;",nativeQuery = true)
    void updateStatus(String from_userid, String to_userid);

    @Query(value = "select * from friend_req  where status=? and to_userid=?;",nativeQuery = true)
    List<Req> findSearch(String status, String to_userid);
}
