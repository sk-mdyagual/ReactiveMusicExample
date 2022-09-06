package ec.com.reactive.music.user.jwt;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtProperties {
    //private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";
    /*SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
String secretString = Encoders.BASE64.encode(key.getEncoded());*/
    private String secretKey ="keep-it-quiet-or-your-secrets-will-be-revealed";

    // validity in milliseconds
    private long validityInMs = 3600000; // 1h
}
