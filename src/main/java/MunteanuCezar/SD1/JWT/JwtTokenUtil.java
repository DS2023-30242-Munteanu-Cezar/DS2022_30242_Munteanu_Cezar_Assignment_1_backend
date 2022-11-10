package MunteanuCezar.SD1.JWT;


import MunteanuCezar.SD1.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 100;

    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateAccesToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername() + "," + user.getRole().getRoleCode())
                .setIssuer("Cezar")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
