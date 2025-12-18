package hu.norbi.cubix.studentmanagement.ws;

import hu.norbi.cubix.studentmanagement.security.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class CourseChatGuard {

    public boolean checkCourseId(Authentication authentication, Integer courseId) {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getCourseIds().contains(courseId);
    }


}
