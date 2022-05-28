package com.github.manerajona.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    private int keylength;
    private String algorithm;
    private String expiration;

    /*
    * Ideally the secret key should be stored in a secure place,
    * like Vault or as K8 secret.
    */
    private Key key;

    @PostConstruct
    protected void init() {
        KeyPair keyPair = JwtRandomKeyPair.of(this.algorithm, this.keylength);
        this.key = keyPair.getPrivate();
    }

    protected static final String BEARER = "Bearer ";

    public String generateToken(UserDetails userDetails) {

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expirationToDate(this.expiration))
                .signWith(this.key)
                .compact();

        return BEARER.concat(token == null ? "" : token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        token = JwtUtils.extractToken(token);
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        token = JwtUtils.extractToken(token);
        return getClaims(token, this.key).getSubject();
    }

    public Date extractExpirationDate(String token) {
        token = JwtUtils.extractToken(token);
        return getClaims(token, this.key).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        token = JwtUtils.extractToken(token);
        return extractExpirationDate(token).before(Calendar.getInstance().getTime());
    }

    protected static Claims getClaims(String token, Key secret) {
        token = JwtUtils.extractToken(token);
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    protected static Date expirationToDate(String expiration) {
        final long millis = Calendar.getInstance().getTime().getTime() + Long.parseLong(expiration);
        return new Date(millis);
    }

    protected static String extractToken(String token) {
        return Optional.ofNullable(token)
                .filter(t -> t.startsWith(BEARER))
                .map(t -> t.split(" ")[1].trim())
                .orElse(token);
    }
}
