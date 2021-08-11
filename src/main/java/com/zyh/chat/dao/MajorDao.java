package com.zyh.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zyh.chat.pojo.Major;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface MajorDao extends JpaRepository<Major,String>,JpaSpecificationExecutor<Major>{

    Major findByMajorname(String major);
}
