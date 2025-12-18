package hu.norbi.cubix.studentmanagement.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String COURSE_IDS = "courseIds";
    private static final String AUTH = "auth";
    private Algorithm alg = Algorithm.HMAC256("mysecret");
    private String issuer = "StudentManagementApp";

    public String creatJwtToken(UserDetails principal) {
        UserInfo userInfo = (UserInfo) principal;
        return JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withArrayClaim(COURSE_IDS, userInfo.getCourseIds().toArray(Integer[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)))
                .withIssuer(issuer)
                .sign(alg);

    }

    public UserDetails parseJwt(String jwtToken) {

        DecodedJWT decodedJwt = JWT.require(alg)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);
        return new UserInfo(decodedJwt.getSubject(), "dummy",
                decodedJwt.getClaim(AUTH).asList(String.class)
                        .stream().map(SimpleGrantedAuthority::new).toList(),
                decodedJwt.getClaim(COURSE_IDS).asList(Integer.class)
        );

    }



}