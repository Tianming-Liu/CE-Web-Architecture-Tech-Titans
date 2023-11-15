package com.czandlh.service.impl;

import com.czandlh.entity.RestUser;
import com.czandlh.service.RestJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class RestJwtServiceImpl implements RestJwtService {

    private static final String TOKEN_SECRET = "ZCEQIUBFKSJBFJH2020BQWE";

    @Override
    public String signUser(RestUser user) {
        long time = 1800 * 1000 * 4;
        return Jwts.builder().claim("user", user).setExpiration(new Date(System.currentTimeMillis() + time)).signWith(SignatureAlgorithm.HS256, TOKEN_SECRET).compact();
    }

    @Override
    public RestUser getUser(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            Map map = body.get("user", Map.class);
            RestUser user;
            if (map != null) {
                user = new RestUser();
                if (map.get("username") != null) {
                    user.setUsername((String) map.get("username"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
