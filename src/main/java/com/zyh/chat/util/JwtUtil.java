package com.zyh.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    private String key ;//盐

    private long ttl ;//过期时间

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);//这两步是为了获取当前时间
        JwtBuilder builder = Jwts.builder().setId(id) //用户id
                .setSubject(subject)//用户名称
                .setIssuedAt(now)//创建时间
                .signWith(SignatureAlgorithm.HS256, key).claim("roles", roles);
        if (ttl > 0) { //判断配置文件是否设置了过期时间,如若有的话,我们需要添加一个过期时间
            builder.setExpiration( new Date( nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
