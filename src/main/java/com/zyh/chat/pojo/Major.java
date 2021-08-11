package com.zyh.chat.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 实体类
 *
 * @author Administrator
 */
@Entity
@Table(name = "major")
public class Major implements Serializable {

    @Id
    private String majorid;


    private String majorname;
    private String subject_fk;


    public String getMajorid() {
        return majorid;
    }

    public void setMajorid(String majorid) {
        this.majorid = majorid;
    }

    public String getMajorname() {
        return majorname;
    }

    public void setMajorname(String majorname) {
        this.majorname = majorname;
    }

    public String getSubject_fk() {
        return subject_fk;
    }

    public void setSubject_fk(String subject_fk) {
        this.subject_fk = subject_fk;
    }

    @Override
    public String toString() {
        return "Major{" +
                "majorid='" + majorid + '\'' +
                ", majorname='" + majorname + '\'' +
                ", subject_fk='" + subject_fk + '\'' +
                '}';
    }
}
