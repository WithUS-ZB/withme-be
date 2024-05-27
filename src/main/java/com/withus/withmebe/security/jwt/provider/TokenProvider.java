package com.withus.withmebe.security.jwt.provider;

import static com.withus.withmebe.common.exception.ExceptionCode.TOKEN_EXPIRED;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.security.jwt.repository.AccessTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 3_600_000L * 24L; // 1000 * 60 * 60 // 1hour
  // TODO: 테스트 상으로
  private static final String KEY_ROLES = "roles";
  private final UserDetailsService userDetailsService;
  private SecretKey key;
  @Value("${spring.jwt.secret}")
  private String secretKey;
  private final AccessTokenRepository accessTokenRepository;

  public String generateToken(String username, List<String> roles) {
    Claims claims = Jwts.claims().subject(username).add(KEY_ROLES, roles).build();

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(expiredDate)
        .signWith(key, SIG.HS256)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
  public boolean validAccessToken(String token) {
    return !(
        !StringUtils.hasText(token)
            || !validateToken(token)
            || !token.equals(accessTokenRepository.get(Long.valueOf(getUsername(token)))));
  }
  private boolean validateToken(String token) {
    return !getExpiration(token).before(new Date());
  }

  public Duration getTokenDuration(String token) {
    Instant now = Instant.now();
    Instant expirationInstant = getExpiration(token).toInstant();

    if (expirationInstant.isBefore(now)) {
      throw new CustomException(TOKEN_EXPIRED);
    }

    return Duration.between(now, expirationInstant);
  }

  private Date getExpiration(String token) {
    if (!StringUtils.hasText(token)) {
      throw new CustomException(TOKEN_EXPIRED);
    }

    var claims = this.parseClaims(token);
    return claims.getExpiration();
  }

  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  @PostConstruct
  private void setKey() {
    key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
  }
}
