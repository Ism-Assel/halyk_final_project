package kz.halykacademy.bookstore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String login) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(10).toInstant());

        return JWT.create()
                .withSubject("user details")
                .withClaim("login", login)
                .withIssuedAt(new Date())
                .withIssuer("BookStore")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("user details")
                .withIssuer("BookStore")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("login").toString();
    }
}