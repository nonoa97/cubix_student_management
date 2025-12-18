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
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private static final String GOOGLE_URL = "https://oauth2.googleapis.com";

    private final UserRepository userRepository;

    @Value("${university.google-client-id}")
    private String googleClientId;

    @Getter
    @Setter
    public static class GoogleData{
        private String email;
        private String sub;
        private String aud;

        private String iss;
        private String azp;
        private String email_verified;
        private String at_hash;
        private String name;
        private String picture;
        private String given_name;
        private String family_name;
        private String locale;
        private String iat;
        private String exp;
        private String jti;
        private String alg;
        private String kid;
        private String typ;
    }

    @Transactional
    public UserDetails getUserDetailsForGoogleToken(String token) {
        GoogleData googleData = getGoogleData(token);
        if(!this.googleClientId.equals(googleData.getAud()))
            throw new BadCredentialsException("The aud claim of the google id token does not match the client id");

        StudentManagementUser stUser = findOrCreateUser(googleData);
        return StudentManagementUserDetailsService.createUserDetails(stUser);
    }

    private GoogleData getGoogleData(String googleToken) {
        return WebClient.create(GOOGLE_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tokeninfo")
                        .queryParam("id_token", googleToken)
                        .build())
                .retrieve()
                .bodyToMono(GoogleData.class)
                .block();
    }



    private StudentManagementUser findOrCreateUser(GoogleData googleData) {
        String googleId = googleData.getSub();
        Optional<StudentManagementUser> optionalExistingUser = userRepository.findByGoogleId(googleId);
        if(optionalExistingUser.isEmpty()) {

            return userRepository.save(Student.builder()
                    .googleId(googleId)
                    .username(googleData.getEmail())
                    .password("dummy")
                    .courses(Collections.emptyList())
                    .name(googleData.getName())
                    .build());
        }

        return optionalExistingUser.get();
    }


}
