package com.zyh.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zyh.chat.pojo.Friend;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface FriendDao extends JpaRepository<Friend,String>,JpaSpecificationExecutor<Friend>{

    List<Friend> findByUserid(String userid);

    @Query(value = "select * from friend where userid=? and friends_id=?;",nativeQuery = true)
    List<Friend> firendJudge(String userid, String friends_id);

    //根据用户id和好友id删除好友关系
    @Modifying
    @Query(value = "delete  from friend where userid=? and friends_id=?;",nativeQuery = true)
    void deleteFriend(String userid, String friends_id);

    @Query(value = "select * from friend where userid=? and friends_id=?;",nativeQuery = true)
    Friend findByUseridAndFriends_id(String userid, String friends_id);
}
