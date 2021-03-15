package dev.cheun.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;

public class JwtUtil {
    // Load configs from .env file.
    private static final Dotenv dotenv = Dotenv.load();
    private static final String secret = dotenv.get("P1_JWT_SECRET");
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);

    public static String generate(int userId, int roleId) {
        String token = JWT.create()
                .withClaim("sub", userId)
                .withClaim("role", roleId)
                // Generates a signature based off of the claims.
                .sign(algorithm);
        return token;
    }

    public static DecodedJWT isValidJWT(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt;
    }
}
