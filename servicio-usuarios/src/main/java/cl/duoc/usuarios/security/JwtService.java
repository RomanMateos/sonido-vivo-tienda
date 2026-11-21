package cl.duoc.usuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private SecretKey obtenerClaveFirma() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}