package ms.jwt;

import lombok.extern.slf4j.Slf4j;
import ms.entities.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private String expiration;


    public String getToken(User user) {
        log.info("get TOKEN " + user.toString());
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String,Object> extraClaims, User user) {    log.info("getToken User " + user.toString());

        if(SECRET_KEY == null) SECRET_KEY = "1Mo8XV9vL+jvHSKXOtxM4hhER6kG2ziPJ8Uxbpc09N4=";
        long expirationTimeLong = expiration != null ? Long.parseLong(expiration) : 60000L; // Parse the expiration time from the properties file
        final Date createdDate = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);


        return Jwts
                .builder()
                .claims(extraClaims)
                .claim("id", user.getId())
                .claim(("name"), user.getName())
                .claim("email", user.getEmail())
                .subject(user.getUsername())
                .issuedAt(createdDate)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private Claims getAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }

}
