package com.raksit.example.order.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Optional<String> jwtToken = Optional.ofNullable(request.getHeader("Authorization"))
          .map(authorization -> authorization.replaceFirst("Bearer ", ""));

      Optional<DecodedJWT> decodedJwtToken = jwtToken.map(JWT::decode);

      List<String> roles = decodedJwtToken
          .map(decodedJWT -> decodedJWT.getClaim("roles").asList(String.class))
          .orElse(Collections.emptyList());

      List<GrantedAuthority> authorities = roles.stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      String appId = decodedJwtToken.map(token -> token.getClaim("appid").asString()).orElse("");
      String issuer = decodedJwtToken.map(Payload::getIssuer).orElse("");

      JwtUser user = new JwtUser(appId, issuer, roles);

      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(user, "", authorities));
    } catch (Exception e) {
      JwtUser user = new JwtUser("", "", Collections.emptyList());

      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList()));
    }

    filterChain.doFilter(request, response);
  }
}
