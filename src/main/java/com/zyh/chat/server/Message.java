package com.zyh.chat.server;

import com.zyh.chat.pojo.Record;

public class Message {
    //消息类型---0建立连接 1发送消息 2发送好友请求通知另一方刷新好友请求list 3接收好友请求或者删除好友后通知对方刷新好友列表
    private Integer type;
    private Record record;//聊天消息
    private Object ext;//扩展类型消息

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", record=" + record.toString() +
                ", ext=" + ext +
                '}';
    }
}
