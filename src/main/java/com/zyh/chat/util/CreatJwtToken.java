package com.zyh.chat.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreatJwtToken {
    public static void main(String[] args){
        JwtBuilder builder= Jwts.builder().setId("888")
                .setSubject("小白")
                .setIssuedAt(new Date())
                .claim("name","zyh")
                .signWith(SignatureAlgorithm.HS256,"zyhhashhahahha");//前面指定编码,后面为盐(保密)

        System.out.println(builder.compact());
        /*
         * setIssuedAt用于设置签发时间
         * signWith用于设置签名秘钥
         * */
    }
}
