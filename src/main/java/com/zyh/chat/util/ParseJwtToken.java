package com.zyh.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtToken {
    public static void main(String[] args){
        JwtParser jwtParser = Jwts.parser().setSigningKey("zyhhashhahahha");
        Claims body = jwtParser
                .parseClaimsJws("xxxx")
                .getBody();
        System.out.println("用户id:"+body.getId()+"    " +
                "IssuedAt: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(body.getIssuedAt())+
                "    setSubject:"+body.getSubject());
        System.out.println("获取自定义载荷之键值为name的值: "+body.get("name"));

        Claims body1 = jwtParser
                .parseClaimsJws("xxxx")
                .getBody();
        System.out.println("用户id:"+body1.getId()+"    " +
                "IssuedAt: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(body1.getIssuedAt())+
                "    setSubject"+body1.getSubject());


    }
}
