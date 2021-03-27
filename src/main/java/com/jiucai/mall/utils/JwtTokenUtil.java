package com.jiucai.mall.utils;

import com.jiucai.mall.common.Constant;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtTokenUtil {
    private static String key = Constant.jwtSetting.JWT_KEY;

    private static long ttl = 1000*60*Constant.jwtSetting.minutes;
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
     * @param username
     * @return
     */
    public static String createJWT(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        if (ttl > 0) {
            builder.setExpiration( new Date( nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     *
     * @param jwtStr
     * @return
     */
    public static Claims parseJWT(String jwtStr) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
