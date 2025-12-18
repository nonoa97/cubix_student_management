package hu.norbi.cubix.studentmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Cacheable
@SuperBuilder
@Audited
public class Teacher extends StudentManagementUser {

    @Override
    public UserType getUserType() {
        return UserType.TEACHER;
    }
}
