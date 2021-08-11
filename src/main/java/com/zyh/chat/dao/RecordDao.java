package com.zyh.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zyh.chat.pojo.Record;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecordDao extends JpaRepository<Record,String>,JpaSpecificationExecutor<Record>{

    @Modifying
    @Query(value = "delete from chat_record where userid=? and friendid=?",nativeQuery = true)
    void deleteRecode(String userid, String friends_id);

    @Query(value = "select * from chat_record where userid=? and friendid=? OR userid=? and friendid=? ORDER BY createtime;",nativeQuery = true)
    List<Record> findByUserIdAndFriendId(String userid, String friendid,String f,String u);

    @Modifying
    @Query(value = "update  chat_record set  has_read=1 where userid=? and friendid=?",nativeQuery = true)
    void setRead(String friendid, String userid);


    @Query(value = "select distinct userid from chat_record where friendid=?  AND has_read=0 ;",nativeQuery = true)
    List getUnreadById(String id);

}
