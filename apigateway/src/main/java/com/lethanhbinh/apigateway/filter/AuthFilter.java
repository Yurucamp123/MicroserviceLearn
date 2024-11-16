package com.lethanhbinh.apigateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lethanhbinh.commonservice.exception.InvalidTokenException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new InvalidTokenException("Missing authorization information");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).getFirst();
            String parts[] = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new InvalidTokenException("Incorrect authorization structure");
            }

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parts[1]);
            String username = decodedJWT.getSubject();

            if (username == null || username.isEmpty()) {
                throw new InvalidTokenException("Invalid authorization information");
            }

            Date expiresAt = decodedJWT.getExpiresAt();
            if (expiresAt == null || expiresAt.before(new Date())) {
                throw new InvalidTokenException("Token has expired");
            }
            ServerHttpRequest request = exchange.getRequest().mutate().
                    header("X-auth-username", username).
                    build();

            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    static class Config {

    }
}
