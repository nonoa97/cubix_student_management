package hu.norbi.cubix.studentmanagement.repository;

import hu.norbi.cubix.studentmanagement.model.StudentManagementUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<StudentManagementUser, Integer> {

    Optional<StudentManagementUser> findByUsername(String username);
    Optional<StudentManagementUser> findByFacebookId(String facebookId);

    Optional<StudentManagementUser> findByGoogleId(String googleId);
}
