package hu.norbi.cubix.studentmanagement.security;

import hu.norbi.cubix.studentmanagement.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtLoginController {

    private final AuthenticationManager authenticationManager;
    private final FacebookLoginService facebookLoginService;
    private final GoogleLoginService googleLoginService;
    private final JwtService jwtService;

    @PostMapping("/api/login")
    public String login(@RequestBody LoginDto loginDto) {

        UserDetails userDetails = null;
        String fbToken = loginDto.getFbToken();
        String googleToken = loginDto.getGoogleToken();

        if (ObjectUtils.isEmpty(fbToken) && ObjectUtils.isEmpty(googleToken)) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            userDetails = (UserDetails) authentication.getPrincipal();
        }else if (ObjectUtils.isEmpty(googleToken)){

            userDetails = facebookLoginService.getUserDetailsForToken(fbToken);

        }else {
            userDetails = googleLoginService.getUserDetailsForGoogleToken(googleToken);
        }
        return "\""+ jwtService.creatJwtToken(userDetails) + "\"";
    }


}