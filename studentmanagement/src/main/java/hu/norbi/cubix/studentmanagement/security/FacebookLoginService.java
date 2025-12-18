package hu.norbi.cubix.studentmanagement.security;

import hu.norbi.cubix.studentmanagement.model.Student;
import hu.norbi.cubix.studentmanagement.model.StudentManagementUser;
import hu.norbi.cubix.studentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacebookLoginService {

    private static final String FACEBOOK_URL = "https://graph.facebook.com/v13.0";

    private final UserRepository userRepository;

    @Value("${university.fb-app-id}")
    private String fbAppId;

    @Getter
    @Setter
    public static class FacebookData{
        private String email;
        private long id;
    }

    @Getter
    @Setter
    public static class DebugTokenResponse {
        private DebugTokenData data;
    }

    @Getter
    @Setter
    public static class DebugTokenData {
        private String app_id;
    }

    @Transactional
    public UserDetails getUserDetailsForToken(String token) {
        FacebookData facebookData = getEmailOfFbUser(token);
        StudentManagementUser stUser = findOrCreateUser(facebookData);
        return StudentManagementUserDetailsService.createUserDetails(stUser);
    }

    private FacebookData getEmailOfFbUser(String fbToken) {
        return WebClient.create(FACEBOOK_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me")
                        .queryParam("fields", "email,name")
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(fbToken))
                .retrieve()
                .bodyToMono(FacebookData.class)
                .block();
    }



    private StudentManagementUser findOrCreateUser(FacebookData facebookData) {
        Optional<StudentManagementUser> byFacebookId = userRepository.findByFacebookId(String.valueOf(facebookData.id));

        return byFacebookId.orElse(userRepository.save(Student.builder()
                 .facebookId(String.valueOf(facebookData.id))
                .username(facebookData.email)
                .password("dummy")
                .name("name")
                .courses(new ArrayList<>())
                .build()));
    }

    private void checkAppId(String fbToken) {
        String appId =
                WebClient.create(FACEBOOK_URL)
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/debug_token")
                                .queryParam("input_token", fbToken)
                                .build())
                        .headers(headers -> headers.setBearerAuth(fbToken))
                        .retrieve()
                        .bodyToMono(DebugTokenResponse.class)
                        .block()
                        .getData().getApp_id();
        if(!fbAppId.equals(appId)) {
            throw new BadCredentialsException("The facebook auth token is for a different app!");
        }
    }

}
