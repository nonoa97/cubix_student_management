package hu.norbi.cubix.studentmanagement.security;

import hu.norbi.cubix.studentmanagement.model.Course;
import hu.norbi.cubix.studentmanagement.model.StudentManagementUser;
import hu.norbi.cubix.studentmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StudentManagementUserDetailsService implements UserDetailsService {

private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentManagementUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return createUserDetails(user);
    }

    public static UserDetails createUserDetails(StudentManagementUser user) {
        return new UserInfo(
                user.getUsername(),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getUserType().toString())),
                user.getCourses().stream().map(Course::getId).toList()
        );
    }


}
