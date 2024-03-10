package com.vinta.utils;


import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.convert.StringToDataTypeConverter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
@Data
public class JWTUtil {
    private final String secret = "737844829@qq.com!!";
    private final Duration expireTime = Duration.ofMinutes(60);

    /**
     * 生成token
     *
     * @param subject: token的id
     * @param claims   附加信息
     */
    public String createToken(String subject, Map<String, Object> claims) {
        JwtBuilder jwtBuilder = Jwts.builder();
        if (claims != null) {
            jwtBuilder.setClaims(claims);
        }
        if (StringUtil.hasContent(subject)) {
            jwtBuilder.setSubject(subject);
        }
        long currentTimeMillis = System.currentTimeMillis();
        jwtBuilder.setIssuedAt(new Date(currentTimeMillis));
        long expire = currentTimeMillis + expireTime.toMillis();
        if (expire > 0) {
            jwtBuilder.setExpiration(new Date(expire));
        }
        if (StringUtil.hasContent(secret)) {
            jwtBuilder.signWith(SignatureAlgorithm.HS256, secret);
        }
        return jwtBuilder.compact();

    }

    public Claims parseToken(String token) {
        return Jwts.parser().
                setSigningKey(secret).
                parseClaimsJws(token).
                getBody();
    }

    public String getUserId(String token) {
        boolean validateToken = validateToken(token);
        if(!validateToken){
            throw new BusinessException(StatusCode.TOKEN_ERROR);
        }
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        Date expTime = claims.getExpiration();
        return expTime != null && expTime.after(new Date());
    }
}
